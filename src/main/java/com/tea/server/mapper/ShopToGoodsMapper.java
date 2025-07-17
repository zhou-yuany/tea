package com.tea.server.mapper;

import com.tea.server.entity.ShopToGoods;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 店家与商品对应关系表 Mapper 接口
 * </p>
 *
 * @author zyy
 * @since 2023-08-24
 */
public interface ShopToGoodsMapper extends BaseMapper<ShopToGoods> {

    List<ShopToGoods> goodsAll(@Param("shopId") Long shopId);
}
