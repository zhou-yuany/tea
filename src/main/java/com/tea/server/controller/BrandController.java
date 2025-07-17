package com.tea.server.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tea.server.entity.Brand;
import com.tea.server.entity.EquipmentAll;
import com.tea.server.entity.query.BrandQuery;
import com.tea.server.service.BrandService;
import com.tea.server.utils.R;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 品牌表 前端控制器
 * </p>
 *
 * @author zyy
 * @since 2023-08-24
 */
@RestController
@RequestMapping("brand")
public class BrandController {

    @Autowired
    private BrandService brandService;

    /**
     * 查询所有品牌
     * @return
     */
    @GetMapping("allList")
    public R allList(){
        List<Brand> list = brandService.list(null);
        return R.ok().data("list", list);
    }

    /**
     * 总设备列表 分页
     */
    @PostMapping("getBrandList/{page}/{limit}")
    public R getBrandList(@PathVariable long page, @PathVariable long limit, @RequestBody BrandQuery brandQuery) {
        // 创建page对象
        Page<Brand> brandPage = new Page<>(page, limit);

        // 构建条件
        LambdaQueryWrapper<Brand> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(Brand::getStatus, 1);

        String name = brandQuery.getName();
        String phone = brandQuery.getPhone();

        if (null != name && !name.equals("")) {
            wrapper.like(Brand::getName, name);
        }
        if (null != phone && !phone.equals("")) {
            wrapper.like(Brand::getPhone, phone);
        }
        wrapper.orderByDesc(Brand::getId);

        brandService.page(brandPage, wrapper);

        List<Brand> records = brandPage.getRecords();
        long total = brandPage.getTotal();
        return R.ok().data("total", total).data("rows", records);
    }

    /**
     * 添加设备
     *
     * @return
     */
    @PostMapping("insertData")
    public R insertData(@RequestBody Brand obj) {
        // 查询设备是否已经添加过
        LambdaQueryWrapper<Brand> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Brand :: getStatus, 1).eq(Brand :: getName, obj.getName()).eq(Brand :: getManufacturer, obj.getManufacturer());
        List<Brand> list = brandService.list(wrapper);
        if (null != list && list.size() > 0){
            return R.error().message("该品牌已添加过！");
        }else {
            Brand brand = new Brand();
            BeanUtils.copyProperties(obj, brand);
            brand.setCreateTime(LocalDateTime.now());
            brand.setUpdateTime(LocalDateTime.now());
            boolean save = brandService.save(brand);
            if (save) {
                return R.ok();
            } else {
                return R.error();
            }
        }

    }

    /**
     * 修改设备
     *
     * @return
     */
    @PostMapping("updateData")
    public R updateData(@RequestBody Brand obj) {
        // 查询设备是否已经添加过
        LambdaQueryWrapper<Brand> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Brand :: getStatus, 1).eq(Brand :: getName, obj.getName()).eq(Brand :: getManufacturer, obj.getManufacturer()).ne(Brand :: getId, obj.getId());
        List<Brand> list = brandService.list(wrapper);
        if (null != list && list.size() > 0){
            return R.error().message("该品牌已添加过！");
        }else {
            Brand brand = new Brand();
            BeanUtils.copyProperties(obj, brand);
            brand.setUpdateTime(LocalDateTime.now());
            boolean save = brandService.updateById(brand);
            if (save) {
                return R.ok();
            } else {
                return R.error();
            }
        }

    }

    @GetMapping("getInfo/{id}")
    public R getInfo(@PathVariable Long id){
        Brand info = brandService.getById(id);
        return R.ok().data("info", info);
    }



}

