package com.tea.server.service;

import com.tea.server.entity.Address;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 地址管理表 服务类
 * </p>
 *
 * @author testjava
 * @since 2024-09-04
 */
public interface AddressService extends IService<Address> {

    List<Address> getListByOpenid(String openid);

    Address addAddress(Address address);

    Integer updateAddress(Address address);
}
