package com.tea.server.controller;


import cn.hutool.core.io.FileUtil;
import cn.hutool.crypto.digest.MD5;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tea.server.entity.*;
import com.tea.server.entity.data.AuthJson;

import com.tea.server.entity.vo.OutletVo;
import com.tea.server.service.AgentsService;
import com.tea.server.service.ShopsService;
import com.tea.server.utils.CoreUtil;
import com.tea.server.utils.R;
import eleme.openapi.sdk.api.service.ShopService;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import org.aspectj.weaver.loadtime.Agent;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 代理商户表 前端控制器
 * </p>
 *
 * @author zyy
 * @since 2024-04-28
 */
@RestController
@RequestMapping("agents")
public class AgentsController {
    @Autowired
    private AgentsService agentsService;

    @Autowired
    private ShopsService shopService;

    /**
     * 查询所有商户
     */
    @GetMapping("allList")
    public R allList() {
        LambdaQueryWrapper<Shop> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Shop::getStatus, 1).eq(Shop::getIsUse, 1);
        List<Shop> list = shopService.list(wrapper);
        return R.ok().data("list", list);
    }

    /**
     * 商户列表 分页
     */
    @PostMapping("getList/{page}/{limit}")
    public R getList(@PathVariable long page, @PathVariable long limit, @RequestBody Agents agentsQuery) {
        // 创建page对象
        Page<Agents> agentsPage = new Page<>(page, limit);

        // 构建条件
        LambdaQueryWrapper<Agents> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(Agents::getStatus, 1);
        String phone = agentsQuery.getPhone();

        if (null != phone && !phone.equals("")) {
            wrapper.like(Agents::getPhone, phone);
        }
        wrapper.orderByDesc(Agents::getId);

        agentsService.page(agentsPage, wrapper);

        List<Agents> records = agentsPage.getRecords();
        if (records.size() > 0) {
            records.stream().forEach(item -> {
                if (null != item.getShopId()) {
                    if (item.getShopId().contains(",")) {
                        List<Long> isList = Arrays.asList(item.getShopId().split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
                        LambdaQueryWrapper<Shop> wrapper2 = new LambdaQueryWrapper<>();
                        wrapper2.eq(Shop::getStatus, 1).in(Shop::getId, isList);
                        List<Shop> list = shopService.list(wrapper2);
                        String collect = list.stream().map(shop -> {
                            return shop.getName();
                        }).collect(Collectors.joining(","));
                        item.setShopName(collect);
                    } else {
                        Shop shop = shopService.getById(item.getShopId());
                        item.setShopName(shop.getName());
                    }
                }

            });
        }
        long total = agentsPage.getTotal();
        return R.ok().data("total", total).data("rows", records);
    }

    @ApiOperation(value = "根据id获取商户详情")
    @GetMapping("getAgentsInfo/{id}")
    public R getShopInfo(@PathVariable Long id) {
        Agents agents = agentsService.getById(id);
        String shopRange = agents.getShopId();
        if (null != shopRange && !shopRange.equals("")) {
            List<Long> shopRangeList = Arrays.asList(shopRange.split(",")).stream().map(Long::valueOf).collect(Collectors.toList());
            agents.setShopRangeList(shopRangeList);
            if (shopRangeList.size() > 0) {
                List<String> list = new ArrayList<>();
                shopRangeList.stream().forEach(item -> {
                    Shop shop = shopService.getById(item);
                    if (null != shop) {
                        list.add(shop.getName());
                    }

                });
                if (list.size() > 0) {
                    String shopName = list.stream().map(String::valueOf).collect(Collectors.joining(","));
                    agents.setShopName(shopName);
                }
            }
        }


        return R.ok().data("info", agents);

    }

    /**
     * 添加
     *
     * @return
     */
    @SneakyThrows
    @PostMapping("insertData")
    public R insertData(@RequestBody Agents obj) {
        Agents agents = new Agents();
        LambdaQueryWrapper<Agents> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Agents::getStatus, 1).eq(Agents::getCity, obj.getCity()).eq(Agents::getPhone, obj.getPhone());
        List<Agents> list = agentsService.list(wrapper);
        if (null != list && list.size() > 0) {
            return R.ok().message("该代理已添加");
        } else {
            BeanUtils.copyProperties(obj, agents);
            List<Long> shopRangeList = obj.getShopRangeList();
            String shopRange = shopRangeList.stream().map(String::valueOf).collect(Collectors.joining(","));
            agents.setShopId(shopRange);
            agents.setCreateTime(LocalDateTime.now());
            agents.setUpdateTime(LocalDateTime.now());
            boolean save = agentsService.save(agents);
            if (save) {
                return R.ok();
            } else {
                return R.error();
            }
        }

    }

    /**
     * 修改
     *
     * @return
     */
    @SneakyThrows
    @PostMapping("updateData/{id}")
    public R updateData(@PathVariable long id, @RequestBody Agents obj) {
        Agents agents = agentsService.getById(id);
        LambdaQueryWrapper<Agents> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Agents::getStatus, 1).eq(Agents::getPhone, obj.getPhone()).eq(Agents::getCity, obj.getCity()).ne(Agents::getId, id);
        List<Agents> list = agentsService.list(wrapper);
        if (null != list && list.size() > 0) {
            return R.ok().message("该代理已添加");
        } else {
            List<Long> shopRangeList = obj.getShopRangeList();
            String shopRange = shopRangeList.stream().map(String::valueOf).collect(Collectors.joining(","));
            Agents agents1 = new Agents();
            BeanUtils.copyProperties(agents, agents1);
            agents1.setShopId(shopRange);
            agents1.setIsUse(obj.getIsUse());
            agents1.setDivideAccounts(obj.getDivideAccounts());
            agents1.setProportion(obj.getProportion());
            agents1.setArea(obj.getArea());
            agents1.setProvince(obj.getProvince());
            agents1.setCity(obj.getCity());
            agents1.setPhone(obj.getPhone());
            agents1.setUpdateTime(LocalDateTime.now());
            boolean save = agentsService.updateById(agents1);
            if (save) {
                return R.ok();
            } else {
                return R.error();
            }
        }

    }

}

