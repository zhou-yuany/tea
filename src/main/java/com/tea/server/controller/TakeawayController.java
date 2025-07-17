package com.tea.server.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tea.server.entity.Agents;
import com.tea.server.entity.Shop;
import com.tea.server.entity.Takeaway;
import com.tea.server.service.TakeawayService;
import com.tea.server.utils.R;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 骑手管理表 前端控制器
 * </p>
 *
 * @author zyy
 * @since 2024-09-09
 */
@RestController
@RequestMapping("takeaway")
public class TakeawayController {

    @Autowired
    private TakeawayService takeawayService;

    /**
     * 列表 分页
     */
    @PostMapping("getList/{page}/{limit}")
    public R getList(@PathVariable long page, @PathVariable long limit, @RequestBody Takeaway takeawayQuery) {
        // 创建page对象
        Page<Takeaway> takeawayPage = new Page<>(page, limit);

        // 构建条件
        LambdaQueryWrapper<Takeaway> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(Takeaway::getStatus, 1);
        String phone = takeawayQuery.getPhone();

        if (null != phone && !phone.equals("")) {
            wrapper.like(Takeaway::getPhone, phone);
        }
        wrapper.orderByDesc(Takeaway::getId);

        takeawayService.page(takeawayPage, wrapper);

        List<Takeaway> records = takeawayPage.getRecords();

        long total = takeawayPage.getTotal();
        return R.ok().data("total", total).data("rows", records);
    }

    @ApiOperation(value = "根据id获取商户详情")
    @GetMapping("getInfo/{id}")
    public R getInfo(@PathVariable Long id) {
        Takeaway takeaway = takeawayService.getById(id);
        return R.ok().data("info", takeaway);

    }

    /**
     * 添加
     *
     * @return
     */
    @SneakyThrows
    @PostMapping("insertData")
    public R insertData(@RequestBody Takeaway obj) {
        Takeaway takeaway = new Takeaway();
        LambdaQueryWrapper<Takeaway> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Takeaway::getStatus, 1).eq(Takeaway::getCity, obj.getCity()).eq(Takeaway::getPhone, obj.getPhone());
        List<Takeaway> list = takeawayService.list(wrapper);
        if (null != list && list.size() > 0) {
            return R.ok().message("该骑手已添加");
        } else {
            BeanUtils.copyProperties(obj, takeaway);
            takeaway.setCreateTime(LocalDateTime.now());
            takeaway.setUpdateTime(LocalDateTime.now());
            boolean save = takeawayService.save(takeaway);
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
    public R updateData(@PathVariable long id, @RequestBody Takeaway obj) {
        Takeaway takeaway = takeawayService.getById(id);
        LambdaQueryWrapper<Takeaway> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Takeaway::getStatus, 1).eq(Takeaway::getPhone, obj.getPhone()).eq(Takeaway::getCity, obj.getCity()).ne(Takeaway::getId, id);
        List<Takeaway> list = takeawayService.list(wrapper);
        if (null != list && list.size() > 0) {
            return R.ok().message("该骑手已添加");
        } else {
            Takeaway takeaway1 = new Takeaway();
            BeanUtils.copyProperties(takeaway, takeaway1);
            takeaway1.setIsUse(obj.getIsUse());
            takeaway1.setDivideAccounts(obj.getDivideAccounts());
            takeaway1.setProportion(obj.getProportion());
            takeaway1.setArea(obj.getArea());
            takeaway1.setProvince(obj.getProvince());
            takeaway1.setCity(obj.getCity());
            takeaway1.setName(obj.getName());
            takeaway1.setPhone(obj.getPhone());
            takeaway1.setUpdateTime(LocalDateTime.now());
            boolean save = takeawayService.updateById(takeaway1);
            if (save) {
                return R.ok();
            } else {
                return R.error();
            }
        }

    }

}

