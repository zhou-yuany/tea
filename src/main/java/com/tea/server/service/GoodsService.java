package com.tea.server.service;

import com.tea.server.entity.Goods;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tea.server.entity.vo.ParamCateAndParam;

/**
 * <p>
 * 商品表 服务类
 * </p>
 *
 * @author zyy
 * @since 2023-08-24
 */
public interface GoodsService extends IService<Goods> {

    // 根据选择商品查询规格
    ParamCateAndParam getParamsByGoodsId(Long goodsId);

}
