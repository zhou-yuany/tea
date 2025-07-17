package com.tea.server.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tea.server.entity.Orders;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tea.server.entity.query.OrderQuery;
import com.tea.server.entity.vo.OrdersAndParam;
import com.tea.server.entity.vo.OrdersAndShopParam;
import com.tea.server.entity.vo.OrdersCall;
import com.tea.server.entity.vo.OutletVo;

import java.util.List;

/**
 * <p>
 * 订单记录表 服务类
 * </p>
 *
 * @author zyy
 * @since 2023-08-24
 */
public interface OrdersService extends IService<Orders> {

    // // 新增订单
    // Orders insertOrders(List<OrdersAndParam> params, String orderNo, Integer type, Long shopId, Long adminId);
    // 新增订单
    Orders insertOrders(List<OrdersAndParam> params, String orderNo, Integer type, Long adminId);

    // 叫号取餐列表 分页
    Page<OrdersCall> callOrders(Page<OrdersCall> ordersPage, Long shopId);

    // // 茶饮机器选择商品直接制作
    Orders startOrders(OrdersAndShopParam params, String orderNo, Integer type, Long adminId);

    // 根据订单查询出料参数
    List<OutletVo> getOutletList(Long id, Long goodsId);

    // 总后台订单 分页
    Page<Orders> pageAllOrders(Page<Orders> ordersPage, OrderQuery orderQuery);
}
