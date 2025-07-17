package com.tea.server.service;

import com.tea.server.entity.ShopToGoods;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 店家与商品对应关系表 服务类
 * </p>
 *
 * @author zyy
 * @since 2023-08-24
 */
public interface ShopToGoodsService extends IService<ShopToGoods> {

    // 查询当前店铺全部商品
    List<ShopToGoods> goodsAll(Long shopId);

}
