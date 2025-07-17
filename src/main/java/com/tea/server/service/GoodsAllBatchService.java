package com.tea.server.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tea.server.entity.GoodsAllBatch;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tea.server.entity.vo.GoodsAllBatchList;
import com.tea.server.entity.vo.GoodsBatchList;

/**
 * <p>
 * 商品与配料对应关系表 服务类
 * </p>
 *
 * @author zyy
 * @since 2024-04-03
 */
public interface GoodsAllBatchService extends IService<GoodsAllBatch> {

    // 产品配方列表
    Page<GoodsAllBatchList> getGoodsAllBatchingList(Page<GoodsAllBatchList> goodsBatchListPage, String keyword);
}
