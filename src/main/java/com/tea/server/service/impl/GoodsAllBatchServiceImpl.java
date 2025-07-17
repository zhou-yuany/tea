package com.tea.server.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tea.server.entity.GoodsAllBatch;
import com.tea.server.entity.vo.GoodsAllBatchList;
import com.tea.server.entity.vo.GoodsBatchList;
import com.tea.server.mapper.GoodsAllBatchMapper;
import com.tea.server.service.GoodsAllBatchService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 商品与配料对应关系表 服务实现类
 * </p>
 *
 * @author zyy
 * @since 2024-04-03
 */
@Service
public class GoodsAllBatchServiceImpl extends ServiceImpl<GoodsAllBatchMapper, GoodsAllBatch> implements GoodsAllBatchService {

    @Override
    public Page<GoodsAllBatchList> getGoodsAllBatchingList(Page<GoodsAllBatchList> goodsBatchListPage, String keyword) {
        Page<GoodsAllBatchList> list = baseMapper.getGoodsAllBatchingList(goodsBatchListPage, keyword);
        return list;
    }
}
