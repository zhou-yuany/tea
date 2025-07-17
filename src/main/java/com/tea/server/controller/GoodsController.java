package com.tea.server.controller;


import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tea.server.config.WeChatProperties;
import com.tea.server.entity.CategorizeAll;
import com.tea.server.entity.Goods;
import com.tea.server.entity.Param;
import com.tea.server.entity.ShopToGoods;
import com.tea.server.entity.vo.GoodsAllBatchList;
import com.tea.server.entity.vo.GoodsMachine;
import com.tea.server.entity.vo.ParamCateAndParam;
import com.tea.server.service.CategorizeAllService;
import com.tea.server.service.GoodsService;
import com.tea.server.service.ParamService;
import com.tea.server.service.ShopToGoodsService;
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
import java.util.List;

/**
 * <p>
 * 商品表 前端控制器
 * </p>
 *
 * @author zyy
 * @since 2023-08-24
 */
@RestController
@RequestMapping("goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private WeChatProperties wechatProperties;

    @Autowired
    private CategorizeAllService categorizeAllService;

    @Autowired
    private ParamService paramService;

    @Autowired
    private ShopToGoodsService shopToGoodsService;


    /**
     * 查询当前店铺全部商品
     */
    @GetMapping("goodsAll/{shopId}")
    public R goodsAll(@PathVariable("shopId") Long shopId){
        List<ShopToGoods> goodsList = shopToGoodsService.goodsAll(shopId);
        return R.ok().data("list", goodsList);
    }

    // 根据选择商品查询规格
    @GetMapping("getParamsByGoodsId/{goodsId}")
    public R getParamsByGoodsId(@PathVariable("goodsId") Long goodsId){
        ParamCateAndParam info = goodsService.getParamsByGoodsId(goodsId);
        return R.ok().data("info", info);
    }

    @GetMapping("allList")
    public R allList(){
        LambdaQueryWrapper<Goods> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Goods :: getStatus, 1).eq(Goods :: getIsGrounding, 1);
        List<Goods> list = goodsService.list(wrapper);
        List<GoodsMachine> newList = new ArrayList<>();
        if (null != list && list.size() > 0){
            list.stream().forEach(item ->{
                GoodsMachine goodsMachine = new GoodsMachine();
                BeanUtils.copyProperties(item, goodsMachine);
                CategorizeAll categorizeAll = categorizeAllService.getById(item.getCateId());
                goodsMachine.setMachineNo(categorizeAll.getMachineNo());
                newList.add(goodsMachine);
            });
        }

        LambdaQueryWrapper<Param> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Param :: getStatus, 1);
        List<Param> paramList = paramService.list(queryWrapper);
        return R.ok().data("list", newList).data("paramList", paramList);
    }

    /**
     * 上传封面
     * @param file
     * @return
     */
    @PostMapping("upload")
    public R upload(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        Path relativePath = Paths.get("goods"
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

    // 商品列表
    @PostMapping("getGoodsList/{page}/{limit}")
    public R getGoodsList(@PathVariable long page, @PathVariable long limit, @RequestParam String keyword){
        // 创建page对象
        Page<Goods> goodsPage = new Page<>(page, limit);

        // 构建条件
        LambdaQueryWrapper<Goods> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(Goods :: getStatus, 1);

        if (null != keyword && !keyword.equals("")){
            wrapper.like(Goods :: getName, keyword);
        }
        wrapper.orderByDesc(Goods :: getId);

        goodsService.page(goodsPage, wrapper);


        long total = goodsPage.getTotal();
        List<Goods> records = goodsPage.getRecords();
        return R.ok().data("total", total).data("rows", records);
    }

    @GetMapping("getGoodInfo/{id}")
    public R getGoodInfo(@PathVariable Long id){
        Goods goods = goodsService.getById(id);
        return R.ok().data("info", goods);
    }

    /**
     * 添加
     * @return
     */
    @PostMapping("insertData")
    public R insertData(@RequestBody Goods obj){
        Goods goods = new Goods();
        LambdaQueryWrapper<Goods> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Goods :: getStatus, 1).eq(Goods :: getName, obj.getName());
        List<Goods> list = goodsService.list(wrapper);
        if (null != list && list.size() > 0) {
            return R.ok().message("该商品已添加");
        }else {
            BeanUtils.copyProperties(obj, goods);
            goods.setCreateTime(LocalDateTime.now());
            goods.setUpdateTime(LocalDateTime.now());
            boolean save = goodsService.save(goods);
            if (save) {
                return R.ok();
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
    public R updateData(@PathVariable long id, @RequestBody Goods obj){
        Goods goods = goodsService.getById(id);
        LambdaQueryWrapper<Goods> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Goods :: getStatus, 1).eq(Goods :: getName, obj.getName()).ne(Goods :: getId, goods.getId());
        List<Goods> list = goodsService.list(wrapper);
        if (null != list && list.size() > 0) {
            return R.ok().message("该商品已添加");
        }else {
            goods.setName(obj.getName());
            goods.setCost(obj.getCost());
            goods.setCateId(obj.getCateId());
            goods.setHaveCount(obj.getHaveCount());
            goods.setIntroduce(obj.getIntroduce());
            goods.setIsGrounding(obj.getIsGrounding());
            goods.setTips(obj.getTips());
            goods.setTotalCount(obj.getTotalCount());
            goods.setUrl(obj.getUrl());
            goods.setPrice(obj.getPrice());
            goods.setUseCount(obj.getUseCount());
            goods.setUpdateTime(LocalDateTime.now());
            boolean save = goodsService.updateById(goods);
            if (save) {
                return R.ok();
            }else {
                return R.error();
            }
        }

    }


    @DeleteMapping("{id}")
    public R remove(@PathVariable("id") Long id) {
        boolean b = goodsService.removeById(id);
        if (b) {
            return R.ok();
        }else {
            return R.error();
        }
    }

}

