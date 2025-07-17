package com.tea.server.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tea.server.entity.*;
import com.tea.server.entity.data.GoodBatch;
import com.tea.server.service.*;
import com.tea.server.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.AfterMapping;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 店家签约配方价格表 前端控制器
 * </p>
 *
 * @author zyy
 * @since 2024-05-22
 */
@Slf4j
@RestController
@RequestMapping("batchShopPrice")
public class BatchShopPriceController {

    @Autowired
    private BatchShopPriceService batchShopPriceService;


    @Autowired
    private GoodsService goodsService;

    @Autowired
    private GoodsAllBatchService goodsAllBatchService;

    @Autowired
    private BatchingService batchingService;

    @Autowired
    private ShopsService shopsService;

    @GetMapping("getShopBatch/{shopId}")
    public R getShopBatch(@PathVariable Long shopId){
        Shop shop = shopsService.getById(shopId);
        String goodRange = shop.getGoodRange();
        List<BatchShopPrice> list = new ArrayList<>();
        if (null != goodRange && !goodRange.equals("")) {
            List<Long> goodRangeList = Arrays.asList(goodRange.split(",")).stream().map(Long::valueOf).collect(Collectors.toList());
            shop.setGoodRangeList(goodRangeList);
            if (goodRangeList.size() > 0) {
                List<Long> goodIds = goodRangeList.stream().map(Long::valueOf).collect(Collectors.toList());
                LambdaQueryWrapper<GoodsAllBatch> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(GoodsAllBatch :: getStatus, 1).in(GoodsAllBatch :: getGoodsId, goodIds);
                List<GoodsAllBatch> allBatches = goodsAllBatchService.list(wrapper);

                if (null != allBatches && allBatches.size() > 0){
                    List<Long> batchIds = allBatches.stream().map(b -> b.getBatchId()).collect(Collectors.toList());
                    LambdaQueryWrapper<Batching> batchWrapper = new LambdaQueryWrapper<>();
                    batchWrapper.eq(Batching :: getStatus, 1).in(Batching :: getId, batchIds);
                    List<Batching> batchings = batchingService.list(batchWrapper);

                    if (null != batchings && batchings.size() > 0){
                        batchings.stream().forEach(ch ->{
                            BatchShopPrice batchShopPrice = new BatchShopPrice();
                            batchShopPrice.setShopId(shopId);
                            batchShopPrice.setBatchId(ch.getId());
                            batchShopPrice.setName(ch.getName());
                            LambdaQueryWrapper<BatchShopPrice> priceWrapper = new LambdaQueryWrapper<>();
                            priceWrapper.eq(BatchShopPrice :: getStatus, 1).eq(BatchShopPrice :: getShopId, shopId).eq(BatchShopPrice :: getBatchId, ch.getId());
                            BatchShopPrice priceServiceOne = batchShopPriceService.getOne(priceWrapper);
                            if (null != priceServiceOne){
                                batchShopPrice.setId(priceServiceOne.getId());
                                batchShopPrice.setPrice(priceServiceOne.getPrice());
                            }
                            list.add(batchShopPrice);
                        });
                        return R.ok().data("list", list);
                    }else {
                        return R.ok().data("list", list);
                    }

                }else {
                    return R.ok().data("list", list);
                }

            }else {
                return R.ok().data("list", list);
            }
        }else {
            return R.ok().data("list", list);
        }
    }

    @PostMapping("saveShopBatch")
    public R saveShopBatch (@RequestBody GoodBatch goodBatch){
        List<BatchShopPrice> batchList = goodBatch.getBatchList();
        if (null != batchList && batchList.size() > 0){
            batchList.stream().forEach(item ->{
                BatchShopPrice batchShopPrice = new BatchShopPrice();
                BeanUtils.copyProperties(item, batchShopPrice);
                if (null != batchShopPrice.getId()){
                    batchShopPrice.setUpdateTime(LocalDateTime.now());
                    batchShopPriceService.updateById(batchShopPrice);

                }else {
                    batchShopPrice.setCreateTime(LocalDateTime.now());
                    batchShopPrice.setUpdateTime(LocalDateTime.now());
                    batchShopPriceService.save(batchShopPrice);
                }

            });
        }
        return R.ok();
    }

}

