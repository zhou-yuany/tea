package com.tea.server.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tea.server.entity.ParamBatch;
import com.tea.server.entity.vo.ParamBatchVo;
import com.tea.server.mapper.ParamBatchMapper;
import com.tea.server.service.ParamBatchService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 * 规格参数配方表 服务实现类
 * </p>
 *
 * @author zyy
 * @since 2023-08-24
 */
@Service
public class ParamBatchServiceImpl extends ServiceImpl<ParamBatchMapper, ParamBatch> implements ParamBatchService {
    @Override
    public Integer addParamBatch(ParamBatch paramBatch) {
        ParamBatch paramBatch1 = new ParamBatch();
        BeanUtils.copyProperties(paramBatch, paramBatch1);
        paramBatch1.setCreateTime(LocalDateTime.now());
        paramBatch1.setUpdateTime(LocalDateTime.now());
        paramBatch1.setHeatUnit("g");
        paramBatch1.setSugarUnit("g");
        int insert = baseMapper.insert(paramBatch1);
        return insert;
    }

    @Override
    public Integer updateParamBatch(ParamBatch paramBatch) {
        ParamBatch paramBatch1 = new ParamBatch();
        BeanUtils.copyProperties(paramBatch, paramBatch1);
        paramBatch1.setUpdateTime(LocalDateTime.now());
        int i = baseMapper.updateById(paramBatch1);
        return i;
    }

    @Override
    public Page<ParamBatchVo> pageList(Page<ParamBatchVo> paramBatchVoPage, Long shopId, String keyword) {
        Page<ParamBatchVo> list = baseMapper.pageList(paramBatchVoPage, shopId, keyword);
        return list;
    }

}
