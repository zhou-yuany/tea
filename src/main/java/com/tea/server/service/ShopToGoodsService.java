package com.tea.server.service;

import com.tea.server.entity.ShopToGoods;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tea.server.entity.vo.GoodsInfoAndParam;

import java.util.List;

/**
 * <p>
 * 店家与商品对应关系表 服务类
 * </p>
 *
 * @author testjava
 * @since 2023-08-02
 */
public interface ShopToGoodsService extends IService<ShopToGoods> {

    // 获取分类下的商品列表
    List<ShopToGoods> getGoods(Long shopId, Long cateId);

    // 获取商家全部商品列表
    List<ShopToGoods> getGoodsList(Long shopId);

    // 获取商品详情及规格
    GoodsInfoAndParam getGoodsInfoAndParam(Long shopId, Long goodsId);
}
