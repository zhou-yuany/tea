package com.tea.server.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tea.server.entity.Batching;
import com.tea.server.entity.Brand;
import com.tea.server.entity.CategorizeAll;
import com.tea.server.entity.vo.GoodsBatchList;
import com.tea.server.service.BatchingService;
import com.tea.server.service.BrandService;
import com.tea.server.utils.R;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 商品配料表 前端控制器
 * </p>
 *
 * @author zyy
 * @since 2023-08-24
 */
@RestController
@RequestMapping("batching")
public class BatchingController {
    @Autowired
    private BatchingService batchingService;

    @Autowired
    private BrandService brandService;

    // 查询所有配料
    @GetMapping("getAllBatchs")
    public R getBatchList(){
        List<Batching> list = batchingService.list(null);
        return R.ok().data("batchList", list);
    }


    // 配料列表
    @PostMapping("getBatchList/{page}/{limit}")
    public R getBatchList(@PathVariable long page, @PathVariable long limit, @RequestParam String keyword){
        // 创建page对象
        Page<Batching> batchingPage = new Page<>(page, limit);

        // 构建条件
        Page<Batching> pageList = batchingService.getBatchList(batchingPage, keyword);

        long total = pageList.getTotal();
        List<Batching> records = pageList.getRecords();
        return R.ok().data("total", total).data("rows", records);
    }

    @GetMapping("getBatchInfo/{id}")
    public R getBatchInfo(@PathVariable Long id){
        Batching batching = batchingService.getById(id);
        return R.ok().data("info", batching);
    }

    /**
     * 添加
     * @return
     */
    @PostMapping("insertData")
    public R insertData(@RequestBody Batching obj){
        Batching batching = new Batching();
        LambdaQueryWrapper<Batching> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Batching :: getStatus, 1).eq(Batching :: getName, obj.getName()).eq(Batching :: getOutlet, obj.getOutlet());
        List<Batching> list = batchingService.list(wrapper);
        if (null != list && list.size() > 0) {
            return R.ok().message("该配料已添加");
        }else {
            BeanUtils.copyProperties(obj, batching);
            batching.setName(obj.getBrand()+"-"+obj.getName());
            batching.setCreateTime(LocalDateTime.now());
            batching.setUpdateTime(LocalDateTime.now());
            boolean save = batchingService.save(batching);
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
    public R updateData(@PathVariable long id, @RequestBody Batching obj){
        Batching batching = batchingService.getById(id);
        LambdaQueryWrapper<Batching> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Batching :: getStatus, 1).eq(Batching :: getName, obj.getName()).eq(Batching :: getOutlet, obj.getOutlet()).ne(Batching :: getId, batching.getId());
        List<Batching> list = batchingService.list(wrapper);
        if (null != list && list.size() > 0) {
            return R.ok().message("该配料已添加");
        }else {
            batching.setOutlet(obj.getOutlet());
            batching.setName(obj.getName());
            batching.setMachineNo(obj.getMachineNo());
            batching.setBrand(obj.getBrand());
            batching.setPrice(obj.getPrice());
            if(!(obj.getBrand()+"-"+obj.getName()).equals((batching.getBrand()+"-"+batching.getName()))){
                batching.setName(obj.getBrand()+"-"+obj.getName());
            }
            batching.setUpdateTime(LocalDateTime.now());
            boolean save = batchingService.updateById(batching);
            if (save) {
                return R.ok();
            }else {
                return R.error();
            }
        }

    }


    @DeleteMapping("{id}")
    public R remove(@PathVariable("id") Long id) {
        boolean b = batchingService.removeById(id);
        if (b) {
            return R.ok();
        }else {
            return R.error();
        }
    }


}

