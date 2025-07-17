package com.tea.server.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tea.server.entity.*;
import com.tea.server.entity.query.ShopQuery;
import com.tea.server.service.EquipmentService;
import com.tea.server.service.InterfaceLogService;
import com.tea.server.service.ShopsService;
import com.tea.server.utils.R;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 店家设备表 前端控制器
 * </p>
 *
 * @author zyy
 * @since 2024-05-08
 */
@RestController
@RequestMapping("equipment")
public class EquipmentController {

    @Autowired
    private EquipmentService equipmentService;

    @Autowired
    private ShopsService shopsService;

    @Autowired
    private InterfaceLogService interfaceLogService;

    /**
     * 店家设备列表 分页
     */
    @PostMapping("getEquipmentList/{page}/{limit}/{shopId}")
    public R getEquipmentList(@PathVariable long page, @PathVariable long limit, @PathVariable Long shopId, @RequestParam String keyword) {
        // 创建page对象
        Page<Equipment> equipmentPage = new Page<>(page, limit);

        // 构建条件
        LambdaQueryWrapper<Equipment> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(Equipment::getStatus, 1).eq(Equipment::getShopId, shopId);

        if (null != keyword && !keyword.equals("")) {
            wrapper.like(Equipment::getName, keyword);
        }
        wrapper.orderByDesc(Equipment::getId);

        equipmentService.page(equipmentPage, wrapper);

        List<Equipment> records = equipmentPage.getRecords();
        long total = equipmentPage.getTotal();
        return R.ok().data("total", total).data("rows", records);
    }

    @GetMapping("getEquipmentInfo/{id}")
    public R getEquipmentInfo(@PathVariable Long id) {
        Equipment equipment = equipmentService.getById(id);
        return R.ok().data("info", equipment);
    }


    /**
     * 修改设备别名
     *
     * @return
     */
    @PostMapping("updateData/{id}")
    public R updateData(@PathVariable long id, @RequestBody Equipment obj) {

        Equipment equipment = equipmentService.getById(id);
        equipment.setUpdateTime(LocalDateTime.now());
        equipment.setName(obj.getName());
        boolean save = equipmentService.updateById(equipment);
        if (save) {
            Shop shop = shopsService.getById(equipment.getShopId());
            InterfaceLog interfaceLog = new InterfaceLog();
            interfaceLog.setTitle("商户修改设备别名");
            interfaceLog.setMethodName("updateData");
            String content = "商户"+shop.getName()+"修改设备别名，修改后的别名为"+obj.getName();
            interfaceLog.setContent(content);
            interfaceLog.setShopId(equipment.getShopId());
            interfaceLog.setTypeStatus(0);
            interfaceLog.setCreateTime(LocalDateTime.now());
            interfaceLog.setUpdateTime(LocalDateTime.now());
            interfaceLogService.save(interfaceLog);
            return R.ok();
        } else {
            return R.error();
        }
    }


}

