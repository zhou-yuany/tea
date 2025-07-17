package com.tea.server.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tea.server.entity.BatchUse;
import com.tea.server.entity.vo.BatchingVo;
import com.tea.server.mapper.BatchUseMapper;
import com.tea.server.service.BatchUseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 店家商品与配方消耗对应关系表 服务实现类
 * </p>
 *
 * @author zyy
 * @since 2023-08-24
 */
@Service
public class BatchUseServiceImpl extends ServiceImpl<BatchUseMapper, BatchUse> implements BatchUseService {

    @Override
    public Page<BatchUse> pageList(Page<BatchUse> batchPage, Long shopId) {
        Page<BatchUse> list = baseMapper.pageList(batchPage, shopId);
        return list;
    }

    @Override
    public Page<BatchingVo> pageInventories(Page<BatchingVo> batchingVoPage, Long shopId) {
        Page<BatchingVo> list = baseMapper.pageInventories(batchingVoPage, shopId);
        return list;
    }

    @Override
    public List<BatchUse> getListByName(Long id, String keyword) {
        List<BatchUse> list = baseMapper.getListByName(id, keyword);
        return list;
    }
}
