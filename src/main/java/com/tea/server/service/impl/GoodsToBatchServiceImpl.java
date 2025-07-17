package com.tea.server.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tea.server.entity.GoodsToBatch;
import com.tea.server.entity.query.GoodsBatchQuery;
import com.tea.server.entity.vo.CostCardVo;
import com.tea.server.entity.vo.GoodsBatchList;
import com.tea.server.entity.vo.GoodsToBatchList;
import com.tea.server.mapper.GoodsToBatchMapper;
import com.tea.server.service.GoodsToBatchService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 商品与配料对应关系表 服务实现类
 * </p>
 *
 * @author zyy
 * @since 2023-08-24
 */
@Service
public class GoodsToBatchServiceImpl extends ServiceImpl<GoodsToBatchMapper, GoodsToBatch> implements GoodsToBatchService {
    @Override
    public Page<CostCardVo> pageCostCards(Page<CostCardVo> costCardVoPage, Long shopId) {
        Page<CostCardVo> list = baseMapper.pageCostCards(costCardVoPage, shopId);
        return list;
    }

    @Override
    public Page<GoodsBatchList> getGoodsBatchingList1(Page<GoodsBatchList> goodsBatchListPage, Long shopId, String keyword) {
        Page<GoodsBatchList> list = baseMapper.getGoodsBatchingList1(goodsBatchListPage, shopId, keyword);
        return list;
    }

    @Override
    public List<GoodsBatchList> getGoodsBatchings(Long id, String keyword) {
        List<GoodsBatchList> list = baseMapper.getGoodsBatchings(id, keyword);
        return list;
    }

    @Override
    public List<GoodsToBatchList> getGoodsAllBatchingList(Long shopId, GoodsBatchQuery goodsBatchQuery) {
        List<GoodsToBatchList> list = baseMapper.getGoodsAllBatchingList(shopId, goodsBatchQuery);
        return list;
    }
    @Override
    public Page<GoodsBatchList> getGoodsBatchingList(Page<GoodsBatchList> goodsBatchListPage, Long shopId, String keyword) {
        Page<GoodsBatchList> list = baseMapper.getGoodsBatchingList(goodsBatchListPage, shopId, keyword);
        return list;
    }

}
