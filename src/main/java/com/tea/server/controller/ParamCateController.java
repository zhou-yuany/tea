package com.tea.server.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tea.server.entity.Categorize;
import com.tea.server.entity.CategorizeAll;
import com.tea.server.entity.ParamCate;
import com.tea.server.service.ParamCateService;
import com.tea.server.utils.R;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 商品规格分类表 前端控制器
 * </p>
 *
 * @author zyy
 * @since 2023-08-24
 */
@RestController
@RequestMapping("paramCate")
public class ParamCateController {

    @Autowired
    private ParamCateService paramCateService;

    /**
     * 所有规格分类
     */
    @GetMapping("allList")
    public R allList(){

        List<ParamCate> list = paramCateService.list(null);
        return R.ok().data("list", list);
    }

    /**
     * 总分类列表 分页
     */
    @PostMapping("getParamCateList/{page}/{limit}")
    public R getParamCateList(@PathVariable long page, @PathVariable long limit, @RequestParam String keyword){
        // 创建page对象
        Page<ParamCate> paramCatePage = new Page<>(page, limit);

        // 构建条件
        LambdaQueryWrapper<ParamCate> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(ParamCate :: getStatus, 1);

        if (null != keyword && !keyword.equals("")){
            wrapper.like(ParamCate :: getName, keyword);
        }
        wrapper.orderByDesc(ParamCate :: getId);

        paramCateService.page(paramCatePage, wrapper);

        List<ParamCate> records = paramCatePage.getRecords();
        long total = paramCatePage.getTotal();
        return R.ok().data("total", total).data("rows", records);
    }

    @GetMapping("getParamCateInfo/{id}")
    public R getParamCateInfo(@PathVariable Long id){
        ParamCate info = paramCateService.getById(id);
        return R.ok().data("info", info);
    }

    /**
     * 添加
     * @return
     */
    @PostMapping("insertData")
    public R insertData(@RequestBody ParamCate obj){
        ParamCate paramCate = new ParamCate();
        LambdaQueryWrapper<ParamCate> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ParamCate :: getStatus, 1).eq(ParamCate :: getName, obj.getName());
        List<ParamCate> list = paramCateService.list(wrapper);
        if (null != list && list.size() > 0) {
            return R.ok().message("该分类已添加");
        }else {
            BeanUtils.copyProperties(obj, paramCate);
            paramCate.setCreateTime(LocalDateTime.now());
            paramCate.setUpdateTime(LocalDateTime.now());
            boolean save = paramCateService.save(paramCate);
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
    public R updateData(@PathVariable long id, @RequestBody ParamCate obj){
        ParamCate paramCate = paramCateService.getById(id);
        LambdaQueryWrapper<ParamCate> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ParamCate :: getStatus, 1).eq(ParamCate :: getName, obj.getName()).ne(ParamCate :: getId, paramCate.getId());
        List<ParamCate> list = paramCateService.list(wrapper);
        if (null != list && list.size() > 0) {
            return R.ok().message("该分类已添加");
        }else {
            paramCate.setName(obj.getName());
            paramCate.setUpdateTime(LocalDateTime.now());
            boolean save = paramCateService.updateById(paramCate);
            if (save) {
                return R.ok();
            }else {
                return R.error();
            }
        }

    }


    @DeleteMapping("{id}")
    public R remove(@PathVariable("id") Long id) {
        boolean b = paramCateService.removeById(id);
        if (b) {
            return R.ok();
        }else {
            return R.error();
        }
    }
}

