package com.tea.server.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tea.server.entity.*;
import com.tea.server.service.CategorizeAllService;
import com.tea.server.service.CategorizeService;
import com.tea.server.service.EquipmentService;
import com.tea.server.service.ShopsService;
import com.tea.server.utils.R;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 商品分类表 前端控制器
 * </p>
 *
 * @author zyy
 * @since 2023-08-24
 */
@RestController
@RequestMapping("categorize")
public class CategorizeController {

    @Autowired
    private CategorizeService categorizeService;

    @Autowired
    private ShopsService shopsService;

    @Autowired
    private CategorizeAllService categorizeAllService;
    
    @Autowired
    private EquipmentService equipmentService;

    /**
     * 所有商家自己的分类 和设备编号
     */
    @GetMapping("allList/{shopId}")
    public R allList(@PathVariable Long shopId) {
        LambdaQueryWrapper<Categorize> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Categorize::getShopId, shopId).eq(Categorize::getStatus, 1);
        List<Categorize> list = categorizeService.list(wrapper);
        // List<String> equipmentList = new ArrayList<>();
        // Shop shop = shopsService.getById(shopId);
        // String equipmentId = shop.getEquipmentId();
        // if (null != equipmentId){
        //     if (equipmentId.contains(",")){
        //         List<String> stringList = Arrays.asList(equipmentId.split(","));
        //         equipmentList.addAll(stringList);
        //     }else {
        //         equipmentList.add(equipmentId);
        //     }
        // }
        LambdaQueryWrapper<Equipment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Equipment :: getStatus, 1).eq(Equipment :: getShopId, shopId);
        List<Equipment> equipmentList = equipmentService.list(queryWrapper);

        return R.ok().data("list", list).data("equipmentList", equipmentList);
    }

    /**
     * 总分类列表 分页
     */
    @PostMapping("getCateList/{page}/{limit}/{shopId}")
    public R getCateList(@PathVariable long page, @PathVariable long limit, @PathVariable Long shopId, @RequestParam String keyword) {
        // 创建page对象
        Page<Categorize> categorizePage = new Page<>(page, limit);

        // 构建条件
        LambdaQueryWrapper<Categorize> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(Categorize::getStatus, 1).eq(Categorize::getShopId, shopId);

        if (null != keyword && !keyword.equals("")) {
            wrapper.like(Categorize::getName, keyword).or().like(Categorize::getRealName, keyword);
        }
        wrapper.orderByDesc(Categorize::getId);

        categorizeService.page(categorizePage, wrapper);

        List<Categorize> records = categorizePage.getRecords();
        long total = categorizePage.getTotal();
        return R.ok().data("total", total).data("rows", records);
    }

    @GetMapping("getCateInfo/{id}")
    public R getCateInfo(@PathVariable Long id) {
        Categorize categorize = categorizeService.getById(id);
        return R.ok().data("info", categorize);
    }

    /**
     * 添加
     *
     * @return
     */
    @PostMapping("insertData")
    public R insertData(@RequestBody Categorize obj) {
        Categorize categorize = new Categorize();
        CategorizeAll categorizeAll = categorizeAllService.getById(obj.getCateAllId());
        BeanUtils.copyProperties(obj, categorize);
        categorize.setRealName(categorizeAll.getName());
        categorize.setCreateTime(LocalDateTime.now());
        categorize.setUpdateTime(LocalDateTime.now());
        boolean save = categorizeService.save(categorize);
        if (save) {
            return R.ok();
        } else {
            return R.error();
        }


    }

    /**
     * 修改
     *
     * @return
     */
    @PostMapping("updateData/{id}")
    public R updateData(@PathVariable long id, @RequestBody Categorize obj) {

        Categorize categorize = categorizeService.getById(id);
        CategorizeAll categorizeAll = categorizeAllService.getById(obj.getCateAllId());
        categorize.setCateAllId(obj.getCateAllId());
        categorize.setName(obj.getName());
        categorize.setMachineNo(obj.getMachineNo());
        categorize.setUpdateTime(LocalDateTime.now());
        categorize.setRealName(categorizeAll.getName());
        boolean save = categorizeService.updateById(categorize);
        if (save) {
            return R.ok();
        } else {
            return R.error();
        }
    }


    @DeleteMapping("{id}")
    public R remove(@PathVariable("id") Long id) {
        boolean b = categorizeService.removeById(id);
        if (b) {
            return R.ok();
        } else {
            return R.error();
        }
    }


}

