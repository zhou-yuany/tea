package com.tea.server.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tea.server.entity.Orders;
import com.tea.server.entity.RechargeRecord;
import com.tea.server.entity.Shop;
import com.tea.server.service.RechargeRecordService;
import com.tea.server.service.ShopsService;
import com.tea.server.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 商户充值记录 前端控制器
 * </p>
 *
 * @author zyy
 * @since 2024-04-29
 */
@RestController
@RequestMapping("rechargeRecord")
public class RechargeRecordController {

    @Autowired
    private RechargeRecordService rechargeRecordService;

    @Autowired
    private ShopsService shopsService;


    // 商品列表
    @PostMapping("getRechargeRecordList/{page}/{limit}/{shopId}")
    public R getRechargeRecordList(@PathVariable long page, @PathVariable long limit, @PathVariable Long shopId, @RequestBody RechargeRecord rechargeQuery){
        // 创建page对象
        Page<RechargeRecord> recordPage = new Page<>(page, limit);

        // 构建条件
        LambdaQueryWrapper<RechargeRecord> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(RechargeRecord :: getStatus, 1).eq(RechargeRecord :: getShopId, shopId).eq(RechargeRecord :: getPayStatus, 1);

        String orderNo = rechargeQuery.getOrderNo();
        Integer type = rechargeQuery.getType();

        if (null != type && type > 0){
            wrapper.eq(RechargeRecord :: getType, type);
        }
        if (null != orderNo && !orderNo.equals("")){
            wrapper.like(RechargeRecord :: getOrderNo, orderNo);
        }
        wrapper.orderByDesc(RechargeRecord :: getId);

        rechargeRecordService.page(recordPage, wrapper);


        long total = recordPage.getTotal();
        List<RechargeRecord> records = recordPage.getRecords();

        Shop shop = shopsService.getById(shopId);
        BigDecimal balance = shop.getBalance();


        return R.ok().data("total", total).data("rows", records).data("balance", balance);
    }




}

