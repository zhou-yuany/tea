package com.tea.server.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tea.server.entity.*;
import com.tea.server.entity.query.AccountQuery;
import com.tea.server.entity.query.WithdrawalQuery;
import com.tea.server.service.InterfaceLogService;
import com.tea.server.service.ShopsService;
import com.tea.server.service.WithdrawalRecordService;
import com.tea.server.utils.R;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 * 提现表 前端控制器
 * </p>
 *
 * @author zyy
 * @since 2024-04-30
 */
@RestController
@RequestMapping("withdrawal")
public class WithdrawalRecordController {

    @Autowired
    private WithdrawalRecordService withdrawalRecordService;

    @Autowired
    private ShopsService shopsService;

    @Autowired
    private InterfaceLogService interfaceLogService;

    /**
     * 商户提现列表
     */
    @PostMapping("getListShop/{page}/{limit}/{shopId}")
    public R getListShop(@PathVariable long page, @PathVariable long limit, @PathVariable Long shopId, @RequestBody WithdrawalQuery withdrawalQuery){

        // 创建page对象
        Page<WithdrawalRecord> recordPage = new Page<>(page, limit);
        // 构建条件
        LambdaQueryWrapper<WithdrawalRecord> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(WithdrawalRecord :: getStatus, 1).eq(WithdrawalRecord :: getShopId, shopId);;

        String begin = withdrawalQuery.getBegin();
        String end = withdrawalQuery.getEnd();
        Integer type = withdrawalQuery.getType();

        if (null != type && type > 0){
            wrapper.eq(WithdrawalRecord :: getType, type);
        }
        if (null != begin && !begin.equals("")){
            wrapper.ge(WithdrawalRecord :: getCreateTime, begin);
        }
        if (null != end && !end.equals("")){
            wrapper.le(WithdrawalRecord :: getCreateTime, end);
        }

        wrapper.orderByDesc(WithdrawalRecord :: getId);
        withdrawalRecordService.page(recordPage, wrapper);
        long total = recordPage.getTotal();
        List<WithdrawalRecord> records = recordPage.getRecords();

        return R.ok().data("total", total).data("rows", records);
    }


    /**
     * 总平台提现列表
     */
    @PostMapping("getList/{page}/{limit}")
    public R getList(@PathVariable long page, @PathVariable long limit, @RequestBody WithdrawalQuery withdrawalQuery){

        // 创建page对象
        Page<WithdrawalRecord> recordPage = new Page<>(page, limit);
        // 构建条件
        LambdaQueryWrapper<WithdrawalRecord> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(WithdrawalRecord :: getStatus, 1);

        String begin = withdrawalQuery.getBegin();
        String end = withdrawalQuery.getEnd();
        Integer type = withdrawalQuery.getType();

        if (null != type && type > 0){
            wrapper.eq(WithdrawalRecord :: getType, type);
        }
        if (null != begin && !begin.equals("")){
            wrapper.ge(WithdrawalRecord :: getCreateTime, begin);
        }
        if (null != end && !end.equals("")){
            wrapper.le(WithdrawalRecord :: getCreateTime, end);
        }

        wrapper.orderByDesc(WithdrawalRecord :: getId);
        withdrawalRecordService.page(recordPage, wrapper);
        long total = recordPage.getTotal();
        List<WithdrawalRecord> records = recordPage.getRecords();

        return R.ok().data("total", total).data("rows", records);
    }


    /**
     * 总平台提现审核 提现
     */
    @PostMapping("toWithdrawal/{id}")
    public R toWithdrawal(@PathVariable Long id){
        WithdrawalRecord withdrawalRecord = withdrawalRecordService.getById(id);
        WithdrawalRecord record = new WithdrawalRecord();
        BeanUtils.copyProperties(withdrawalRecord, record);
        record.setType(2);
        record.setUpdateTime(LocalDateTime.now());
        withdrawalRecordService.updateById(record);

        Shop shop = shopsService.getById(withdrawalRecord.getShopId());

        InterfaceLog interfaceLog = new InterfaceLog();
        interfaceLog.setTitle("提现申请平台已审核通过");
        interfaceLog.setMethodName("toWithdrawal");
        String content = "商户"+shop.getName()+"提现申请平台已审核通过，"+"提现金额为："+withdrawalRecord.getPrice()+"元";
        interfaceLog.setContent(content);
        interfaceLog.setShopId(withdrawalRecord.getShopId());
        interfaceLog.setTypeStatus(0);
        interfaceLog.setCreateTime(LocalDateTime.now());
        interfaceLog.setUpdateTime(LocalDateTime.now());
        interfaceLogService.save(interfaceLog);

        return R.ok();
    }

    /**
     * 总平台提现审核  驳回
     */
    @PostMapping("unWithdrawalShop/{id}")
    public R unWithdrawalShop(@PathVariable Long id){
        WithdrawalRecord withdrawalRecord = withdrawalRecordService.getById(id);
        WithdrawalRecord record = new WithdrawalRecord();
        BeanUtils.copyProperties(withdrawalRecord, record);
        record.setType(3);
        record.setUpdateTime(LocalDateTime.now());
        withdrawalRecordService.updateById(record);

        Shop shop = shopsService.getById(withdrawalRecord.getShopId());

        InterfaceLog interfaceLog = new InterfaceLog();
        interfaceLog.setTitle("提现申请平台已驳回");
        interfaceLog.setMethodName("unWithdrawalShop");
        String content = "商户"+shop.getName()+"提现申请平台已驳回";
        interfaceLog.setContent(content);
        interfaceLog.setShopId(withdrawalRecord.getShopId());
        interfaceLog.setTypeStatus(0);
        interfaceLog.setCreateTime(LocalDateTime.now());
        interfaceLog.setUpdateTime(LocalDateTime.now());
        interfaceLogService.save(interfaceLog);

        return R.ok();
    }


}

