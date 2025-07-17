package com.tea.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tea.server.entity.*;
import com.tea.server.entity.vo.GoodsInfoAndParam;
import com.tea.server.mapper.*;
import com.tea.server.service.ShopToGoodsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 店家与商品对应关系表 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2023-08-02
 */
@Service
public class ShopToGoodsServiceImpl extends ServiceImpl<ShopToGoodsMapper, ShopToGoods> implements ShopToGoodsService {

    @Resource
    private GoodsToBatchMapper goodsToBatchMapper;

    @Resource
    private ShopToParamMapper shopToParamMapper;

    @Resource
    private ParamMapper paramMapper;


    @Override
    public List<ShopToGoods> getGoods(Long shopId, Long cateId) {
        LambdaQueryWrapper<ShopToGoods> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShopToGoods::getShopId, shopId).eq(ShopToGoods::getCateId, cateId).eq(ShopToGoods::getStatus, 1).eq(ShopToGoods::getIsGrounding, 1);
        List<ShopToGoods> shopToGoods = baseMapper.selectList(queryWrapper);
        return shopToGoods;
    }

    @Override
    public List<ShopToGoods> getGoodsList(Long shopId) {
        LambdaQueryWrapper<ShopToGoods> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShopToGoods::getShopId, shopId).eq(ShopToGoods::getStatus, 1).eq(ShopToGoods::getIsGrounding, 1);
        List<ShopToGoods> shopToGoods = baseMapper.selectList(queryWrapper);
        return shopToGoods;
    }

    // @Override
    // public GoodsInfoAndParam getGoodsInfoAndParam(Long shopId, Long goodsId) {
    //     ShopToGoods shopToGoods = baseMapper.selectById(goodsId);
    //
    //     GoodsInfoAndParam goodsInfoAndParam = new GoodsInfoAndParam();
    //     goodsInfoAndParam.setName(shopToGoods.getName());
    //     goodsInfoAndParam.setUrl(shopToGoods.getUrl());
    //     goodsInfoAndParam.setIntroduce(shopToGoods.getIntroduce());
    //     goodsInfoAndParam.setPrice(shopToGoods.getPrice());
    //     goodsInfoAndParam.setTips(shopToGoods.getTips());
    //
    //     List<ParamCate> paramCateList = new ArrayList<>();
    //
    //     LambdaQueryWrapper<GoodsToBatch> wrapper = new LambdaQueryWrapper<>();
    //     wrapper.eq(GoodsToBatch::getShopId, shopId).eq(GoodsToBatch::getStatus, 1).eq(GoodsToBatch::getGoodsId, goodsId);
    //     List<GoodsToBatch> goodsToBatchList = goodsToBatchMapper.selectList(wrapper);
    //     if (null != goodsToBatchList && goodsToBatchList.size() > 0) {
    //         List<Long> sizeIds = goodsToBatchList.stream().map(item -> item.getSizeId()).collect(Collectors.toList());
    //         Set<Long> setSizeIds = new HashSet<>(sizeIds);
    //         List<Long> sizeIdList = new ArrayList<>(setSizeIds);
    //
    //         LambdaQueryWrapper<ShopToParam> wrapper2 = new LambdaQueryWrapper<>();
    //         wrapper2.eq(ShopToParam::getStatus, 1).eq(ShopToParam::getShopId, shopId).in(ShopToParam::getParamId, sizeIdList);
    //         List<ShopToParam> shopToParams = shopToParamMapper.selectList(wrapper2);
    //         if (null != shopToParams && shopToParams.size() > 0) {
    //             ParamCate paramCate = new ParamCate();
    //             paramCate.setName("规格");
    //             paramCate.setParamList(shopToParams);
    //             paramCateList.add(paramCate);
    //         }
    //
    //
    //         List<Long> sugarIds = goodsToBatchList.stream().map(item -> item.getSugarId()).collect(Collectors.toList());
    //         Set<Long> setSugarIds = new HashSet<>(sugarIds);
    //         List<Long> sugarIdList = new ArrayList<>(setSugarIds);
    //         LambdaQueryWrapper<ShopToParam> wrapper3 = new LambdaQueryWrapper<>();
    //         wrapper3.eq(ShopToParam::getStatus, 1).eq(ShopToParam::getShopId, shopId).in(ShopToParam::getParamId, sugarIdList);
    //         List<ShopToParam> shopToParams2 = shopToParamMapper.selectList(wrapper3);
    //         if (null != shopToParams2 && shopToParams2.size() > 0) {
    //             ParamCate paramCate = new ParamCate();
    //             paramCate.setName("糖度");
    //             paramCate.setParamList(shopToParams2);
    //             paramCateList.add(paramCate);
    //         }
    //
    //         List<Long> temperatureIds = goodsToBatchList.stream().map(item -> item.getTemperatureId()).collect(Collectors.toList());
    //         Set<Long> setTemperatureIds = new HashSet<>(temperatureIds);
    //         List<Long> temperatureIdList = new ArrayList<>(setTemperatureIds);
    //         LambdaQueryWrapper<ShopToParam> wrapper4 = new LambdaQueryWrapper<>();
    //         wrapper4.eq(ShopToParam::getStatus, 1).eq(ShopToParam::getShopId, shopId).in(ShopToParam::getParamId, temperatureIdList);
    //         List<ShopToParam> shopToParams3 = shopToParamMapper.selectList(wrapper4);
    //         if (null != shopToParams3 && shopToParams3.size() > 0) {
    //             ParamCate paramCate = new ParamCate();
    //             paramCate.setName("温度");
    //             paramCate.setParamList(shopToParams3);
    //             paramCateList.add(paramCate);
    //         }
    //
    //     }
    //     goodsInfoAndParam.setParamCateList(paramCateList);
    //     return goodsInfoAndParam;
    // }

    @Override
    public GoodsInfoAndParam getGoodsInfoAndParam(Long shopId, Long goodsId) {
        ShopToGoods shopToGoods = baseMapper.selectById(goodsId);

        GoodsInfoAndParam goodsInfoAndParam = new GoodsInfoAndParam();
        goodsInfoAndParam.setName(shopToGoods.getName());
        goodsInfoAndParam.setUrl(shopToGoods.getUrl());
        goodsInfoAndParam.setIntroduce(shopToGoods.getIntroduce());
        goodsInfoAndParam.setPrice(shopToGoods.getPrice());
        goodsInfoAndParam.setTips(shopToGoods.getTips());


        List<ParamCate> paramCateList = new ArrayList<>();

        LambdaQueryWrapper<GoodsToBatch> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GoodsToBatch::getShopId, shopId).eq(GoodsToBatch::getStatus, 1).eq(GoodsToBatch::getGoodsId, goodsId);
        List<GoodsToBatch> goodsToBatchList = goodsToBatchMapper.selectList(wrapper);
        if (null != goodsToBatchList && goodsToBatchList.size() > 0) {
            List<Long> sizeIds = goodsToBatchList.stream().map(item -> item.getSizeId()).collect(Collectors.toList());
            Set<Long> setSizeIds = new HashSet<>(sizeIds);
            List<Long> sizeIdList = new ArrayList<>(setSizeIds);

            LambdaQueryWrapper<Param> wrapper2 = new LambdaQueryWrapper<>();
            wrapper2.eq(Param::getStatus, 1).in(Param::getId, sizeIdList);
            List<Param> params = paramMapper.selectList(wrapper2);
            if (null != params && params.size() > 0) {
                ParamCate paramCate = new ParamCate();
                paramCate.setName("规格");
                paramCate.setParamList(params);
                paramCateList.add(paramCate);
            }


            List<Long> sugarIds = goodsToBatchList.stream().map(item -> item.getSugarId()).collect(Collectors.toList());
            Set<Long> setSugarIds = new HashSet<>(sugarIds);
            List<Long> sugarIdList = new ArrayList<>(setSugarIds);
            LambdaQueryWrapper<Param> wrapper3 = new LambdaQueryWrapper<>();
            wrapper3.eq(Param::getStatus, 1).in(Param::getId, sugarIdList);
            List<Param> params2 = paramMapper.selectList(wrapper3);
            if (null != params2 && params2.size() > 0) {
                ParamCate paramCate = new ParamCate();
                paramCate.setName("糖度");
                paramCate.setParamList(params2);
                paramCateList.add(paramCate);
            }

            List<Long> temperatureIds = goodsToBatchList.stream().map(item -> item.getTemperatureId()).collect(Collectors.toList());
            Set<Long> setTemperatureIds = new HashSet<>(temperatureIds);
            List<Long> temperatureIdList = new ArrayList<>(setTemperatureIds);
            LambdaQueryWrapper<Param> wrapper4 = new LambdaQueryWrapper<>();
            wrapper4.eq(Param::getStatus, 1).in(Param::getId, temperatureIdList);
            List<Param> params3 = paramMapper.selectList(wrapper4);
            if (null != params3 && params3.size() > 0) {
                ParamCate paramCate = new ParamCate();
                paramCate.setName("温度");
                paramCate.setParamList(params3);
                paramCateList.add(paramCate);
            }

        }
        goodsInfoAndParam.setParamCateList(paramCateList);
        return goodsInfoAndParam;
    }
}
