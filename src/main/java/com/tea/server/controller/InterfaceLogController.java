package com.tea.server.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tea.server.entity.InterfaceLog;
import com.tea.server.entity.InventoryRecord;
import com.tea.server.entity.WithdrawalRecord;
import com.tea.server.entity.query.InterfaceLogQuery;
import com.tea.server.entity.vo.BatchingVo;
import com.tea.server.service.InterfaceLogService;
import com.tea.server.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 请求接口日志 前端控制器
 * </p>
 *
 * @author zyy
 * @since 2024-05-16
 */
@RestController
@RequestMapping("interfaceLog")
public class InterfaceLogController {
    @Autowired
    private InterfaceLogService interfaceLogService;

    /**
     * 总后台查看日志 记录 分页
     */
    @PostMapping("getInterfaceLogList/{page}/{limit}")
    public R getInterfaceLogList(@PathVariable long page, @PathVariable long limit, @RequestBody InterfaceLogQuery interfaceLogQuery){
        // 创建page对象
        Page<InterfaceLog> interfaceLogPage = new Page<>(page, limit);

        // 构建条件
        Page<InterfaceLog> pageList = interfaceLogService.pageInterfaceLogList(interfaceLogPage, interfaceLogQuery);

        long total = pageList.getTotal();
        List<InterfaceLog> records = pageList.getRecords();
        return R.ok().data("total", total).data("rows", records);
    }

    /**
     * 商户查看日志 分页
     */
    @PostMapping("getShopInterfaceLogList/{page}/{limit}/{shopId}")
    public R getShopInterfaceLogList(@PathVariable long page, @PathVariable long limit, @PathVariable Long shopId, @RequestBody InterfaceLogQuery interfaceLogQuery){
        // 创建page对象
        Page<InterfaceLog> interfaceLogPage = new Page<>(page, limit);

        // 构建条件
        Page<InterfaceLog> pageList = interfaceLogService.pageShopInterfaceLogList(interfaceLogPage, interfaceLogQuery, shopId);

        long total = pageList.getTotal();
        List<InterfaceLog> records = pageList.getRecords();
        return R.ok().data("total", total).data("rows", records);
    }

}

