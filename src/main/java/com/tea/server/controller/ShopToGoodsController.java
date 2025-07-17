package com.tea.server.controller;


import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tea.server.config.WeChatProperties;
import com.tea.server.entity.*;
import com.tea.server.entity.vo.CateAndGoods;
import com.tea.server.entity.vo.GoodsObj;
import com.tea.server.service.*;
import com.tea.server.utils.CoreUtil;
import com.tea.server.utils.R;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 店家与商品对应关系表 前端控制器
 * </p>
 *
 * @author zyy
 * @since 2023-08-24
 */
@RestController
@RequestMapping("shopToGoods")
public class ShopToGoodsController {

    @Autowired
    private ShopToGoodsService shopToGoodsService;

    @Autowired
    private WeChatProperties weChatProperties;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private ShopsService shopsService;

    @Autowired
    private GoodsToBatchService goodsToBatchService;

    @Autowired
    private GoodsAllBatchService goodsAllBatchService;

    // *************

    @Autowired
    private AdminsService adminsService;

    @Autowired
    private CategorizeService categorizeService;

    @Autowired
    private WeChatProperties wechatProperties;

    @Autowired
    private InterfaceLogService interfaceLogService;

    @GetMapping("getShopGoodsList/{shopId}")
    public R getShopGoodsList(@PathVariable Long shopId){
            LambdaQueryWrapper<ShopToGoods> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ShopToGoods :: getStatus, 1).eq(ShopToGoods :: getShopId, shopId);
        List<ShopToGoods> list = shopToGoodsService.list(wrapper);

        return R.ok().data("list", list);
    }

    /**
     * 下架商品
     */
    @PostMapping("clearGoods")
    public R clearGoods(@RequestBody GoodsObj goodsObj){
        List<Long> ids = goodsObj.getIds();
        LambdaQueryWrapper<ShopToGoods> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShopToGoods :: getStatus, 1).in(ShopToGoods :: getGoodsId, ids);
        List<ShopToGoods> list = shopToGoodsService.list(queryWrapper);

        Shop shop = shopsService.getById(list.get(0).getShopId());
        list.stream().forEach(item -> {
            ShopToGoods shopToGoods = shopToGoodsService.getById(item.getId());
            shopToGoods.setIsGrounding(2);
            shopToGoodsService.updateById(shopToGoods);


            InterfaceLog interfaceLog = new InterfaceLog();
            interfaceLog.setTitle("商户下架商品");
            interfaceLog.setMethodName("clearGoods");
            String content = "商户"+shop.getName()+"下架商品，商品名为"+shopToGoods.getName();
            interfaceLog.setContent(content);
            interfaceLog.setShopId(item.getShopId());
            interfaceLog.setTypeStatus(0);
            interfaceLog.setCreateTime(LocalDateTime.now());
            interfaceLog.setUpdateTime(LocalDateTime.now());
            interfaceLogService.save(interfaceLog);
        });

        return R.ok();
    }

    /**
     * 上架商品
     */
    @PostMapping("groundingGoods/{id}")
    public R groundingGoods(@PathVariable Long id){
        ShopToGoods shopToGoods = shopToGoodsService.getById(id);
        shopToGoods.setIsGrounding(1);
        boolean b = shopToGoodsService.updateById(shopToGoods);
        if (b) {
            Shop shop = shopsService.getById(shopToGoods.getShopId());
            InterfaceLog interfaceLog = new InterfaceLog();
            interfaceLog.setTitle("商户上架商品");
            interfaceLog.setMethodName("groundingGoods");
            String content = "商户"+shop.getName()+"上架商品，商品名为"+shopToGoods.getName();
            interfaceLog.setContent(content);
            interfaceLog.setShopId(shopToGoods.getShopId());
            interfaceLog.setTypeStatus(0);
            interfaceLog.setCreateTime(LocalDateTime.now());
            interfaceLog.setUpdateTime(LocalDateTime.now());
            interfaceLogService.save(interfaceLog);
            return R.ok();
        }else {
            return R.error();
        }

    }

    @PostMapping("uploadDetails")
    public R uploadDetails(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        Path relativePath = Paths.get("detailsUrl"
                , DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now())
                , CoreUtil.shortUUID() + fileName.substring(fileName.indexOf('.')));
        File outFile = Paths.get(wechatProperties.getUploadPath(), relativePath.toString()).toFile();
        if (!FileUtil.exist(outFile)) {
            outFile.getParentFile().mkdirs();
        }
        try {
            file.transferTo(outFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.ok().data("url", relativePath.toString().replace('\\', '/'));
    }


    /**
     * 茶饮机器端 查询商品（带分类）
     */
    @GetMapping("getCateAndGoods/{adminId}")
    public R getCateAndGoods(@PathVariable Long adminId){
        List<CateAndGoods> list = new ArrayList<>();
        LambdaQueryWrapper<Shop> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Shop::getStatus, 1).in(Shop::getAdminId, adminId);
        Shop one = shopsService.getOne(queryWrapper);
        LambdaQueryWrapper<ShopToGoods> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShopToGoods :: getStatus, 1).eq(ShopToGoods :: getShopId, one.getId());
        List<ShopToGoods> shopToGoodsList = shopToGoodsService.list(wrapper);
        if (null != shopToGoodsList && shopToGoodsList.size() > 0){
            CateAndGoods cateAndGoods = new CateAndGoods();
            cateAndGoods.setId(0L);
            cateAndGoods.setName("全部");
            cateAndGoods.setChildren(shopToGoodsList);
            list.add(cateAndGoods);
        }
        LambdaQueryWrapper<Categorize> wrapper2 = new LambdaQueryWrapper<>();
        wrapper2.eq(Categorize :: getStatus, 1).eq(Categorize :: getShopId, one.getId());
        List<Categorize> list1 = categorizeService.list(wrapper2);
        if (null != list1 && list1.size() > 0){
            list1.stream().forEach(item -> {
                CateAndGoods cateAndGoods2 = new CateAndGoods();
                Long cateId = item.getId();
                Categorize categorize = categorizeService.getById(cateId);
                cateAndGoods2.setId(cateId);
                cateAndGoods2.setName(categorize.getName());
                LambdaQueryWrapper<ShopToGoods> wrapper3 = new LambdaQueryWrapper<>();
                wrapper3.eq(ShopToGoods :: getStatus, 1).eq(ShopToGoods :: getShopId, one.getId()).eq(ShopToGoods :: getCateId, cateId);
                List<ShopToGoods> goodsList = shopToGoodsService.list(wrapper3);
                cateAndGoods2.setChildren(goodsList);
                list.add(cateAndGoods2);
            });
        }
        return R.ok().data("list", list);
    }


    // ***************


    /**
     * 查询授权商家的全部商品
     * @return
     */
    @GetMapping("allList/{shopId}")
    public R allList(@PathVariable Long shopId){
        Shop shop = shopsService.getById(shopId);
        String goodRange = shop.getGoodRange();
        List<Goods> list = new ArrayList<>();
        if (null != goodRange && !goodRange.equals("")){
            List<Long> goodsIds = Arrays.asList(goodRange.split(",")).stream().map(Long::valueOf).collect(Collectors.toList());
            LambdaQueryWrapper<Goods> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Goods :: getStatus, 1).in(Goods :: getId, goodsIds).eq(Goods :: getIsGrounding, 1);
            list = goodsService.list(wrapper);
        }
        return R.ok().data("list", list);
    }

    /**
     * 上传封面
     * @param file
     * @return
     */
    @PostMapping("upload")
    public R upload(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        String hashString = "";
                Path relativePath = Paths.get("goods"
                , DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now())
                , CoreUtil.shortUUID() + fileName.substring(fileName.indexOf('.')));
        File outFile = Paths.get(weChatProperties.getUploadPath(), relativePath.toString()).toFile();
        if (!FileUtil.exist(outFile)) {
            outFile.getParentFile().mkdirs();
        }
        try {
            file.transferTo(outFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return R.ok().data("url", relativePath.toString().replace('\\', '/'));
    }

    // 商品列表
    @PostMapping("getGoodsList/{page}/{limit}/{shopId}")
    public R getGoodsList(@PathVariable long page, @PathVariable long limit, @PathVariable Long shopId, @RequestParam String keyword){
        // 创建page对象
        Page<ShopToGoods> goodsPage = new Page<>(page, limit);

        // 构建条件
        LambdaQueryWrapper<ShopToGoods> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(ShopToGoods :: getStatus, 1).eq(ShopToGoods :: getShopId, shopId);

        if (null != keyword && !keyword.equals("")){
            wrapper.like(ShopToGoods :: getName, keyword);
        }
        wrapper.orderByDesc(ShopToGoods :: getId);
        shopToGoodsService.page(goodsPage, wrapper);

        long total = goodsPage.getTotal();
        List<ShopToGoods> records = goodsPage.getRecords();
        return R.ok().data("total", total).data("rows", records);
    }

    @GetMapping("getGoodInfo/{id}")
    public R getGoodInfo(@PathVariable Long id){
        ShopToGoods info = shopToGoodsService.getById(id);
        return R.ok().data("info", info);
    }

    /**
     * 添加
     * @return
     */
    @PostMapping("insertData")
    public R insertData(@RequestBody ShopToGoods obj){
        ShopToGoods shopToGoods = new ShopToGoods();
        LambdaQueryWrapper<ShopToGoods> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShopToGoods :: getStatus, 1).eq(ShopToGoods :: getGoodsId, obj.getGoodsId()).eq(ShopToGoods :: getShopId, obj.getShopId());
        List<ShopToGoods> list = shopToGoodsService.list(wrapper);
        if (null != list && list.size() > 0) {
            return R.ok().message("该商品已添加");
        }else {
            BeanUtils.copyProperties(obj, shopToGoods);
            shopToGoods.setCreateTime(LocalDateTime.now());
            shopToGoods.setUpdateTime(LocalDateTime.now());
            boolean save = shopToGoodsService.save(shopToGoods);
            if (save) {
                // 添加配料
                final Integer[] count = {0};
                LambdaQueryWrapper<GoodsAllBatch> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(GoodsAllBatch :: getStatus, 1).eq(GoodsAllBatch :: getGoodsId, obj.getGoodsId());
                List<GoodsAllBatch> allBatches = goodsAllBatchService.list(queryWrapper);
                if (null != allBatches && allBatches.size() > 0){
                    allBatches.stream().forEach(item -> {
                        GoodsToBatch goodsToBatch = new GoodsToBatch();
                        goodsToBatch.setBatchId(item.getBatchId());
                        goodsToBatch.setUseNumber(item.getUseNumber());
                        goodsToBatch.setCreateTime(LocalDateTime.now());
                        goodsToBatch.setUpdateTime(LocalDateTime.now());
                        goodsToBatch.setShopId(obj.getShopId());
                        goodsToBatch.setGoodsId(shopToGoods.getId());
                        goodsToBatch.setSizeId(item.getSizeId());
                        goodsToBatch.setSugarId(item.getSugarId());
                        goodsToBatch.setTemperatureId(item.getTemperatureId());
                        boolean b = goodsToBatchService.save(goodsToBatch);
                        if (b) {
                            count[0] = count[0] + 1;
                        }
                    });
                }

                if (count[0] == allBatches.size()) {
                    Shop shop = shopsService.getById(obj.getShopId());
                    InterfaceLog interfaceLog = new InterfaceLog();
                    interfaceLog.setTitle("商户添加商品");
                    interfaceLog.setMethodName("insertData");
                    String content = "商户"+shop.getName()+"添加商品，商品名为"+obj.getName();
                    interfaceLog.setContent(content);
                    interfaceLog.setShopId(obj.getShopId());
                    interfaceLog.setTypeStatus(0);
                    interfaceLog.setCreateTime(LocalDateTime.now());
                    interfaceLog.setUpdateTime(LocalDateTime.now());
                    interfaceLogService.save(interfaceLog);
                    return R.ok();
                }else {
                    return R.error();
                }

            }else {
                return R.error();
            }
        }

    }
    /**
     * 修改
     * @return
     */
    @PostMapping("updateData/{id}")
    public R updateData(@PathVariable long id, @RequestBody ShopToGoods obj){
        ShopToGoods shopToGoods = shopToGoodsService.getById(id);
        LambdaQueryWrapper<ShopToGoods> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShopToGoods :: getStatus, 1).eq(ShopToGoods :: getShopId, obj.getShopId()).eq(ShopToGoods :: getGoodsId, obj.getGoodsId()).ne(ShopToGoods :: getId, id);
        List<ShopToGoods> list = shopToGoodsService.list(wrapper);
        if (null != list && list.size() > 0) {
            return R.ok().message("该商品已添加");
        }else {
            shopToGoods.setName(obj.getName());
            shopToGoods.setCost(obj.getCost());
            shopToGoods.setCateId(obj.getCateId());
            shopToGoods.setIntroduce(obj.getIntroduce());
            shopToGoods.setIsGrounding(obj.getIsGrounding());
            shopToGoods.setTips(obj.getTips());
            shopToGoods.setTotalCount(obj.getTotalCount());
            shopToGoods.setUrl(obj.getUrl());
            shopToGoods.setEquipmentId(obj.getEquipmentId());
            shopToGoods.setPrice(obj.getPrice());
            shopToGoods.setUseCount(obj.getUseCount());
            shopToGoods.setUpdateTime(LocalDateTime.now());
            boolean save = shopToGoodsService.updateById(shopToGoods);
            if (save) {
                Shop shop = shopsService.getById(obj.getShopId());
                InterfaceLog interfaceLog = new InterfaceLog();
                interfaceLog.setTitle("商户修改商品");
                interfaceLog.setMethodName("updateData");
                String content = "商户"+shop.getName()+"修改商品，商品名为"+obj.getName();
                interfaceLog.setContent(content);
                interfaceLog.setShopId(obj.getShopId());
                interfaceLog.setTypeStatus(0);
                interfaceLog.setCreateTime(LocalDateTime.now());
                interfaceLog.setUpdateTime(LocalDateTime.now());
                interfaceLogService.save(interfaceLog);
                return R.ok();
            }else {
                return R.error();
            }
        }

    }


    @DeleteMapping("{id}")
    public R remove(@PathVariable("id") Long id) {
        ShopToGoods byId = shopToGoodsService.getById(id);
        boolean b = shopToGoodsService.removeById(id);
        if (b) {
            Shop shop = shopsService.getById(byId.getShopId());
            InterfaceLog interfaceLog = new InterfaceLog();
            interfaceLog.setTitle("商户删除商品");
            interfaceLog.setMethodName("");
            String content = "商户"+shop.getName()+"删除商品，商品名为"+byId.getName();
            interfaceLog.setContent(content);
            interfaceLog.setShopId(byId.getShopId());
            interfaceLog.setTypeStatus(0);
            interfaceLog.setCreateTime(LocalDateTime.now());
            interfaceLog.setUpdateTime(LocalDateTime.now());
            interfaceLogService.save(interfaceLog);
            return R.ok();
        }else {
            return R.error();
        }
    }

}

