package com.tea.server.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tea.server.entity.*;
import com.tea.server.entity.vo.*;
import com.tea.server.service.*;
import com.tea.server.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 总商品与配料对应关系表
 * </p>
 *
 * @author zyy
 * @since 2024-04-03
 */
@Slf4j
@RestController
@RequestMapping("goodsAllBatch")
public class GoodsAllBatchController {
    @Autowired
    private GoodsAllBatchService goodsAllBatchService;

    @Autowired
    private BatchingService batchingService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private CategorizeAllService categorizeAllService;

    @Autowired
    private ParamService paramService;

    @Autowired
    private ShopToGoodsService shopToGoodsService;

    @Autowired
    private GoodsToBatchService goodsToBatchService;


    // 产品配方列表
    @PostMapping("getGoodsAllBatchingList/{page}/{limit}")
    public R getGoodsAllBatchingList(@PathVariable long page, @PathVariable long limit, @RequestParam String keyword){
        // 创建page对象
        Page<GoodsAllBatchList> goodsBatchListPage = new Page<>(page, limit);

        // 构建条件
        Page<GoodsAllBatchList> pageList = goodsAllBatchService.getGoodsAllBatchingList(goodsBatchListPage, keyword);

        long total = pageList.getTotal();
        List<GoodsAllBatchList> records = pageList.getRecords();
        return R.ok().data("total", total).data("rows", records);
    }

    // 添加商品配料
    @PostMapping("addGoodsBatching")
    public R addGoodsBatching(@RequestBody GoodsAllBatchVo goodsAllBatchVo){
        final Integer[] count = {0};
        goodsAllBatchVo.getGoodsAllBatchList().stream().forEach(item -> {
            GoodsAllBatch goodsAllBatch = new GoodsAllBatch();
            BeanUtils.copyProperties(item, goodsAllBatch);
            goodsAllBatch.setCreateTime(LocalDateTime.now());
            goodsAllBatch.setUpdateTime(LocalDateTime.now());
            goodsAllBatch.setGoodsId(goodsAllBatchVo.getGoodsId());
            goodsAllBatch.setSizeId(goodsAllBatchVo.getSizeId());
            goodsAllBatch.setTemperatureId(goodsAllBatchVo.getTemperatureId());
            goodsAllBatch.setSugarId(goodsAllBatchVo.getSugarId());
            goodsAllBatch.setCost(goodsAllBatchVo.getCost());
            goodsAllBatch.setPrice(goodsAllBatchVo.getPrice());
            boolean save = goodsAllBatchService.save(goodsAllBatch);
            if (save) {
                count[0] = count[0] + 1;
            }
        });
        if (count[0] == goodsAllBatchVo.getGoodsAllBatchList().size()) {

            LambdaQueryWrapper<ShopToGoods> goodsWrapper = new LambdaQueryWrapper<>();
            goodsWrapper.eq(ShopToGoods :: getStatus, 1).eq(ShopToGoods :: getGoodsId, goodsAllBatchVo.getGoodsId());
            List<ShopToGoods> goodsList = shopToGoodsService.list(goodsWrapper);


            List<Long> shopIds = goodsList.stream().map(good -> good.getShopId()).collect(Collectors.toList());
            goodsAllBatchVo.getGoodsAllBatchList().stream().forEach(item -> {
            if (shopIds.size() > 0){
                shopIds.stream().forEach(shopId -> {
                    LambdaQueryWrapper<ShopToGoods> goodsWrapper2 = new LambdaQueryWrapper<>();
                    goodsWrapper2.eq(ShopToGoods::getStatus, 1).eq(ShopToGoods::getGoodsId, goodsAllBatchVo.getGoodsId()).eq(ShopToGoods::getShopId, shopId);
                    ShopToGoods one = shopToGoodsService.getOne(goodsWrapper2);

                    GoodsToBatch goodsToBatch = new GoodsToBatch();
                    goodsToBatch.setBatchId(item.getBatchId());
                    goodsToBatch.setShopId(shopId);
                    goodsToBatch.setSizeId(goodsAllBatchVo.getSizeId());
                    goodsToBatch.setSugarId(goodsAllBatchVo.getSugarId());
                    goodsToBatch.setTemperatureId(goodsAllBatchVo.getTemperatureId());
                    goodsToBatch.setGoodsId(one.getId());
                    goodsToBatch.setUseNumber(item.getUseNumber());
                    goodsToBatch.setCost(goodsAllBatchVo.getCost());
                    goodsToBatch.setPrice(goodsAllBatchVo.getPrice());
                    goodsToBatch.setCreateTime(LocalDateTime.now());
                    goodsToBatch.setUpdateTime(LocalDateTime.now());
                    goodsToBatchService.save(goodsToBatch);
                });
            }
            });

            return R.ok();
        }else {
            return R.error();
        }
    }

    // 修改商品配料
    @PostMapping("updateGoodsBatching")
    public R updateGoodsBatching(@RequestBody GoodsAllBatchVo goodsAllBatchVo){
        final Integer[] count = {0};
        LambdaQueryWrapper<GoodsAllBatch> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GoodsAllBatch :: getStatus, 1).eq(GoodsAllBatch :: getGoodsId, goodsAllBatchVo.getGoodsId()).eq(GoodsAllBatch :: getTemperatureId, goodsAllBatchVo.getTemperatureId());
        queryWrapper.eq(GoodsAllBatch :: getSizeId, goodsAllBatchVo.getSizeId()).eq(GoodsAllBatch :: getSugarId, goodsAllBatchVo.getSugarId());
        List<GoodsAllBatch> list = goodsAllBatchService.list(queryWrapper);

        boolean remove = goodsAllBatchService.remove(queryWrapper);

        if (remove) {

            LambdaQueryWrapper<ShopToGoods> goodsWrapper = new LambdaQueryWrapper<>();
            goodsWrapper.eq(ShopToGoods :: getStatus, 1).eq(ShopToGoods :: getGoodsId, goodsAllBatchVo.getGoodsId());
            List<ShopToGoods> goodsList = shopToGoodsService.list(goodsWrapper);


            List<Long> shopIds = goodsList.stream().map(good -> good.getShopId()).collect(Collectors.toList());
            List<Long> goodsIds = goodsList.stream().map(good -> good.getId()).collect(Collectors.toList());

            if (null != goodsList && goodsList.size() > 0){
                LambdaQueryWrapper<GoodsToBatch> batchWrapper = new LambdaQueryWrapper<>();
                batchWrapper.eq(GoodsToBatch :: getStatus, 1).in(GoodsToBatch :: getGoodsId, goodsIds).eq(GoodsToBatch :: getSizeId, goodsAllBatchVo.getSizeId());
                batchWrapper.eq(GoodsToBatch :: getSugarId, goodsAllBatchVo.getSugarId()).eq(GoodsToBatch :: getTemperatureId, goodsAllBatchVo.getTemperatureId());
                List<GoodsToBatch> goodsToBatches = goodsToBatchService.list(batchWrapper);

                if (null != goodsToBatches && goodsToBatches.size() > 0) {
                    goodsToBatchService.remove(batchWrapper);
                }
            }

            goodsAllBatchVo.getGoodsAllBatchList().stream().forEach(item -> {

                GoodsAllBatch goodsAllBatch = new GoodsAllBatch();
                goodsAllBatch.setBatchId(item.getBatchId());
                goodsAllBatch.setUseNumber(item.getUseNumber());
                goodsAllBatch.setSugarId(goodsAllBatchVo.getSugarId());
                goodsAllBatch.setTemperatureId(goodsAllBatchVo.getTemperatureId());
                goodsAllBatch.setCost(goodsAllBatchVo.getCost());
                goodsAllBatch.setPrice(goodsAllBatchVo.getPrice());
                goodsAllBatch.setCreateTime(list.get(0).getCreateTime());
                goodsAllBatch.setUpdateTime(LocalDateTime.now());
                goodsAllBatch.setGoodsId(goodsAllBatchVo.getGoodsId());
                goodsAllBatch.setSizeId(goodsAllBatchVo.getSizeId());
                boolean save = goodsAllBatchService.save(goodsAllBatch);

                if (save) {
                        if (shopIds.size() > 0){
                            shopIds.stream().forEach(shopId -> {
                                LambdaQueryWrapper<ShopToGoods> goodsWrapper2 = new LambdaQueryWrapper<>();
                                goodsWrapper2.eq(ShopToGoods::getStatus, 1).eq(ShopToGoods::getGoodsId, goodsAllBatchVo.getGoodsId()).eq(ShopToGoods::getShopId, shopId);
                                ShopToGoods one = shopToGoodsService.getOne(goodsWrapper2);
                                GoodsToBatch goodsToBatch = new GoodsToBatch();
                                goodsToBatch.setBatchId(item.getBatchId());
                                goodsToBatch.setShopId(shopId);
                                goodsToBatch.setSizeId(goodsAllBatchVo.getSizeId());
                                goodsToBatch.setSugarId(goodsAllBatchVo.getSugarId());
                                goodsToBatch.setTemperatureId(goodsAllBatchVo.getTemperatureId());
                                goodsToBatch.setGoodsId(one.getId());
                                goodsToBatch.setUseNumber(item.getUseNumber());
                                goodsToBatch.setCost(goodsAllBatchVo.getCost());
                                goodsToBatch.setPrice(goodsAllBatchVo.getPrice());
                                goodsToBatch.setCreateTime(LocalDateTime.now());
                                goodsToBatch.setUpdateTime(LocalDateTime.now());
                                goodsToBatchService.save(goodsToBatch);
                            });
                        }

                }
            });
            return R.ok();

        }else {
            return R.error();
        }


    }

    // 查询商品配料
    @GetMapping("getGoodsBatchInfo/{goodsId}/{sizeId}/{sugarId}/{temperatureId}")
    public R getGoodsBatchInfo(@PathVariable Long goodsId, @PathVariable Long sizeId, @PathVariable Long sugarId, @PathVariable Long temperatureId){
        LambdaQueryWrapper<GoodsAllBatch> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GoodsAllBatch :: getStatus, 1).eq(GoodsAllBatch :: getGoodsId, goodsId).eq(GoodsAllBatch :: getSizeId, sizeId).eq(GoodsAllBatch :: getSugarId, sugarId).eq(GoodsAllBatch :: getTemperatureId, temperatureId);
        List<GoodsAllBatch> list = goodsAllBatchService.list(queryWrapper);
        GoodsAllBatchVo goodsAllBatchVo = new GoodsAllBatchVo();
        goodsAllBatchVo.setFlag("info");
        goodsAllBatchVo.setGoodsId(goodsId);
        goodsAllBatchVo.setSizeId(sizeId);
        goodsAllBatchVo.setSugarId(sugarId);
        goodsAllBatchVo.setCost(list.get(0).getCost());
        goodsAllBatchVo.setPrice(list.get(0).getPrice());
        goodsAllBatchVo.setTemperatureId(temperatureId);
        goodsAllBatchVo.setGoodsAllBatchList(list);
        return R.ok().data("info", goodsAllBatchVo);
    }
    /**
     * 查询当前店铺全部商品 包含分类
     */
    // @GetMapping("goodsAll/{shopId}")
    // public R goodsAll(@PathVariable("shopId") Long shopId){
    //     List<GoodsAndMachine> goodsAndMachineList = new ArrayList<>();
    //     LambdaQueryWrapper<ShopToGoods> wrapper = new LambdaQueryWrapper<>();
    //     wrapper.eq(ShopToGoods :: getStatus, 1).eq(ShopToGoods :: getIsGrounding, 1).eq(ShopToGoods :: getShopId, shopId);
    //     List<ShopToGoods> list = shopToGoodsService.list(wrapper);
    //     if (list.size() > 0){
    //         List<Long> goodsIds = list.stream().map(item -> item.getGoodsId()).collect(Collectors.toList());
    //         LambdaQueryWrapper<Goods> queryWrapper = new LambdaQueryWrapper<>();
    //         queryWrapper.eq(Goods :: getIsGrounding, 1).eq(Goods :: getStatus, 1).in(Goods :: getId, goodsIds);
    //         List<Goods> goodsList = goodsService.list(queryWrapper);
    //
    //         goodsList.stream().forEach(item -> {
    //             Categorize categorize = categorizeService.getById(item.getCateId());
    //             GoodsAndMachine goodsAndMachine = new GoodsAndMachine();
    //             BeanUtils.copyProperties(item, goodsAndMachine);
    //             goodsAndMachine.setMachineNo(categorize.getMachineNo());
    //             LambdaQueryWrapper<ShopToGoods> goodQuery = new LambdaQueryWrapper<>();
    //             goodQuery.eq(ShopToGoods :: getStatus, 1).eq(ShopToGoods :: getIsGrounding, 1).eq(ShopToGoods :: getGoodsId, item.getId()).eq(ShopToGoods :: getShopId, shopId);
    //             ShopToGoods one = shopToGoodsService.getOne(goodQuery);
    //             goodsAndMachine.setAddBatch(one.getAddBatch());
    //             goodsAndMachineList.add(goodsAndMachine);
    //         });
    //     }
    //     return R.ok().data("list", goodsAndMachineList);
    // }


    @DeleteMapping("remove/{goodsId}/{sizeId}/{sugarId}/{temperatureId}")
    public R deleteParamBatch(@PathVariable("goodsId") Long goodsId, @PathVariable("sizeId") Long sizeId, @PathVariable("sugarId") Long sugarId, @PathVariable("temperatureId") Long temperatureId){
        LambdaQueryWrapper<GoodsAllBatch> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GoodsAllBatch :: getStatus, 1).eq(GoodsAllBatch :: getGoodsId, goodsId).eq(GoodsAllBatch :: getSizeId, sizeId).eq(GoodsAllBatch :: getSugarId, sugarId).eq(GoodsAllBatch :: getTemperatureId, temperatureId);

        boolean b = goodsAllBatchService.remove(wrapper);
        if (b) {
            return R.ok();
        }else {
            return R.error();
        }

    }

}

