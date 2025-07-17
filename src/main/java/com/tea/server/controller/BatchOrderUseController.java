package com.tea.server.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tea.server.entity.BatchOrderUse;
import com.tea.server.entity.BatchUse;
import com.tea.server.entity.Batching;
import com.tea.server.entity.Shop;
import com.tea.server.entity.query.BatchUseQuery;
import com.tea.server.entity.vo.GoodsBatchList;
import com.tea.server.service.BatchOrderUseService;
import com.tea.server.service.BatchingService;
import com.tea.server.service.ShopsService;
import com.tea.server.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 物料消耗-订单表 前端控制器
 * </p>
 *
 * @author zyy
 * @since 2024-05-24
 */
@RestController
@RequestMapping("batchOrderUse")
public class BatchOrderUseController {

    @Autowired
    private BatchOrderUseService batchOrderUseService;

    @Autowired
    private ShopsService shopService;

    @Autowired
    private BatchingService batchingService;

    /**
     * 商户竖屏 小屏 物料消耗
     */
    @PostMapping("shopBatchUseList/{adminId}")
    public R shopBatchUseList(@PathVariable Long adminId, @RequestBody BatchUseQuery batchUseQuery){

        // LambdaQueryWrapper<Shop> shopWrapper = new LambdaQueryWrapper<>();
        // shopWrapper.eq(Shop :: getStatus, 1).eq(Shop :: getAdminId, adminId);
        // Shop shop = shopService.getOne(shopWrapper);
        //
        // // 构建条件
        // LambdaQueryWrapper<BatchOrderUse> wrapper = new LambdaQueryWrapper<>();
        //
        // wrapper.eq(BatchOrderUse :: getStatus, 1).eq(BatchOrderUse :: getShopId, shop.getId());
        //
        // String name = batchUseQuery.getName();
        // if (null != name && !name.equals("")){
        //     LambdaQueryWrapper<Batching> batchWrapper = new LambdaQueryWrapper<>();
        //     batchWrapper.eq(Batching :: getStatus, 1).like(Batching :: getName, name);
        //     List<Batching> batchList = batchingService.list(batchWrapper);
        //     if (null != batchList && batchList.size() > 0){
        //         List<Long> ids = batchList.stream().map(item -> item.getId()).collect(Collectors.toList());
        //         wrapper.in(BatchOrderUse :: getBatchId, ids);
        //     }
        //
        //
        // }
        //
        // String orderNo = batchUseQuery.getOrderNum();
        // String begin = batchUseQuery.getBegin();
        // String end = batchUseQuery.getEnd();
        // if (null != orderNo && !orderNo.equals("")){
        //     wrapper.like(BatchOrderUse :: getOrderNum, orderNo);
        // }
        //
        // if (null != begin && !begin.equals("")){
        //     wrapper.ge(BatchOrderUse :: getCreateTime, begin);
        // }
        // if (null != end && !end.equals("")){
        //     wrapper.le(BatchOrderUse :: getCreateTime, end);
        // }
        //
        // wrapper.orderByDesc(BatchOrderUse :: getId);
        List<BatchOrderUse> list = batchOrderUseService.shopBatchUseList(adminId, batchUseQuery);
        List<BatchOrderUse> newList = new ArrayList<>();
        int totalUse = 0;
        if (null != list && list.size() > 0){
            newList = list.stream().filter(item -> item.getCreateTime().isAfter(LocalDateTime.now().minusMonths(3))).collect(Collectors.toList());
        }
        if (null != newList && newList.size() > 0){
            totalUse = newList.stream().map(item -> item.getUseCount()).mapToInt(Integer::intValue).sum();
        }


        return R.ok().data("list", newList).data("totalUse", totalUse);
    }

    /**
     * 总后台 查询所有商户 物料消耗 分页
     */
    @PostMapping("batchUseList/{page}/{limit}")
    public R batchUseList(@PathVariable long page, @PathVariable long limit,@RequestBody BatchUseQuery batchUseQuery){

        List<BatchOrderUse> list = batchOrderUseService.batchUseList(batchUseQuery);
        List<BatchOrderUse> newList = new ArrayList<>();
        int totalUse = 0;
        if (null != list && list.size() > 0){
            newList = list.stream().filter(item -> item.getCreateTime().isAfter(LocalDateTime.now().minusMonths(3))).collect(Collectors.toList());

            if (null != newList && newList.size() > 0){
                totalUse = newList.stream().map(item -> item.getUseCount()).mapToInt(Integer::intValue).sum();
            }
        }


        // 创建page对象
        Page<BatchOrderUse> batchListPage = new Page<>(page, limit);

        // 构建条件
        Page<BatchOrderUse> pageList = batchOrderUseService.pageBatchUseList(batchListPage, batchUseQuery);

        long total = pageList.getTotal();
        List<BatchOrderUse> records = pageList.getRecords();


        return R.ok().data("total", total).data("rows", records).data("totalUse", totalUse);
    }

}

