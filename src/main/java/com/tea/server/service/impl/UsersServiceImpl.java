package com.tea.server.service.impl;

import com.tea.server.entity.Users;
import com.tea.server.mapper.UsersMapper;
import com.tea.server.service.UsersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2023-08-02
 */
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements UsersService {

    @Override
    public Boolean login(String openid) {
        Users users = new Users();
        users.setOpenid(openid);
        users.setCreateTime(LocalDateTime.now());
        users.setUpdateTime(LocalDateTime.now());
        int insert = baseMapper.insert(users);
        if (insert > 0) {
            return true;
        }else {
            return false;
        }
    }
}
