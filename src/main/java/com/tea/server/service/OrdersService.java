package com.tea.server.service;

import com.tea.server.entity.Orders;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tea.server.entity.vo.GoodsInfoAndParam;
import com.tea.server.entity.vo.OrdersInfo;
import com.tea.server.entity.vo.ParamOrders;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 订单记录表 服务类
 * </p>
 *
 * @author testjava
 * @since 2023-08-02
 */
public interface OrdersService extends IService<Orders> {

    // 新增订单
    Orders insertOrders(List<ParamOrders> params);

    // 订单详情
    OrdersInfo getOrdersInfo(Long orderId);

    // 订单列表
    List<OrdersInfo> getOrdersList(String openid, Integer type);
}
