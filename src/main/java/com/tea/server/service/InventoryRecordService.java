package com.tea.server.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tea.server.entity.InventoryRecord;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 配料出/入库记录表 服务类
 * </p>
 *
 * @author zyy
 * @since 2023-08-24
 */
public interface InventoryRecordService extends IService<InventoryRecord> {
    // 根据店铺id获取当前出入库记录 分页
    Page<InventoryRecord> pageInventoryRecords(Page<InventoryRecord> inventory, Long shopId, Integer type);

    // 入库
    Integer insertInventory(InventoryRecord obj);

    // 入库
    Integer outInventory(Long batchNumberId, Integer count, String remarks);

}
