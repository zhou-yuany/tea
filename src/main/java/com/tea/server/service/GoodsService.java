package com.tea.server.service;

import com.tea.server.entity.Categorize;
import com.tea.server.entity.Goods;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tea.server.entity.vo.GoodsInfoAndParam;

import java.util.List;

/**
 * <p>
 * 商品表 服务类
 * </p>
 *
 * @author testjava
 * @since 2023-08-02
 */
public interface GoodsService extends IService<Goods> {

    // 获取店家商品的所有分类
    List<Categorize> getCates(Long shopId);

    // 获取分类下的商品列表
    List<Goods> getGoods(Long shopId, Long cateId);

    // 获取商品详情及规格
    GoodsInfoAndParam getGoodsInfoAndParam(Long cateId, Long goodsId);

    // 获取商家全部商品列表
    List<Goods> getGoodsList(Long shopId);
}
