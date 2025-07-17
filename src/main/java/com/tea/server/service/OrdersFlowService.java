package com.tea.server.service;

import com.tea.server.entity.OrdersFlow;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tea.server.entity.excel.FlowData;
import com.tea.server.entity.query.AccountQuery;

import java.util.List;

/**
 * <p>
 * 订单流水表 服务类
 * </p>
 *
 * @author zyy
 * @since 2024-04-15
 */
public interface OrdersFlowService extends IService<OrdersFlow> {

    // 商户账单导出
    List<FlowData> excelFlows(Long shopId, Integer type, String begin, String end);
}
