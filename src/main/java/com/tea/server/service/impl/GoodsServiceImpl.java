package com.tea.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tea.server.entity.CateToParamCate;
import com.tea.server.entity.Goods;
import com.tea.server.entity.Param;
import com.tea.server.entity.ParamCate;
import com.tea.server.entity.vo.ParamCateAndParam;
import com.tea.server.mapper.CateToParamCateMapper;
import com.tea.server.mapper.GoodsMapper;
import com.tea.server.mapper.ParamCateMapper;
import com.tea.server.mapper.ParamMapper;
import com.tea.server.service.GoodsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 商品表 服务实现类
 * </p>
 *
 * @author zyy
 * @since 2023-08-24
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {

    @Resource
    private CateToParamCateMapper cateToParamCateMapper;

    @Resource
    private ParamCateMapper paramCateMapper;

    @Resource
    private ParamMapper paramMapper;

    @Override
    public ParamCateAndParam getParamsByGoodsId(Long goodsId) {
        ParamCateAndParam paramCateAndParam = new ParamCateAndParam();
        Goods goods = baseMapper.selectById(goodsId);
        LambdaQueryWrapper<CateToParamCate> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CateToParamCate :: getStatus, 1).eq(CateToParamCate :: getCateAllId, goods.getCateId());
        List<CateToParamCate> cateToParamCates = cateToParamCateMapper.selectList(wrapper);
        List<Long> paramCateIds = cateToParamCates.stream().map(item -> item.getParamCateId()).collect(Collectors.toList());

        LambdaQueryWrapper<ParamCate> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ParamCate :: getStatus, 1).in(ParamCate :: getId, paramCateIds);
        List<ParamCate> paramCates = paramCateMapper.selectList(queryWrapper);

        List<ParamCate> paramCateList = new ArrayList<>();
        paramCates.stream().forEach(paramCate -> {
            ParamCate paramCate1 = new ParamCate();
            BeanUtils.copyProperties(paramCate, paramCate1);
            LambdaQueryWrapper<Param> paramWrapper = new LambdaQueryWrapper<>();
            paramWrapper.eq(Param :: getStatus, 1).in(Param :: getParamCateId, paramCate.getId());
            List<Param> paramList = paramMapper.selectList(paramWrapper);
            paramCate1.setParamList(paramList);
            paramCateList.add(paramCate1);
        });
        paramCateAndParam.setIntroduce(goods.getIntroduce());
        paramCateAndParam.setName(goods.getName());
        paramCateAndParam.setPrice(goods.getPrice());
        paramCateAndParam.setUrl(goods.getUrl());
        paramCateAndParam.setParamCateList(paramCateList);
        return paramCateAndParam;
    }

}
