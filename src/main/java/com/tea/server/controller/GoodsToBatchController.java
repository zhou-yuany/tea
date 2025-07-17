package com.tea.server.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tea.server.entity.*;
import com.tea.server.entity.query.GoodsBatchQuery;
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
 * 商品与配料对应关系表 前端控制器
 * </p>
 *
 * @author zyy
 * @since 2023-08-24
 */
@Slf4j
@RestController
@RequestMapping("goodsToBatch")
public class GoodsToBatchController {
    @Autowired
    private GoodsToBatchService goodsToBatchService;

    @Autowired
    private BatchingService batchingService;

    @Autowired
    private ShopToGoodsService shopToGoodsService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private CategorizeService categorizeService;

    @Autowired
    private ShopToParamService shopToParamService;

    @Autowired
    private GoodsAllBatchService goodsAllBatchService;

    // *******
    @Autowired
    private AdminsService adminsService;

    @Autowired
    private ShopsService shopService;

    @Autowired
    private ParamService paramService;

    @Autowired
    private InterfaceLogService interfaceLogService;
    /**
     * 根据店铺id获取当前商品成本卡 分页
     */
    @PostMapping("getCostCards/{page}/{limit}/{shopId}")
    public R getCostCards(@PathVariable long page, @PathVariable long limit, @PathVariable Long shopId) {
        // 创建page对象
        Page<CostCardVo> costCardVoPage = new Page<>(page, limit);

        // 构建条件
        Page<CostCardVo> pageList = goodsToBatchService.pageCostCards(costCardVoPage, shopId);

        long total = pageList.getTotal();
        List<CostCardVo> records = pageList.getRecords();
        return R.ok().data("total", total).data("rows", records);
    }

    // 根据店铺id获取当前商品成本卡配方详情
    @GetMapping("getCostCardInfo/{id}/{sizeId}")
    public R getCostCardInfo(@PathVariable Long id, @PathVariable Long sizeId) {
        GoodsToBatch goodsToBatch = goodsToBatchService.getById(id);
        LambdaQueryWrapper<GoodsToBatch> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GoodsToBatch::getStatus, 1).eq(GoodsToBatch::getGoodsId, goodsToBatch.getGoodsId()).eq(GoodsToBatch::getSizeId, sizeId);
        List<GoodsToBatch> list = goodsToBatchService.list(queryWrapper);
        List<CostCardInfoVo> costCardInfoVoList = new ArrayList<>();
        if (list.size() > 0) {
            list.stream().forEach(item -> {
                CostCardInfoVo costCardInfoVo = new CostCardInfoVo();
                Batching batching = batchingService.getById(item.getBatchId());
                costCardInfoVo.setBatchName(batching.getName());
                costCardInfoVo.setBatchUnit(batching.getUnit());
                costCardInfoVo.setBatchUseCount(item.getUseNumber());
                costCardInfoVoList.add(costCardInfoVo);
            });
        }
        return R.ok().data("list", costCardInfoVoList);
    }

    // 产品配方列表
    @PostMapping("getGoodsBatchingList1/{page}/{limit}/{shopId}")
    public R getGoodsBatchingList1(@PathVariable long page, @PathVariable long limit, @PathVariable Long shopId, @RequestParam String keyword) {
        // 创建page对象
        Page<GoodsBatchList> goodsBatchListPage = new Page<>(page, limit);

        // 构建条件
        Page<GoodsBatchList> pageList = goodsToBatchService.getGoodsBatchingList1(goodsBatchListPage, shopId, keyword);

        long total = pageList.getTotal();
        List<GoodsBatchList> records = pageList.getRecords();
        return R.ok().data("total", total).data("rows", records);
    }

    // 添加产品配方
    @PostMapping("addGoodsBatching")
    public R addGoodsBatching(@RequestBody GoodsToBatchVo goodsToBatchVo) {

            final Integer[] count = {0};
            // LambdaQueryWrapper<ShopToGoods> queryWrapper = new LambdaQueryWrapper<>();
            // queryWrapper.eq(ShopToGoods::getStatus, 1).eq(ShopToGoods::getShopId, goodsToBatchVo.getShopId()).eq(ShopToGoods::getGoodsId, goodsToBatchVo.getGoodsId());
            // ShopToGoods shopToGoods = shopToGoodsService.getOne(queryWrapper);

            ShopToGoods shopToGoods = shopToGoodsService.getById(goodsToBatchVo.getGoodsId());
            shopToGoods.setAddBatch(1);
            shopToGoodsService.updateById(shopToGoods);
            goodsToBatchVo.getGoodsToBatchList().stream().forEach(item -> {
                GoodsToBatch goodsToBatch1 = new GoodsToBatch();
                BeanUtils.copyProperties(item, goodsToBatch1);
                goodsToBatch1.setCreateTime(LocalDateTime.now());
                goodsToBatch1.setUpdateTime(LocalDateTime.now());
                goodsToBatch1.setShopId(goodsToBatchVo.getShopId());
                goodsToBatch1.setGoodsId(goodsToBatchVo.getGoodsId());
                goodsToBatch1.setSizeId(goodsToBatchVo.getSizeId());
                goodsToBatch1.setSugarId(goodsToBatchVo.getSugarId());
                goodsToBatch1.setPrice(goodsToBatchVo.getPrice());
                goodsToBatch1.setCost(goodsToBatchVo.getCost());
                goodsToBatch1.setTemperatureId(goodsToBatchVo.getTemperatureId());
                boolean save = goodsToBatchService.save(goodsToBatch1);
                if (save) {
                    count[0] = count[0] + 1;
                }
            });
            if (count[0] == goodsToBatchVo.getGoodsToBatchList().size()) {
                Param sizeParam = paramService.getById(goodsToBatchVo.getSizeId());
                Param sugarParam = paramService.getById(goodsToBatchVo.getSugarId());
                Param temperatureParam = paramService.getById(goodsToBatchVo.getTemperatureId());
                Shop shop = shopService.getById(shopToGoods.getShopId());
                InterfaceLog interfaceLog = new InterfaceLog();
                interfaceLog.setTitle("商户添加产品配方");
                interfaceLog.setMethodName("");
                String content = "商户"+shop.getName()+"添加产品配方，商品名为"+shopToGoods.getName()+"（"+sizeParam.getName()+"-"+sugarParam.getName()+"-"+temperatureParam.getName()+"）";
                interfaceLog.setContent(content);
                interfaceLog.setShopId(shopToGoods.getShopId());
                interfaceLog.setTypeStatus(0);
                interfaceLog.setCreateTime(LocalDateTime.now());
                interfaceLog.setUpdateTime(LocalDateTime.now());
                interfaceLogService.save(interfaceLog);
                return R.ok();
            } else {
                return R.error();
            }




    }

    // 修改产品配方
    @PostMapping("updateGoodsBatching")
    public R updateGoodsBatching(@RequestBody GoodsToBatchVo goodsToBatchVo) {
        ShopToGoods shopToGoods = shopToGoodsService.getById(goodsToBatchVo.getGoodsId());
        shopToGoods.setAddBatch(2);
        shopToGoodsService.updateById(shopToGoods);
        goodsToBatchVo.getGoodsToBatchList().stream().forEach(item -> {
            GoodsToBatch goodsToBatch1 = new GoodsToBatch();
            BeanUtils.copyProperties(item, goodsToBatch1);
            goodsToBatch1.setUseNumber(item.getUseNumber());
            goodsToBatch1.setUpdateTime(LocalDateTime.now());
            goodsToBatch1.setPrice(goodsToBatchVo.getPrice());
            goodsToBatch1.setCost(goodsToBatchVo.getCost());
            goodsToBatchService.updateById(goodsToBatch1);


        });
        Param sizeParam = paramService.getById(goodsToBatchVo.getSizeId());
        Param sugarParam = paramService.getById(goodsToBatchVo.getSugarId());
        Param temperatureParam = paramService.getById(goodsToBatchVo.getTemperatureId());
        Shop shop = shopService.getById(shopToGoods.getShopId());
        InterfaceLog interfaceLog = new InterfaceLog();
        interfaceLog.setTitle("商户修改产品配方");
        interfaceLog.setMethodName("");
        String content = "商户"+shop.getName()+"修改产品配方，商品名为"+shopToGoods.getName()+"（"+sizeParam.getName()+"-"+sugarParam.getName()+"-"+temperatureParam.getName()+"）";
        interfaceLog.setContent(content);
        interfaceLog.setShopId(shopToGoods.getShopId());
        interfaceLog.setTypeStatus(0);
        interfaceLog.setCreateTime(LocalDateTime.now());
        interfaceLog.setUpdateTime(LocalDateTime.now());
        interfaceLogService.save(interfaceLog);
        return R.ok();


    }

    // // 查询商品配料
    // @GetMapping("getGoodsBatchInfo/{goodsId}/{shopId}")
    // public R getGoodsBatchInfo(@PathVariable Long goodsId, @PathVariable Long shopId){
    //     LambdaQueryWrapper<GoodsToBatch> queryWrapper = new LambdaQueryWrapper<>();
    //     queryWrapper.eq(GoodsToBatch :: getStatus, 1).eq(GoodsToBatch :: getShopId, shopId).eq(GoodsToBatch :: getGoodsId, goodsId);
    //     List<GoodsToBatch> list = goodsToBatchService.list(queryWrapper);
    //     GoodsToBatchVo goodsToBatchVo = new GoodsToBatchVo();
    //     goodsToBatchVo.setGoodsId(goodsId);
    //     goodsToBatchVo.setGoodsToBatchList(list);
    //     return R.ok().data("info", goodsToBatchVo);
    // }
    // 查询商品配料
    @GetMapping("getGoodsBatchInfo/{goodsId}/{sizeId}/{sugarId}/{temperatureId}/{shopId}")
    public R getGoodsBatchInfo(@PathVariable Long goodsId, @PathVariable Long sizeId, @PathVariable Long sugarId, @PathVariable Long temperatureId, @PathVariable Long shopId) {
        Shop one = shopService.getById(shopId);

        // LambdaQueryWrapper<ShopToGoods> wrapper2 = new LambdaQueryWrapper<>();
        // wrapper2.eq(ShopToGoods::getStatus, 1).eq(ShopToGoods::getId, goodsId).eq(ShopToGoods::getShopId, one.getId());
        // ShopToGoods goods = shopToGoodsService.getOne(wrapper2);
        ShopToGoods goods = shopToGoodsService.getById(goodsId);
        Param param = paramService.getById(sizeId);
        LambdaQueryWrapper<GoodsToBatch> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GoodsToBatch::getStatus, 1).eq(GoodsToBatch::getShopId, one.getId()).eq(GoodsToBatch::getGoodsId, goodsId).eq(GoodsToBatch::getSizeId, sizeId);
        queryWrapper.eq(GoodsToBatch :: getSugarId, sugarId).eq(GoodsToBatch :: getTemperatureId, temperatureId);
        List<GoodsToBatch> list = goodsToBatchService.list(queryWrapper);
        list.stream().forEach(item -> {
            Batching batching = batchingService.getById(item.getBatchId());
            item.setBatchName(batching.getName());
            item.setUnit(batching.getUnit());
        });
        GoodsToBatchVo goodsToBatchVo = new GoodsToBatchVo();
        goodsToBatchVo.setId(goods.getId());
        goodsToBatchVo.setGoodsId(goodsId);
        goodsToBatchVo.setSizeId(sizeId);
        goodsToBatchVo.setGoodsName(goods.getName());
        goodsToBatchVo.setSizeName(param.getName());
        goodsToBatchVo.setSugarId(sugarId);
        goodsToBatchVo.setTemperatureId(temperatureId);
        goodsToBatchVo.setCost(list.get(0).getCost());
        goodsToBatchVo.setPrice(list.get(0).getPrice());
        goodsToBatchVo.setGoodsToBatchList(list);
        return R.ok().data("info", goodsToBatchVo);
    }

    /**
     * 查询当前店铺全部商品 包含分类
     */
    @GetMapping("goodsAll/{shopId}")
    public R goodsAll(@PathVariable("shopId") Long shopId) {
        List<GoodsAndMachine> goodsAndMachineList = new ArrayList<>();
        LambdaQueryWrapper<ShopToGoods> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShopToGoods::getStatus, 1).eq(ShopToGoods::getIsGrounding, 1).eq(ShopToGoods::getShopId, shopId);
        List<ShopToGoods> list = shopToGoodsService.list(wrapper);
        if (list.size() > 0) {
            List<Long> goodsIds = list.stream().map(item -> item.getGoodsId()).collect(Collectors.toList());
            LambdaQueryWrapper<Goods> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Goods::getIsGrounding, 1).eq(Goods::getStatus, 1).in(Goods::getId, goodsIds);
            List<Goods> goodsList = goodsService.list(queryWrapper);

            goodsList.stream().forEach(item -> {
                Categorize categorize = categorizeService.getById(item.getCateId());
                GoodsAndMachine goodsAndMachine = new GoodsAndMachine();
                BeanUtils.copyProperties(item, goodsAndMachine);
                goodsAndMachine.setMachineNo(categorize.getMachineNo());
                LambdaQueryWrapper<ShopToGoods> goodQuery = new LambdaQueryWrapper<>();
                goodQuery.eq(ShopToGoods::getStatus, 1).eq(ShopToGoods::getIsGrounding, 1).eq(ShopToGoods::getGoodsId, item.getId()).eq(ShopToGoods::getShopId, shopId);
                ShopToGoods one = shopToGoodsService.getOne(goodQuery);
                goodsAndMachine.setAddBatch(one.getAddBatch());
                goodsAndMachineList.add(goodsAndMachine);
            });
        }
        LambdaQueryWrapper<Param> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Param::getStatus, 1).like(Param::getType, 1);
        List<Param> paramList = paramService.list(queryWrapper);
        return R.ok().data("list", goodsAndMachineList).data("paramList", paramList);
    }


    // @DeleteMapping("remove/{goodsId}/{shopId}")
    // public R deleteParamBatch(@PathVariable Long goodsId, @PathVariable Long shopId){
    //
    //     LambdaQueryWrapper<GoodsToBatch> queryWrapper = new LambdaQueryWrapper<>();
    //     queryWrapper.eq(GoodsToBatch :: getGoodsId, goodsId).eq(GoodsToBatch :: getShopId, shopId);
    //     boolean b = goodsToBatchService.remove(queryWrapper);
    //     if (b) {
    //         LambdaQueryWrapper<ShopToGoods> wrapper = new LambdaQueryWrapper<>();
    //         wrapper.eq(ShopToGoods :: getStatus, 1).eq(ShopToGoods :: getShopId, shopId).eq(ShopToGoods :: getGoodsId, goodsId);
    //         ShopToGoods shopToGoods = shopToGoodsService.getOne(wrapper);
    //         shopToGoods.setAddBatch(2);
    //         shopToGoodsService.updateById(shopToGoods);
    //         return R.ok();
    //     }else {
    //         return R.error();
    //     }
    //
    // }


    // 茶饮竖屏 配方列表
    @PostMapping("getGoodsBatchings/{adminId}")
    public R getGoodsBatchings(@PathVariable Long adminId, @RequestBody GoodsBatchQuery goodsBatchQuery) {
        LambdaQueryWrapper<Shop> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Shop::getStatus, 1).in(Shop::getAdminId, adminId);
        Shop one = shopService.getOne(queryWrapper);

        // 构建条件
        List<GoodsToBatchList> list = goodsToBatchService.getGoodsAllBatchingList(one.getId(), goodsBatchQuery);

        return R.ok().data("list", list);
    }

    /**
     * *****************************************************************
     */

    @GetMapping("getShopParams/{shopId}")
    public R getCupSizes(@PathVariable Long shopId) {
        LambdaQueryWrapper<ShopToParam> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShopToParam::getStatus, 1).eq(ShopToParam::getShopId, shopId);
        List<ShopToParam> paramList = shopToParamService.list(queryWrapper);

        return R.ok().data("paramList", paramList);
    }


    // 产品配方列表
    @PostMapping("getGoodsBatchingList/{page}/{limit}/{shopId}")
    public R getGoodsBatchingList(@PathVariable long page, @PathVariable long limit, @PathVariable Long shopId, @RequestParam String keyword) {
        // 创建page对象
        Page<GoodsBatchList> goodsBatchListPage = new Page<>(page, limit);

        // 构建条件
        Page<GoodsBatchList> pageList = goodsToBatchService.getGoodsBatchingList(goodsBatchListPage, shopId, keyword);

        long total = pageList.getTotal();
        List<GoodsBatchList> records = pageList.getRecords();
        return R.ok().data("total", total).data("rows", records);
    }
    //
    // // 添加商品配料
    // @PostMapping("addGoodsBatching")
    // public R addGoodsBatching(@RequestBody GoodsToBatchVo goodsToBatchVo){
    //     final Integer[] count = {0};
    //     LambdaQueryWrapper<ShopToGoods> queryWrapper = new LambdaQueryWrapper<>();
    //     queryWrapper.eq(ShopToGoods :: getStatus, 1).eq(ShopToGoods :: getShopId, goodsToBatchVo.getShopId()).eq(ShopToGoods :: getGoodsId, goodsToBatchVo.getGoodsId());
    //     ShopToGoods shopToGoods = shopToGoodsService.getOne(queryWrapper);
    //     shopToGoods.setAddBatch(1);
    //     shopToGoodsService.updateById(shopToGoods);
    //     goodsToBatchVo.getGoodsToBatchList().stream().forEach(item -> {
    //         GoodsToBatch goodsToBatch1 = new GoodsToBatch();
    //         BeanUtils.copyProperties(item, goodsToBatch1);
    //         goodsToBatch1.setCreateTime(LocalDateTime.now());
    //         goodsToBatch1.setUpdateTime(LocalDateTime.now());
    //         goodsToBatch1.setShopId(goodsToBatchVo.getShopId());
    //         goodsToBatch1.setGoodsId(goodsToBatchVo.getGoodsId());
    //         goodsToBatch1.setSizeId(goodsToBatchVo.getSizeId());
    //         boolean save = goodsToBatchService.save(goodsToBatch1);
    //         if (save) {
    //             count[0] = count[0] + 1;
    //         }
    //     });
    //     if (count[0] == goodsToBatchVo.getGoodsToBatchList().size()) {
    //         return R.ok();
    //     }else {
    //         return R.error();
    //     }
    // }

    // // 修改商品配料
    // @PostMapping("updateGoodsBatching")
    // public R updateGoodsBatching(@RequestBody GoodsToBatchVo goodsToBatchVo){
    //     final Integer[] count = {0};
    //         LambdaQueryWrapper<ShopToGoods> wrapper = new LambdaQueryWrapper<>();
    //         wrapper.eq(ShopToGoods :: getStatus, 1).eq(ShopToGoods :: getShopId, goodsToBatchVo.getShopId()).eq(ShopToGoods :: getGoodsId, goodsToBatchVo.getGoodsId());
    //         ShopToGoods shopToGoods = shopToGoodsService.getOne(wrapper);
    //         shopToGoods.setAddBatch(2);
    //         shopToGoodsService.updateById(shopToGoods);
    //         goodsToBatchVo.getGoodsToBatchList().stream().forEach(item -> {
    //             GoodsToBatch goodsToBatch1 = new GoodsToBatch();
    //             BeanUtils.copyProperties(item, goodsToBatch1);
    //             goodsToBatch1.setUseNumber(item.getUseNumber());
    //             goodsToBatch1.setUpdateTime(LocalDateTime.now());
    //             goodsToBatch1.setSizeId(goodsToBatchVo.getSizeId());
    //             boolean save = goodsToBatchService.updateById(goodsToBatch1);
    //             if (save) {
    //                 count[0] = count[0] + 1;
    //             }
    //         });
    //         if (count[0] == goodsToBatchVo.getGoodsToBatchList().size()) {
    //             return R.ok();
    //         }else {
    //             return R.error();
    //         }
    //
    // }

    // // 修改商品配料
    // @PostMapping("updateGoodsBatching")
    // public R updateGoodsBatching(@RequestBody GoodsToBatchVo goodsToBatchVo){
    //     final Integer[] count = {0};
    //     LambdaQueryWrapper<GoodsToBatch> queryWrapper = new LambdaQueryWrapper<>();
    //     queryWrapper.eq(GoodsToBatch :: getStatus, 1).eq(GoodsToBatch :: getShopId, goodsToBatchVo.getShopId()).eq(GoodsToBatch :: getGoodsId, goodsToBatchVo.getGoodsId());
    //     boolean remove = goodsToBatchService.remove(queryWrapper);
    //
    //     if (remove) {
    //         LambdaQueryWrapper<ShopToGoods> wrapper = new LambdaQueryWrapper<>();
    //         wrapper.eq(ShopToGoods :: getStatus, 1).eq(ShopToGoods :: getShopId, goodsToBatchVo.getShopId()).eq(ShopToGoods :: getGoodsId, goodsToBatchVo.getGoodsId());
    //         ShopToGoods shopToGoods = shopToGoodsService.getOne(wrapper);
    //         shopToGoods.setAddBatch(2);
    //         shopToGoodsService.updateById(shopToGoods);
    //         goodsToBatchVo.getGoodsToBatchList().stream().forEach(item -> {
    //             GoodsToBatch goodsToBatch1 = new GoodsToBatch();
    //             goodsToBatch1.setBatchId(item.getBatchId());
    //             goodsToBatch1.setUseNumber(item.getUseNumber());
    //             goodsToBatch1.setCreateTime(LocalDateTime.now());
    //             goodsToBatch1.setUpdateTime(LocalDateTime.now());
    //             goodsToBatch1.setShopId(goodsToBatchVo.getShopId());
    //             goodsToBatch1.setGoodsId(goodsToBatchVo.getGoodsId());
    //             // goodsToBatch1.setParamId(goodsToBatchVo.getParamId());
    //             boolean save = goodsToBatchService.save(goodsToBatch1);
    //             if (save) {
    //                 count[0] = count[0] + 1;
    //             }
    //         });
    //         if (count[0] == goodsToBatchVo.getGoodsToBatchList().size()) {
    //             return R.ok();
    //         }else {
    //             return R.error();
    //         }
    //     }else {
    //         return R.error();
    //     }
    //
    //
    // }
    // 查询总商品配料详情
    @GetMapping("getAllGoodsBatchInfo/{goodsId}/{shopId}/{sizeId}/{sugarId}/{temperatureId}")
    public R getAllGoodsBatchInfo(@PathVariable Long goodsId, @PathVariable Long shopId, @PathVariable Long sizeId, @PathVariable Long sugarId, @PathVariable Long temperatureId) {
        ShopToGoods shopToGoods = shopToGoodsService.getById(goodsId);

        if (null == shopToGoods){
            return R.ok().message("请到产品管理页添加此产品！");
        }else {
            LambdaQueryWrapper<GoodsAllBatch> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(GoodsAllBatch::getStatus, 1).eq(GoodsAllBatch::getGoodsId, shopToGoods.getGoodsId()).eq(GoodsAllBatch::getSizeId, sizeId).eq(GoodsAllBatch::getSugarId, sugarId).eq(GoodsAllBatch::getTemperatureId, temperatureId);
            List<GoodsAllBatch> list = goodsAllBatchService.list(wrapper);
            List<GoodsToBatch> newList = new ArrayList<>();
            if (null != list && list.size() > 0) {
                list.stream().forEach(item -> {
                    GoodsToBatch goodsToBatch = new GoodsToBatch();
                    goodsToBatch.setUseNumber(item.getUseNumber());
                    goodsToBatch.setBatchId(item.getBatchId());
                    newList.add(goodsToBatch);
                });
                GoodsToBatchVo goodsToBatchVo = new GoodsToBatchVo();
                goodsToBatchVo.setGoodsId(goodsId);
                goodsToBatchVo.setGoodsToBatchList(newList);
                goodsToBatchVo.setPrice(list.get(0).getPrice());
                goodsToBatchVo.setCost(list.get(0).getCost());
                return R.ok().data("info", goodsToBatchVo);

            }else {
                return R.ok().message("总平台未添加该配方，请联系总平台添加");
            }


        }


    }

    // // 查询商家商品配料
    // @GetMapping("getGoodsBatchInfo/{goodsId}/{sizeId}/{shopId}")
    // public R getGoodsBatchInfo(@PathVariable Long goodsId, @PathVariable Long sizeId, @PathVariable Long shopId){
    //     LambdaQueryWrapper<GoodsToBatch> queryWrapper = new LambdaQueryWrapper<>();
    //     queryWrapper.eq(GoodsToBatch :: getStatus, 1).eq(GoodsToBatch :: getShopId, shopId).eq(GoodsToBatch :: getGoodsId, goodsId).eq(GoodsToBatch :: getSizeId, sizeId);
    //     List<GoodsToBatch> list = goodsToBatchService.list(queryWrapper);
    //     GoodsToBatchVo goodsToBatchVo = new GoodsToBatchVo();
    //     goodsToBatchVo.setGoodsId(goodsId);
    //     goodsToBatchVo.setSizeId(sizeId);
    //     goodsToBatchVo.setGoodsToBatchList(list);
    //     return R.ok().data("info", goodsToBatchVo);
    // }

    // /**
    //  * 查询当前店铺全部商品 包含分类
    //  */
    // @GetMapping("goodsAll/{shopId}")
    // public R goodsAll(@PathVariable("shopId") Long shopId){
    //     List<GoodsAndMachine> goodsAndMachineList = new ArrayList<>();
    //     LambdaQueryWrapper<ShopToGoods> wrapper = new LambdaQueryWrapper<>();
    //     wrapper.eq(ShopToGoods :: getStatus, 1).eq(ShopToGoods :: getIsGrounding, 1).eq(ShopToGoods :: getShopId, shopId);
    //     List<ShopToGoods> list = shopToGoodsService.list(wrapper);
    //     if (list.size() > 0){
    //         list.stream().forEach(item -> {
    //             Categorize categorize = categorizeService.getById(item.getCateId());
    //             GoodsAndMachine goodsAndMachine = new GoodsAndMachine();
    //             BeanUtils.copyProperties(item, goodsAndMachine);
    //             goodsAndMachine.setMachineNo(categorize.getMachineNo());
    //             goodsAndMachine.setAddBatch(item.getAddBatch());
    //             goodsAndMachineList.add(goodsAndMachine);
    //         });
    //
    //     }
    //     return R.ok().data("list", goodsAndMachineList);
    // }

    // /**
    //  * 查询当前店铺全部商品 包含分类
    //  */
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


    @DeleteMapping("remove/{goodsId}/{shopId}/{sizeId}/{sugarId}/{temperatureId}")
    public R deleteParamBatch(@PathVariable Long goodsId, @PathVariable Long shopId,@PathVariable Long sizeId,@PathVariable Long sugarId,@PathVariable Long temperatureId) {
        LambdaQueryWrapper<GoodsToBatch> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GoodsToBatch::getGoodsId, goodsId).eq(GoodsToBatch::getShopId, shopId).eq(GoodsToBatch :: getSizeId, sizeId).eq(GoodsToBatch :: getSugarId, sugarId).eq(GoodsToBatch :: getTemperatureId, temperatureId);
        boolean b = goodsToBatchService.remove(queryWrapper);
        if (b) {
            ShopToGoods shopToGoods = shopToGoodsService.getById(goodsId);
            shopToGoods.setAddBatch(2);
            shopToGoodsService.updateById(shopToGoods);
            Param sizeParam = paramService.getById(sizeId);
            Param sugarParam = paramService.getById(sugarId);
            Param temperatureParam = paramService.getById(temperatureId);
            Shop shop = shopService.getById(shopToGoods.getShopId());
            InterfaceLog interfaceLog = new InterfaceLog();
            interfaceLog.setTitle("商户删除产品配方");
            interfaceLog.setMethodName("");
            String content = "商户"+shop.getName()+"删除产品配方，商品名为"+shopToGoods.getName()+"（"+sizeParam.getName()+"-"+sugarParam.getName()+"-"+temperatureParam.getName()+"）";
            interfaceLog.setContent(content);
            interfaceLog.setShopId(shopToGoods.getShopId());
            interfaceLog.setTypeStatus(0);
            interfaceLog.setCreateTime(LocalDateTime.now());
            interfaceLog.setUpdateTime(LocalDateTime.now());
            interfaceLogService.save(interfaceLog);
            return R.ok();
        } else {
            return R.error();
        }

    }

}

