package com.tea.server.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tea.server.entity.Orders;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tea.server.entity.query.OrderQuery;
import com.tea.server.entity.vo.OrdersCall;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 订单记录表 Mapper 接口
 * </p>
 *
 * @author zyy
 * @since 2023-08-24
 */
public interface OrdersMapper extends BaseMapper<Orders> {
    Page<OrdersCall> callOrders(@Param("ordersPage") Page<OrdersCall> ordersPage, @Param("shopId") Long shopId);

    Page<Orders> pageAllOrders(@Param("ordersPage") Page<Orders> ordersPage, @Param("orderQuery") OrderQuery orderQuery);
}
