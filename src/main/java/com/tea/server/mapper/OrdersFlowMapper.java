package com.tea.server.mapper;

import com.tea.server.entity.OrdersFlow;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tea.server.entity.excel.FlowData;
import com.tea.server.entity.query.AccountQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 订单流水表 Mapper 接口
 * </p>
 *
 * @author zyy
 * @since 2024-04-15
 */
public interface OrdersFlowMapper extends BaseMapper<OrdersFlow> {

    List<FlowData> excelFlows(@Param("shopId") Long shopId, @Param("type") Integer type, @Param("begin") String begin, @Param("end") String end);
}
