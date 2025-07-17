package com.tea.server.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tea.server.entity.GoodsToBatch;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tea.server.entity.query.GoodsBatchQuery;
import com.tea.server.entity.vo.CostCardVo;
import com.tea.server.entity.vo.GoodsBatchList;
import com.tea.server.entity.vo.GoodsToBatchList;

import java.util.List;

/**
 * <p>
 * 商品与配料对应关系表 服务类
 * </p>
 *
 * @author zyy
 * @since 2023-08-24
 */
public interface GoodsToBatchService extends IService<GoodsToBatch> {
    // 根据店铺id获取当前商品成本卡 分页
    Page<CostCardVo> pageCostCards(Page<CostCardVo> costCardVoPage, Long shopId);

    // 产品配方列表
    Page<GoodsBatchList> getGoodsBatchingList1(Page<GoodsBatchList> goodsBatchListPage, Long shopId, String keyword);

    // 茶饮竖屏 产品配方列表
    List<GoodsBatchList> getGoodsBatchings(Long id, String keyword);

    // 茶饮竖屏 配方列表
    List<GoodsToBatchList> getGoodsAllBatchingList(Long shopId, GoodsBatchQuery goodsBatchQuery);

    Page<GoodsBatchList> getGoodsBatchingList(Page<GoodsBatchList> goodsBatchListPage, Long shopId, String keyword);
}
