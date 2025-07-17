package com.tea.server.service.impl;

import com.tea.server.entity.OrdersFlow;
import com.tea.server.entity.excel.FlowData;
import com.tea.server.entity.query.AccountQuery;
import com.tea.server.mapper.OrdersFlowMapper;
import com.tea.server.service.OrdersFlowService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 订单流水表 服务实现类
 * </p>
 *
 * @author zyy
 * @since 2024-04-15
 */
@Service
public class OrdersFlowServiceImpl extends ServiceImpl<OrdersFlowMapper, OrdersFlow> implements OrdersFlowService {

    @Override
    public List<FlowData> excelFlows(Long shopId, Integer type, String begin, String end) {
        List<FlowData> list = baseMapper.excelFlows(shopId, type, begin, end);
        return list;
    }
}
