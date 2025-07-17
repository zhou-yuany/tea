package com.tea.server.controller;


import cn.hutool.crypto.digest.MD5;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tea.server.entity.Brand;
import com.tea.server.entity.CategorizeAll;
import com.tea.server.entity.Goods;
import com.tea.server.entity.Shop;
import com.tea.server.entity.query.ShopQuery;
import com.tea.server.service.CategorizeAllService;
import com.tea.server.utils.R;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 总的商品分类表 前端控制器
 * </p>
 *
 * @author zyy
 * @since 2024-04-02
 */
@RestController
@RequestMapping("categorizeAll")
public class CategorizeAllController {
    @Autowired
    private CategorizeAllService categorizeAllService;

    /**
     * 所有总分类
     */
    @GetMapping("allList")
    public R allList(){
        List<CategorizeAll> list = categorizeAllService.list(null);
        return R.ok().data("list", list);
    }

    /**
     * 总分类列表 分页
     */
    @PostMapping("getCateAllList/{page}/{limit}")
    public R getCateAllList(@PathVariable long page, @PathVariable long limit, @RequestParam String keyword){
        // 创建page对象
        Page<CategorizeAll> categorizeAllPage = new Page<>(page, limit);

        // 构建条件
        LambdaQueryWrapper<CategorizeAll> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(CategorizeAll :: getStatus, 1);

        if (null != keyword && !keyword.equals("")){
            wrapper.like(CategorizeAll :: getName, keyword).or().like(CategorizeAll :: getRealName, keyword );
        }
        wrapper.orderByDesc(CategorizeAll :: getId);

        categorizeAllService.page(categorizeAllPage, wrapper);

        List<CategorizeAll> records = categorizeAllPage.getRecords();
        long total = categorizeAllPage.getTotal();
        return R.ok().data("total", total).data("rows", records);
    }

    @GetMapping("getCateAllInfo/{id}")
    public R getCateAllInfo(@PathVariable Long id){
        CategorizeAll categorizeAll = categorizeAllService.getById(id);
        return R.ok().data("info", categorizeAll);
    }

    /**
     * 添加
     * @return
     */
    @PostMapping("insertData")
    public R insertData(@RequestBody CategorizeAll obj){
        CategorizeAll categorizeAll = new CategorizeAll();
        LambdaQueryWrapper<CategorizeAll> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CategorizeAll :: getStatus, 1).eq(CategorizeAll :: getName, obj.getName()).eq(CategorizeAll :: getRealName, obj.getRealName());
        List<CategorizeAll> list = categorizeAllService.list(wrapper);
        if (null != list && list.size() > 0) {
            return R.ok().message("该分类已添加");
        }else {
            BeanUtils.copyProperties(obj, categorizeAll);
            categorizeAll.setMachineNo(obj.getMachineNo());
            categorizeAll.setCreateTime(LocalDateTime.now());
            categorizeAll.setUpdateTime(LocalDateTime.now());
            boolean save = categorizeAllService.save(categorizeAll);
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
    public R updateData(@PathVariable long id, @RequestBody CategorizeAll obj){
        CategorizeAll categorizeAll = categorizeAllService.getById(id);
        LambdaQueryWrapper<CategorizeAll> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CategorizeAll :: getStatus, 1).eq(CategorizeAll :: getName, obj.getName()).eq(CategorizeAll :: getRealName, obj.getRealName()).ne(CategorizeAll :: getId, categorizeAll.getId());
        List<CategorizeAll> list = categorizeAllService.list(wrapper);
        if (null != list && list.size() > 0) {
            return R.ok().message("该分类已添加");
        }else {
            categorizeAll.setRealName(obj.getRealName());
            categorizeAll.setName(obj.getName());
            categorizeAll.setMachineNo(obj.getMachineNo());
            categorizeAll.setUpdateTime(LocalDateTime.now());
            boolean save = categorizeAllService.updateById(categorizeAll);
            if (save) {
                return R.ok();
            }else {
                return R.error();
            }
        }

    }


    @DeleteMapping("{id}")
    public R remove(@PathVariable("id") Long id) {
        boolean b = categorizeAllService.removeById(id);
        if (b) {
            return R.ok();
        }else {
            return R.error();
        }
    }

}

