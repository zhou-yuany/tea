package com.tea.server.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tea.server.entity.Admins;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tea.server.entity.Shop;

/**
 * <p>
 * 管理员表 服务类
 * </p>
 *
 * @author zyy
 * @since 2023-08-24
 */
public interface AdminsService extends IService<Admins> {

    // 帐号列表 分页
    Page<Admins> getList(Page<Admins> adminPage, Admins adminQuery);

}
