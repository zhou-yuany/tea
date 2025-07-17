package com.tea.server.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tea.server.entity.Param;
import com.tea.server.entity.ShopToGoods;
import com.tea.server.entity.ShopToParam;
import com.tea.server.service.ShopToParamService;
import com.tea.server.utils.R;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 店铺与规格消耗对应关系表 前端控制器
 * </p>
 *
 * @author zyy
 * @since 2023-08-24
 */
@RestController
@RequestMapping("shopToParam")
public class ShopToParamController {

    @Autowired
    private ShopToParamService shopToParamService;

    /**
     * 总分类列表 分页
     */
    @PostMapping("getParamList/{page}/{limit}/{shopId}")
    public R getParamList(@PathVariable long page, @PathVariable long limit, @PathVariable Long shopId, @RequestParam String keyword) {
        // 创建page对象
        Page<ShopToParam> paramPage = new Page<>(page, limit);

        // 构建条件
        LambdaQueryWrapper<ShopToParam> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(ShopToParam::getStatus, 1).eq(ShopToParam::getShopId, shopId);
        if (null != keyword && !keyword.equals("")){
            wrapper.like(ShopToParam:: getName, keyword);
        }
        wrapper.orderByDesc(ShopToParam::getId);

        shopToParamService.page(paramPage, wrapper);

        List<ShopToParam> records = paramPage.getRecords();
        long total = paramPage.getTotal();
        return R.ok().data("total", total).data("rows", records);
    }

    @GetMapping("getParamInfo/{id}")
    public R getParamInfo(@PathVariable Long id) {
        ShopToParam info = shopToParamService.getById(id);
        return R.ok().data("info", info);
    }

    /**
     * 修改
     *
     * @return
     */
    @PostMapping("updateData/{id}")
    public R updateData(@PathVariable long id, @RequestBody ShopToParam obj) {
        ShopToParam shopToParam = shopToParamService.getById(id);
        shopToParam.setAddPrice(obj.getAddPrice());
        shopToParam.setUseNumber(obj.getUseNumber());
        shopToParam.setTotalCount(obj.getTotalCount());
        shopToParam.setUseCount(obj.getUseNumber());
        shopToParam.setUpdateTime(LocalDateTime.now());
        boolean save = shopToParamService.updateById(shopToParam);
        if (save) {
            return R.ok();
        } else {
            return R.error();
        }


    }

}

