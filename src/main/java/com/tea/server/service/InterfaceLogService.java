package com.tea.server.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tea.server.entity.InterfaceLog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tea.server.entity.query.InterfaceLogQuery;

/**
 * <p>
 * 请求接口日志 服务类
 * </p>
 *
 * @author zyy
 * @since 2024-05-16
 */
public interface InterfaceLogService extends IService<InterfaceLog> {

    Page<InterfaceLog> pageInterfaceLogList(Page<InterfaceLog> interfaceLogPage, InterfaceLogQuery interfaceLogQuery);

    Page<InterfaceLog> pageShopInterfaceLogList(Page<InterfaceLog> interfaceLogPage, InterfaceLogQuery interfaceLogQuery, Long shopId);
}
