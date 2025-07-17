package com.tea.server.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tea.server.entity.BatchOrderUse;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tea.server.entity.query.BatchUseQuery;

import java.util.List;

/**
 * <p>
 * 物料消耗-订单表 服务类
 * </p>
 *
 * @author zyy
 * @since 2024-05-24
 */
public interface BatchOrderUseService extends IService<BatchOrderUse> {

    // 商户竖屏 小屏 物料消耗
    List<BatchOrderUse> shopBatchUseList(Long adminId, BatchUseQuery batchUseQuery);

    // 总后台 查询所有商户 物料消耗
    List<BatchOrderUse> batchUseList(BatchUseQuery batchUseQuery);

    // 总后台 查询所有商户 物料消耗 分页
    Page<BatchOrderUse> pageBatchUseList(Page<BatchOrderUse> batchListPage, BatchUseQuery batchUseQuery);
}
