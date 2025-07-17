package com.tea.server.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tea.server.config.WeChatProperties;
import com.tea.server.entity.Categorize;
import com.tea.server.entity.Goods;
import com.tea.server.entity.ShopToCate;
import com.tea.server.entity.ShopToGoods;
import com.tea.server.entity.vo.GoodsInfoAndParam;
import com.tea.server.service.CategorizeService;
import com.tea.server.service.GoodsService;
import com.tea.server.service.ShopToGoodsService;
import com.tea.server.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 商品表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2023-08-02
 */
@RestController
@RequestMapping("goods")
public class GoodsController {

    @Autowired
    private WeChatProperties wechatProperties;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private ShopToGoodsService shopToGoodsService;

    @Autowired
    private CategorizeService categorizeService;

    // 获取店家的商品分类
    @GetMapping("getCates")
    public R getCates(@RequestParam("shopId") Long shopId){

        List<Categorize> list = goodsService.getCates(shopId);
        return R.ok().data("list", list);
    }

    // 获取分类下的商品列表
    @GetMapping("getGoods")
    public R getGoods(@RequestParam("shopId") Long shopId, @RequestParam("cateId") Long cateId){
        List<Goods> list = goodsService.getGoods(shopId, cateId);
        return R.ok().data("list", list);
    }

    // 获取商家全部商品列表
    @GetMapping("getGoodsList")
    public R getGoodsList(@RequestParam("shopId") Long shopId){
        List<Goods> list = goodsService.getGoodsList(shopId);
        return R.ok().data("list", list);
    }

    // 获取商品详情及规格
    @GetMapping("getGoodsInfoAndParam")
    public R getGoodsInfoAndParam(@RequestParam("shopId") Long shopId, @RequestParam("cateId") Long cateId, @RequestParam("goodsId") Long goodsId){
        LambdaQueryWrapper<ShopToGoods> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShopToGoods :: getStatus, 1).eq(ShopToGoods :: getShopId, shopId).eq(ShopToGoods :: getGoodsId, goodsId);
        ShopToGoods shopToGoods = shopToGoodsService.getOne(queryWrapper);
        if (shopToGoods.getIsGrounding() == 1){
            GoodsInfoAndParam info = goodsService.getGoodsInfoAndParam(cateId, goodsId);
            return R.ok().data("info", info);
        }else {
            return R.error();
        }


    }

}

