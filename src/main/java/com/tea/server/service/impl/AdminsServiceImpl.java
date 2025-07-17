package com.tea.server.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tea.server.entity.Admins;
import com.tea.server.entity.Shop;
import com.tea.server.mapper.AdminsMapper;
import com.tea.server.service.AdminsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 管理员表 服务实现类
 * </p>
 *
 * @author zyy
 * @since 2023-08-24
 */
@Service
public class AdminsServiceImpl extends ServiceImpl<AdminsMapper, Admins> implements AdminsService {

    @Override
    public Page<Admins> getList(Page<Admins> adminPage, Admins adminQuery) {
        Page<Admins> list = baseMapper.getList(adminPage, adminQuery);
        return list;
    }

}
