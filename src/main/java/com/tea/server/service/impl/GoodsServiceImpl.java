package com.tea.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tea.server.core.BaseBean;
import com.tea.server.entity.*;
import com.tea.server.entity.vo.GoodsInfoAndParam;
import com.tea.server.mapper.*;
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
 * @author testjava
 * @since 2023-08-02
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {

    @Resource
    private ShopToCateMapper shopToCateMapper;

    @Resource
    private CategorizeMapper categorizeMapper;

    @Resource
    private ShopToGoodsMapper shopToGoodsMapper;

    @Resource
    private ParamCateMapper paramCateMapper;

    @Resource
    private ParamMapper paramMapper;

    @Resource
    private CateToParamCateMapper cateToParamCateMapper;



    @Override
    public List<Categorize> getCates(Long shopId) {
        // LambdaQueryWrapper<ShopToCate> queryWrapper = new LambdaQueryWrapper<>();
        // queryWrapper.eq(ShopToCate :: getShopId, shopId).eq(ShopToCate :: getStatus, 1);
        // List<ShopToCate> cates = shopToCateMapper.selectList(queryWrapper);
        // List<Long> cateIds = cates.stream().map(item -> item.getCateId()).collect(Collectors.toList());
        // List<Categorize> categorizes = new ArrayList<>();
        // if (cateIds.size() > 0) {
        //     LambdaQueryWrapper<Categorize> wrapper = new LambdaQueryWrapper<>();
        //     wrapper.eq(Categorize :: getStatus, 1).in(Categorize :: getId, cateIds);
        //     categorizes = categorizeMapper.selectList(wrapper);
        // }

            LambdaQueryWrapper<Categorize> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Categorize :: getStatus, 1).eq(Categorize :: getShopId, shopId);
        List<Categorize> categorizes = categorizeMapper.selectList(wrapper);


        return categorizes;
    }

    @Override
    public List<Goods> getGoods(Long shopId, Long cateId) {
        LambdaQueryWrapper<ShopToGoods> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShopToGoods :: getShopId, shopId).eq(ShopToGoods :: getStatus, 1).eq(ShopToGoods :: getIsGrounding, 1);
        List<ShopToGoods> shopToGoods = shopToGoodsMapper.selectList(queryWrapper);
        List<Long> goodsId = shopToGoods.stream().map(item -> item.getGoodsId()).collect(Collectors.toList());
        List<Goods> goods = new ArrayList<>();
        if (goodsId.size() > 0) {
            LambdaQueryWrapper<Goods> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Goods :: getStatus, 1).in(Goods :: getCateId, cateId).in(Goods :: getId, goodsId);
            goods = baseMapper.selectList(wrapper);
        }
        return goods;
    }

    @Override
    public GoodsInfoAndParam getGoodsInfoAndParam(Long cateId, Long goodsId) {
        // Goods goods = baseMapper.selectById(goodsId);
        //
        GoodsInfoAndParam goodsInfoAndParam = new GoodsInfoAndParam();
        // goodsInfoAndParam.setName(goods.getName());
        // goodsInfoAndParam.setUrl(goods.getUrl());
        // goodsInfoAndParam.setIntroduce(goods.getIntroduce());
        // goodsInfoAndParam.setPrice(goods.getPrice());
        // goodsInfoAndParam.setTips(goods.getTips());
        //
        // LambdaQueryWrapper<CateToParamCate> queryWrapper = new LambdaQueryWrapper<>();
        // queryWrapper.eq(CateToParamCate :: getStatus, 1).eq(CateToParamCate :: getCateAllId, cateId);
        // List<CateToParamCate> cateToParamCates = cateToParamCateMapper.selectList(queryWrapper);
        // List<Long> paramCateIds = cateToParamCates.stream().map(item -> item.getParamCateId()).collect(Collectors.toList());
        //
        // LambdaQueryWrapper<ParamCate> wrapper = new LambdaQueryWrapper<>();
        // wrapper.eq(ParamCate :: getStatus, 1).in(ParamCate :: getId, paramCateIds);
        // List<ParamCate> paramCates = paramCateMapper.selectList(wrapper);
        //
        // List<ParamCate> paramCateList = new ArrayList<>();
        // paramCates.stream().forEach(item -> {
        //     ParamCate paramCate = new ParamCate();
        //     LambdaQueryWrapper<Param> paramWapper = new LambdaQueryWrapper<>();
        //     paramWapper.eq(Param :: getStatus, 1).eq(Param :: getParamCateId, item.getId());
        //     List<Param> params = paramMapper.selectList(paramWapper);
        //     BeanUtils.copyProperties(item, paramCate);
        //     paramCate.setParamList(params);
        //     paramCateList.add(paramCate);
        // });
        // goodsInfoAndParam.setParamCateList(paramCateList);
        return goodsInfoAndParam;
    }

    @Override
    public List<Goods> getGoodsList(Long shopId) {
        LambdaQueryWrapper<ShopToGoods> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShopToGoods :: getShopId, shopId).eq(ShopToGoods :: getStatus, 1).eq(ShopToGoods :: getIsGrounding, 1);
        List<ShopToGoods> shopToGoods = shopToGoodsMapper.selectList(queryWrapper);
        List<Long> goodsId = shopToGoods.stream().map(item -> item.getGoodsId()).collect(Collectors.toList());
        List<Goods> goods = new ArrayList<>();
        if (goodsId.size() > 0) {
            LambdaQueryWrapper<Goods> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Goods :: getStatus, 1).in(Goods :: getId, goodsId);
            goods = baseMapper.selectList(wrapper);
        }
        return goods;
    }
}
