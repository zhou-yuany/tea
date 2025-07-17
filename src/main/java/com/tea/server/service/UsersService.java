package com.tea.server.service;

import com.tea.server.entity.Users;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author testjava
 * @since 2023-08-02
 */
public interface UsersService extends IService<Users> {

    Boolean login(String openid);
}
