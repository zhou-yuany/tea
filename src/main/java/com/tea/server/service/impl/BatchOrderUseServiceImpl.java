package com.tea.server.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tea.server.entity.BatchOrderUse;
import com.tea.server.entity.query.BatchUseQuery;
import com.tea.server.mapper.BatchOrderUseMapper;
import com.tea.server.service.BatchOrderUseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 物料消耗-订单表 服务实现类
 * </p>
 *
 * @author zyy
 * @since 2024-05-24
 */
@Service
public class BatchOrderUseServiceImpl extends ServiceImpl<BatchOrderUseMapper, BatchOrderUse> implements BatchOrderUseService {

    @Override
    public List<BatchOrderUse> shopBatchUseList(Long adminId, BatchUseQuery batchUseQuery) {
        List<BatchOrderUse> list = baseMapper.shopBatchUseList(adminId, batchUseQuery);
        return list;
    }

    @Override
    public List<BatchOrderUse> batchUseList(BatchUseQuery batchUseQuery) {
        List<BatchOrderUse> list = baseMapper.batchUseList(batchUseQuery);
        return list;
    }

    @Override
    public Page<BatchOrderUse> pageBatchUseList(Page<BatchOrderUse> batchListPage, BatchUseQuery batchUseQuery) {
        Page<BatchOrderUse> list = baseMapper.pageBatchUseList(batchListPage, batchUseQuery);
        return list;
    }
}
