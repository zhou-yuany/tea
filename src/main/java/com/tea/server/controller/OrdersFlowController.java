package com.tea.server.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tea.server.entity.Agents;
import com.tea.server.entity.OrdersFlow;
import com.tea.server.entity.Shop;
import com.tea.server.service.AgentsService;
import com.tea.server.service.OrdersFlowService;
import com.tea.server.service.ShopService;
import com.tea.server.utils.R;
import org.aspectj.weaver.loadtime.Agent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 * 订单流水表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2024-04-15
 */
@RestController
@RequestMapping("ordersFlow")
public class OrdersFlowController {

    @Autowired
    private OrdersFlowService ordersFlowService;

    @Autowired
    private ShopService shopService;

    @Autowired
    private AgentsService agentsService;


    // 获取绑定的所有商户
    @GetMapping("getShopList/{flagId}")
    public R getShopList(@PathVariable("flagId") Long flagId){
        Agents agents = agentsService.getById(flagId);
        String shopId = agents.getShopId();
        List<Long> ids = Arrays.asList(shopId.split(",")).stream().map(Long::valueOf).collect(Collectors.toList());

        LambdaQueryWrapper<Shop> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Shop :: getStatus, 1).in(Shop :: getId, ids);
        List<Shop> list = shopService.list(wrapper);

        return R.ok().data("list", list);
    }

    // 获取订单流水列表
    @GetMapping("getList/{page}/{limit}")
    public R getList(@PathVariable("page") long page, @PathVariable("limit") long limit, @RequestParam("flagId") Long flagId, @RequestParam("type") Integer type, @RequestParam(value = "shopId", required = false) Long shopId) {
        LambdaQueryWrapper<OrdersFlow> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrdersFlow :: getStatus, 1);
        if (type == 1){ // 商户
            wrapper.eq(OrdersFlow :: getShopId, flagId);
        }
        if (type == 2){ // 代理
            wrapper.eq(OrdersFlow :: getAgentId, flagId);
        }
        if (shopId > 0){
            wrapper.eq(OrdersFlow :: getShopId, shopId);
        }
        wrapper.orderByDesc(OrdersFlow :: getId);
        List<OrdersFlow> list = ordersFlowService.list(wrapper);

        if (null != list && list.size() > 0){
            list.stream().forEach(item -> {
                Shop shop = shopService.getById(item.getShopId());
                item.setShopName(shop.getName());
            });
        }
        BigDecimal sumPrice = Optional.ofNullable(list)
                .orElse(new ArrayList<>())
                .stream()
                .filter(x-> x.getDistributionPrice() != null) // 避免空指针
                .map(OrdersFlow::getDistributionPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal sumPrice2 = Optional.ofNullable(list)
                .orElse(new ArrayList<>())
                .stream()
                .filter(x-> x.getShopDistributionPrice() != null) // 避免空指针
                .map(OrdersFlow::getShopDistributionPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);


        int listSize = list.size();
        int startCount = Integer.parseInt((page - 1) * limit + "");
        int endCount = Integer.parseInt(page * limit + "");
        return R.ok().data("total", list.size()).data("rows", list.subList(startCount, listSize > endCount ? endCount : listSize)).data("sumPrice",sumPrice).data("sumPrice2", sumPrice2);
    }

}

