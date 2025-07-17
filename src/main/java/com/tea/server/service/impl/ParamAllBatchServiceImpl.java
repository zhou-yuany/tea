package com.tea.server.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tea.server.entity.ParamAllBatch;
import com.tea.server.entity.ParamBatch;
import com.tea.server.entity.vo.ParamAllBatchVo;
import com.tea.server.mapper.ParamAllBatchMapper;
import com.tea.server.service.ParamAllBatchService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 * 总规格参数配方表 服务实现类
 * </p>
 *
 * @author zyy
 * @since 2024-04-03
 */
@Service
public class ParamAllBatchServiceImpl extends ServiceImpl<ParamAllBatchMapper, ParamAllBatch> implements ParamAllBatchService {

    @Override
    public Integer addParamAllBatch(ParamAllBatch paramAllBatch) {
        ParamAllBatch paramAllBatch1 = new ParamAllBatch();
        BeanUtils.copyProperties(paramAllBatch, paramAllBatch1);
        paramAllBatch1.setCreateTime(LocalDateTime.now());
        paramAllBatch1.setUpdateTime(LocalDateTime.now());
        paramAllBatch1.setHeatUnit("g");
        paramAllBatch1.setSugarUnit("g");
        int insert = baseMapper.insert(paramAllBatch1);
        return insert;
    }

    @Override
    public Integer updateParamBatch(ParamAllBatch paramAllBatch) {
        ParamAllBatch paramAllBatch1 = new ParamAllBatch();
        BeanUtils.copyProperties(paramAllBatch, paramAllBatch1);
        paramAllBatch1.setUpdateTime(LocalDateTime.now());
        int i = baseMapper.updateById(paramAllBatch1);
        return i;
    }

    @Override
    public Page<ParamAllBatchVo> pageList(Page<ParamAllBatchVo> paramBatchVoPage, String keyword) {
        Page<ParamAllBatchVo> list = baseMapper.pageList(paramBatchVoPage, keyword);
        return list;
    }
}
