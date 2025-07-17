package com.tea.server.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tea.server.entity.InterfaceLog;
import com.tea.server.entity.query.InterfaceLogQuery;
import com.tea.server.mapper.InterfaceLogMapper;
import com.tea.server.service.InterfaceLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 请求接口日志 服务实现类
 * </p>
 *
 * @author zyy
 * @since 2024-05-16
 */
@Service
public class InterfaceLogServiceImpl extends ServiceImpl<InterfaceLogMapper, InterfaceLog> implements InterfaceLogService {

    @Override
    public Page<InterfaceLog> pageInterfaceLogList(Page<InterfaceLog> interfaceLogPage, InterfaceLogQuery interfaceLogQuery) {
        Page<InterfaceLog> list = baseMapper.pageInterfaceLogList(interfaceLogPage, interfaceLogQuery);
        return list;
    }

    @Override
    public Page<InterfaceLog> pageShopInterfaceLogList(Page<InterfaceLog> interfaceLogPage, InterfaceLogQuery interfaceLogQuery, Long shopId) {
        Page<InterfaceLog> list = baseMapper.pageShopInterfaceLogList(interfaceLogPage, interfaceLogQuery, shopId);
        return list;
    }
}
