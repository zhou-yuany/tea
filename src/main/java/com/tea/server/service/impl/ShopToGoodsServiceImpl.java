package com.tea.server.service.impl;

import com.tea.server.entity.ShopToGoods;
import com.tea.server.mapper.ShopToGoodsMapper;
import com.tea.server.service.ShopToGoodsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 店家与商品对应关系表 服务实现类
 * </p>
 *
 * @author zyy
 * @since 2023-08-24
 */
@Service
public class ShopToGoodsServiceImpl extends ServiceImpl<ShopToGoodsMapper, ShopToGoods> implements ShopToGoodsService {

    @Override
    public List<ShopToGoods> goodsAll(Long shopId) {
        List<ShopToGoods> list = baseMapper.goodsAll(shopId);
        return list;
    }

}
