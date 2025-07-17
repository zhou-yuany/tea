package com.tea.server.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tea.server.entity.Param;
import com.tea.server.entity.ParamBatch;
import com.tea.server.entity.ParamCate;
import com.tea.server.entity.vo.ParamBatchVo;
import com.tea.server.service.ParamBatchService;
import com.tea.server.service.ParamCateService;
import com.tea.server.service.ParamService;
import com.tea.server.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 规格参数配方表 前端控制器
 * </p>
 *
 * @author zyy
 * @since 2023-08-24
 */
@RestController
@RequestMapping("paramBatch")
public class ParamBatchController {

    @Autowired
    private ParamBatchService paramBatchService;

    @Autowired
    private ParamCateService paramCateService;

    @Autowired
    private ParamService paramService;

    /**
     * 规格参数配比详情
     */
    @GetMapping("getParamBatchInfo/{id}")
    public R getParamBatchInfo(@PathVariable("id") Long id){
        ParamBatch paramBatch = paramBatchService.getById(id);
        return R.ok().data("info", paramBatch);
    }

    /**
     * 添加规格参数配比
     */
    @PostMapping("addParamBatch")
    public R addParamBatch(@RequestBody ParamBatch paramBatch){
        Integer count = paramBatchService.addParamBatch(paramBatch);
        if (count > 0){
            return R.ok();
        }else {
            return R.error();
        }
    }

    /**
     * 修改规格参数配比
     */
    @PostMapping("updateParamBatch")
    public R updateParamBatch(@RequestBody ParamBatch paramBatch){
        Integer count = paramBatchService.updateParamBatch(paramBatch);
        if (count > 0){
            return R.ok();
        }else {
            return R.error();
        }
    }

    /**
     * 规格参数配方 分页
     */
    @PostMapping("getParamBatchList/{page}/{limit}/{shopId}")
    public R getParamBatchList(@PathVariable long page, @PathVariable long limit, @PathVariable long shopId, @RequestParam String keyword){
        // 创建page对象
        Page<ParamBatchVo> paramBatchVoPage = new Page<>(page, limit);

        Page<ParamBatchVo> list = paramBatchService.pageList(paramBatchVoPage, shopId, keyword);

        List<ParamBatchVo> records = list.getRecords();
        long total = list.getTotal();
        return R.ok().data("total", total).data("rows", records);
    }

    @DeleteMapping("{id}")
    public R deleteParamBatch(@PathVariable Long id){
        boolean b = paramBatchService.removeById(id);
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

