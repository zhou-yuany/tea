package com.tea.server.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tea.server.entity.Equipment;
import com.tea.server.entity.EquipmentAll;
import com.tea.server.service.EquipmentAllService;
import com.tea.server.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 平台设备表 前端控制器
 * </p>
 *
 * @author zyy
 * @since 2024-05-10
 */
@RestController
@RequestMapping("equipmentAll")
public class EquipmentAllController {

    @Autowired
    private EquipmentAllService equipmentAllService;

    /**
     * 所有设备
     */
    @GetMapping("allList")
    public R allList(){
        LambdaQueryWrapper<EquipmentAll> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EquipmentAll :: getStatus, 1).eq(EquipmentAll :: getIsBinding, 2);
        List<EquipmentAll> list = equipmentAllService.list(wrapper);
        return R.ok().data("list", list);
    }

    /**
     * 总设备列表 分页
     */
    @PostMapping("getEquipmentList/{page}/{limit}")
    public R getEquipmentList(@PathVariable long page, @PathVariable long limit, @RequestParam(required = false) Integer type) {
        // 创建page对象
        Page<EquipmentAll> equipmentPage = new Page<>(page, limit);

        // 构建条件
        LambdaQueryWrapper<EquipmentAll> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(EquipmentAll::getStatus, 1);

        if (null != type && type > 0) {
            wrapper.like(EquipmentAll::getType, type);
        }
        wrapper.orderByDesc(EquipmentAll::getId);

        equipmentAllService.page(equipmentPage, wrapper);

        List<EquipmentAll> records = equipmentPage.getRecords();
        long total = equipmentPage.getTotal();
        return R.ok().data("total", total).data("rows", records);
    }

    /**
     * 添加设备
     *
     * @return
     */
    @PostMapping("insertData")
    public R insertData(@RequestBody EquipmentAll obj) {
        // 查询设备是否已经添加过
        LambdaQueryWrapper<EquipmentAll> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EquipmentAll :: getStatus, 1).eq(EquipmentAll :: getEquipmentNo, obj.getEquipmentNo());
        List<EquipmentAll> list = equipmentAllService.list(wrapper);
        if (null != list && list.size() > 0){
            return R.error().message("该设备编号已添加过！");
        }else {
            EquipmentAll equipmentAll = new EquipmentAll();
            equipmentAll.setEquipmentNo(obj.getEquipmentNo());
            equipmentAll.setType(obj.getType());
            equipmentAll.setCreateTime(LocalDateTime.now());
            equipmentAll.setUpdateTime(LocalDateTime.now());
            boolean save = equipmentAllService.save(equipmentAll);
            if (save) {
                return R.ok();
            } else {
                return R.error();
            }
        }

    }


}

