package com.tea.server.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tea.server.entity.BatchNumber;
import com.tea.server.entity.Batching;
import com.tea.server.entity.InventoryRecord;
import com.tea.server.service.BatchingService;
import com.tea.server.service.InventoryRecordService;
import com.tea.server.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 配料出/入库记录表 前端控制器
 * </p>
 *
 * @author zyy
 * @since 2023-08-24
 */
@RestController
@RequestMapping("inventoryRecord")
public class InventoryRecordController {
    @Autowired
    private InventoryRecordService inventoryRecordService;

    @Autowired
    private BatchingService batchingService;

    /**
     * 根据店铺id获取当前出入库记录 分页
     */
    @PostMapping("getInventoryRecords/{page}/{limit}/{shopId}/{type}")
    public R getInventoryRecords(@PathVariable long page, @PathVariable long limit, @PathVariable Long shopId, @PathVariable Integer type){
        // 创建page对象
        Page<InventoryRecord> inventory = new Page<>(page, limit);

        // 构建条件
        Page<InventoryRecord> pageList = inventoryRecordService.pageInventoryRecords(inventory, shopId, type);

        long total = pageList.getTotal();
        List<InventoryRecord> records = pageList.getRecords();
        return R.ok().data("total", total).data("rows", records);
    }

    /**
     * 入库
     * @param obj
     * @return
     */
    @PostMapping("insertInventory")
    public R insertInventory(@RequestBody InventoryRecord obj){
        Integer num = inventoryRecordService.insertInventory(obj);
        if (num > 0){
            return R.ok();
        }else {
            return R.error();
        }
    }

    /**
     * 出库
     */
    @PostMapping("outInventory/{batchNumberId}/{count}")
    public R outInventory(@PathVariable("batchNumberId") Long batchNumberId, @PathVariable("count") Integer count, @RequestParam String remarks){
        Integer num = inventoryRecordService.outInventory(batchNumberId, count, remarks);
        if (num > 0){
            return R.ok();
        }else {
            return R.error();
        }
    }

    /**
     * 当前店铺配料的所有品牌
     */
    @GetMapping("getBatchBrands/{shopId}")
    public R getBatchBrands(@PathVariable Long shopId){
        List<Batching> list = batchingService.getBatchBrands(shopId);
        return R.ok().data("list", list);
    }

    /**
     * 当前店铺配料的所有配料名称
     */
    @GetMapping("getBatchNames/{shopId}")
    public R getBatchNames(@PathVariable Long shopId){
        List<Batching> list = batchingService.getBatchNames(shopId);
        return R.ok().data("list", list);
    }

    /**
     * 当前店铺所选配料的所有配料规格
     */
    @GetMapping("getBatchNumbers/{shopId}")
    public R getBatchNumbers(@PathVariable Long shopId){
        List<BatchNumber> list = batchingService.getBatchNumbers(shopId);
        return R.ok().data("list", list);
    }

}

