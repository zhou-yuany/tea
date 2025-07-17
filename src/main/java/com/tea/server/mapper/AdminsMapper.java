package com.tea.server.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tea.server.entity.Admins;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tea.server.entity.Shop;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 管理员表 Mapper 接口
 * </p>
 *
 * @author zyy
 * @since 2023-08-24
 */
public interface AdminsMapper extends BaseMapper<Admins> {

    Page<Admins> getList(@Param("adminPage") Page<Admins> adminPage, @Param("adminQuery") Admins adminQuery);

}
