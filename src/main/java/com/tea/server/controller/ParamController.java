package com.tea.server.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tea.server.entity.Param;
import com.tea.server.entity.ParamCate;
import com.tea.server.entity.Shop;
import com.tea.server.entity.ShopToParam;
import com.tea.server.service.AdminsService;
import com.tea.server.service.ParamService;
import com.tea.server.service.ShopToParamService;
import com.tea.server.service.ShopsService;
import com.tea.server.utils.R;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 商品规格表 前端控制器
 * </p>
 *
 * @author zyy
 * @since 2023-08-24
 */
@RestController
@RequestMapping("param")
public class ParamController {

    @Autowired
    private ParamService paramService;

    // *********

    @Autowired
    private ShopToParamService shopToParamService;

    @Autowired
    private AdminsService adminsService;

    @Autowired
    private ShopsService shopService;

    // 查询全部杯型
    @GetMapping("getCupSizes")
    public R getCupSizes(){
        // LambdaQueryWrapper<ShopToParam> wrapper = new LambdaQueryWrapper<>();
        // wrapper.eq(ShopToParam :: getStatus, 1).like(ShopToParam :: getShopId, shopId);
        // List<ShopToParam> list = shopToParamService.list(wrapper);
        // List<Long> paramIds = list.stream().map(item -> item.getParamId()).collect(Collectors.toList());
        //
        // LambdaQueryWrapper<Param> queryWrapper = new LambdaQueryWrapper<>();
        // queryWrapper.eq(Param :: getStatus, 1).in(Param :: getId, paramIds).like(Param :: getName, "杯");
        // List<Param> paramList = paramService.list(queryWrapper);

        LambdaQueryWrapper<Param> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Param :: getStatus, 1).eq(Param :: getType, 1);
        List<Param> list = paramService.list(wrapper);
        return R.ok().data("paramList", list);
    }

    // 查询商家全部杯型
    @GetMapping("getCupShaped/{shopId}")
    public R getCupShaped(@PathVariable("shopId") Long shopId){
        // LambdaQueryWrapper<ShopToParam> wrapper = new LambdaQueryWrapper<>();
        // wrapper.eq(ShopToParam :: getStatus, 1).like(ShopToParam :: getShopId, shopId);
        // List<ShopToParam> list = shopToParamService.list(wrapper);
        // List<Long> paramIds = list.stream().map(item -> item.getParamId()).collect(Collectors.toList());
        //
        // LambdaQueryWrapper<Param> queryWrapper = new LambdaQueryWrapper<>();
        // queryWrapper.eq(Param :: getStatus, 1).in(Param :: getId, paramIds).like(Param :: getName, "杯");
        // List<Param> paramList = paramService.list(queryWrapper);

        LambdaQueryWrapper<ShopToParam> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShopToParam :: getStatus, 1).like(ShopToParam :: getShopId, shopId).eq(ShopToParam :: getType, 1);
        List<ShopToParam> list = shopToParamService.list(wrapper);
        return R.ok().data("paramList", list);
    }

    // 查询规格 温度 糖度
    @GetMapping("getStandards/{adminId}")
    public R getStandards(@PathVariable("adminId") Long adminId){

        LambdaQueryWrapper<Shop> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Shop::getStatus, 1).in(Shop::getAdminId, adminId);
        Shop one = shopService.getOne(queryWrapper);
        final Long shopId = one.getId();

        LambdaQueryWrapper<ShopToParam> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShopToParam :: getStatus, 1).like(ShopToParam :: getShopId, shopId).eq(ShopToParam :: getType, 1);
        List<ShopToParam> list = shopToParamService.list(wrapper);

        LambdaQueryWrapper<ShopToParam> wrapper2 = new LambdaQueryWrapper<>();
        wrapper2.eq(ShopToParam :: getStatus, 1).like(ShopToParam :: getShopId, shopId).eq(ShopToParam :: getType, 2);
        List<ShopToParam> list2 = shopToParamService.list(wrapper2);

        LambdaQueryWrapper<ShopToParam> wrapper3 = new LambdaQueryWrapper<>();
        wrapper3.eq(ShopToParam :: getStatus, 1).like(ShopToParam :: getShopId, shopId).eq(ShopToParam :: getType, 3);
        List<ShopToParam> list3 = shopToParamService.list(wrapper3);

        return R.ok().data("guige", list).data("sweet", list2).data("temperature", list3);

    }


    // **************


    // 查询全部杯型
    @GetMapping("getCupShaped")
    public R getCupShaped(){

        LambdaQueryWrapper<Param> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Param :: getStatus, 1).like(Param :: getType, 1);
        List<Param> paramList = paramService.list(queryWrapper);
        return R.ok().data("paramList", paramList);
    }

    /**
     * 总分类列表 分页
     */
    @PostMapping("getParamList/{page}/{limit}")
    public R getParamList(@PathVariable long page, @PathVariable long limit, @RequestParam String keyword){
        // 创建page对象
        Page<Param> paramPage = new Page<>(page, limit);

        // 构建条件
        LambdaQueryWrapper<Param> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(Param :: getStatus, 1);

        if (null != keyword && !keyword.equals("")){
            wrapper.like(Param :: getName, keyword);
        }
        wrapper.orderByDesc(Param :: getId);

        paramService.page(paramPage, wrapper);

        List<Param> records = paramPage.getRecords();
        long total = paramPage.getTotal();
        return R.ok().data("total", total).data("rows", records);
    }

    @GetMapping("getParamInfo/{id}")
    public R getParamInfo(@PathVariable Long id){
        Param info = paramService.getById(id);
        return R.ok().data("info", info);
    }

    /**
     * 添加
     * @return
     */
    @PostMapping("insertData")
    public R insertData(@RequestBody Param obj){
        Param param = new Param();
        LambdaQueryWrapper<Param> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Param :: getStatus, 1).eq(Param :: getName, obj.getName());
        List<Param> list = paramService.list(wrapper);
        if (null != list && list.size() > 0) {
            return R.ok().message("该规格已添加");
        }else {
            BeanUtils.copyProperties(obj, param);
            param.setCreateTime(LocalDateTime.now());
            param.setUpdateTime(LocalDateTime.now());
            boolean save = paramService.save(param);
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
    public R updateData(@PathVariable long id, @RequestBody Param obj){
        Param param = paramService.getById(id);
        LambdaQueryWrapper<Param> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Param :: getStatus, 1).eq(Param :: getName, obj.getName()).ne(Param :: getId, param.getId());
        List<Param> list = paramService.list(wrapper);
        if (null != list && list.size() > 0) {
            return R.ok().message("该规格已添加");
        }else {
            param.setParamCateId(obj.getParamCateId());
            param.setType(obj.getType());
            param.setAddPrice(obj.getAddPrice());
            param.setName(obj.getName());
            param.setUseNumber(obj.getUseNumber());
            param.setUpdateTime(LocalDateTime.now());
            boolean save = paramService.updateById(param);

            if (save) {
                LambdaQueryWrapper<ShopToParam> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(ShopToParam::getStatus, 1).eq(ShopToParam:: getParamId, id);
                List<ShopToParam> paramList = shopToParamService.list(queryWrapper);
                if(paramList.size() > 0){
                    paramList.stream().forEach(item -> {
                        item.setName(obj.getName());
                        shopToParamService.updateById(item);
                    });
                }

                return R.ok();
            }else {
                return R.error();
            }
        }

    }


    @DeleteMapping("{id}")
    public R remove(@PathVariable("id") Long id) {
        boolean b = paramService.removeById(id);
        if (b) {
            return R.ok();
        }else {
            return R.error();
        }
    }

}

