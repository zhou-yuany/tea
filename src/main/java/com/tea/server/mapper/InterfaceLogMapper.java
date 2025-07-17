package com.tea.server.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tea.server.entity.InterfaceLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tea.server.entity.query.InterfaceLogQuery;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 请求接口日志 Mapper 接口
 * </p>
 *
 * @author zyy
 * @since 2024-05-16
 */
public interface InterfaceLogMapper extends BaseMapper<InterfaceLog> {

    Page<InterfaceLog> pageInterfaceLogList(@Param("interfaceLogPage") Page<InterfaceLog> interfaceLogPage, @Param("interfaceLogQuery") InterfaceLogQuery interfaceLogQuery);

    Page<InterfaceLog> pageShopInterfaceLogList(@Param("interfaceLogPage") Page<InterfaceLog> interfaceLogPage, @Param("interfaceLogQuery") InterfaceLogQuery interfaceLogQuery, @Param("shopId") Long shopId);
}
