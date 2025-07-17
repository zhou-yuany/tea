package com.tea.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tea.server.entity.BatchNumber;
import com.tea.server.entity.Batching;
import com.tea.server.mapper.BatchNumberMapper;
import com.tea.server.mapper.BatchingMapper;
import com.tea.server.service.BatchingService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 商品配料表 服务实现类
 * </p>
 *
 * @author zyy
 * @since 2023-08-24
 */
@Service
public class BatchingServiceImpl extends ServiceImpl<BatchingMapper, Batching> implements BatchingService {

    @Override
    public Page<Batching> getBatchList(Page<Batching> batchingPage, String keyword) {
        Page<Batching> list = baseMapper.getBatchList(batchingPage, keyword);
        return list;
    }

    @Resource
    private BatchNumberMapper batchNumberMapper;

    @Override
    public List<Batching> getBatchBrands(Long shopId) {
        List<Batching> list = new ArrayList<>();
        LambdaQueryWrapper<BatchNumber> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BatchNumber :: getShopId, shopId).eq(BatchNumber :: getStatus, 1);
        List<BatchNumber> batchNumbers = batchNumberMapper.selectList(queryWrapper);
        List<Long> batchIdList = batchNumbers.stream().map(item -> item.getBatchId()).collect(Collectors.toList());
        if (batchIdList.size() > 0) {
            LambdaQueryWrapper<Batching> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Batching :: getStatus, 1).in(Batching :: getId, batchIdList).groupBy(Batching :: getBrand);
            list = baseMapper.selectList(wrapper);

        }
        return list;

    }

    @Override
    public List<Batching> getBatchNames(Long shopId) {
        List<Batching> list = new ArrayList<>();
        LambdaQueryWrapper<BatchNumber> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BatchNumber :: getShopId, shopId).eq(BatchNumber :: getStatus, 1);
        List<BatchNumber> batchNumbers = batchNumberMapper.selectList(queryWrapper);
        List<Long> batchIdList = batchNumbers.stream().map(item -> item.getBatchId()).collect(Collectors.toList());
        if (batchIdList.size() > 0) {
            LambdaQueryWrapper<Batching> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Batching :: getStatus, 1).in(Batching :: getId, batchIdList);
            list = baseMapper.selectList(wrapper);

        }
        return list;
    }

    @Override
    public List<BatchNumber> getBatchNumbers(Long shopId) {
        LambdaQueryWrapper<BatchNumber> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BatchNumber :: getShopId, shopId).eq(BatchNumber :: getStatus, 1);
        List<BatchNumber> batchNumbers = batchNumberMapper.selectList(queryWrapper);
        return batchNumbers;
    }
}
