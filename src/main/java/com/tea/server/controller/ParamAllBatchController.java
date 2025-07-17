package com.tea.server.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tea.server.entity.Param;
import com.tea.server.entity.ParamAllBatch;
import com.tea.server.entity.ParamBatch;
import com.tea.server.entity.ParamCate;
import com.tea.server.entity.vo.ParamAllBatchVo;
import com.tea.server.service.ParamAllBatchService;
import com.tea.server.service.ParamBatchService;
import com.tea.server.service.ParamCateService;
import com.tea.server.service.ParamService;
import com.tea.server.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 总规格参数配方表 前端控制器
 * </p>
 *
 * @author zyy
 * @since 2024-04-03
 */
@RestController
@RequestMapping("paramAllBatch")
public class ParamAllBatchController {
    @Autowired
    private ParamAllBatchService paramAllBatchService;

    @Autowired
    private ParamCateService paramCateService;

    @Autowired
    private ParamService paramService;

    /**
     * 规格参数配比详情
     */
    @GetMapping("getParamAllBatchInfo/{id}")
    public R getParamAllBatchInfo(@PathVariable("id") Long id){
        ParamAllBatch info = paramAllBatchService.getById(id);
        return R.ok().data("info", info);
    }

    /**
     * 添加规格参数配比
     */
    @PostMapping("addParamAllBatch")
    public R addParamAllBatch(@RequestBody ParamAllBatch paramAllBatch){
        Integer count = paramAllBatchService.addParamAllBatch(paramAllBatch);
        if (count > 0){
            return R.ok();
        }else {
            return R.error();
        }
    }

    /**
     * 修改规格参数配比
     */
    @PostMapping("updateParamAllBatch")
    public R updateParamAllBatch(@RequestBody ParamAllBatch paramAllBatch){
        Integer count = paramAllBatchService.updateParamBatch(paramAllBatch);
        if (count > 0){
            return R.ok();
        }else {
            return R.error();
        }
    }

    /**
     * 规格参数配方 分页
     */
    @PostMapping("getParamAllBatchList/{page}/{limit}")
    public R getParamAllBatchList(@PathVariable long page, @PathVariable long limit, @RequestParam String keyword){
        // 创建page对象
        Page<ParamAllBatchVo> paramBatchVoPage = new Page<>(page, limit);

        Page<ParamAllBatchVo> list = paramAllBatchService.pageList(paramBatchVoPage, keyword);

        List<ParamAllBatchVo> records = list.getRecords();
        long total = list.getTotal();
        return R.ok().data("total", total).data("rows", records);
    }

    @DeleteMapping("{id}")
    public R deleteParamBatch(@PathVariable Long id){
        boolean b = paramAllBatchService.removeById(id);
        if (b) {
            return R.ok();
        }else {
            return R.error();
        }

    }

    // 查询全部糖度\温度规格参数
    @GetMapping("getHeatAndSugarParams")
    public R getHeatAndSugarParams(){
        LambdaQueryWrapper<ParamCate> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ParamCate :: getStatus, 1).eq(ParamCate :: getName, "糖度");
        ParamCate one = paramCateService.getOne(queryWrapper);

        LambdaQueryWrapper<Param> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Param :: getStatus, 1).eq(Param :: getParamCateId, one.getId());
        List<Param> list = paramService.list(wrapper);

        LambdaQueryWrapper<ParamCate> heatWrapper = new LambdaQueryWrapper<>();
        heatWrapper.eq(ParamCate :: getStatus, 1).eq(ParamCate :: getName, "温度");
        ParamCate paramCate = paramCateService.getOne(heatWrapper);

        LambdaQueryWrapper<Param> heatQueryWrapper = new LambdaQueryWrapper<>();
        heatQueryWrapper.eq(Param :: getStatus, 1).eq(Param :: getParamCateId, paramCate.getId());
        List<Param> heatList = paramService.list(heatQueryWrapper);
        return R.ok().data("sugarList", list).data("heatList", heatList);
    }
}

