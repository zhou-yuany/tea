package com.tea.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tea.server.entity.Address;
import com.tea.server.mapper.AddressMapper;
import com.tea.server.service.AddressService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 地址管理表 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2024-09-04
 */
@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements AddressService {

    @Override
    public List<Address> getListByOpenid(String openid) {
        LambdaQueryWrapper<Address> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Address :: getStatus, 1).eq(Address :: getOpenid, openid).orderByDesc(Address :: getId);
        List<Address> addresses = baseMapper.selectList(wrapper);
        return addresses;
    }

    @Override
    public Address addAddress(Address address) {
        Address address1 = new Address();
        BeanUtils.copyProperties(address, address1);
        address1.setCreateTime(LocalDateTime.now());
        address1.setUpdateTime(LocalDateTime.now());
        baseMapper.insert(address1);
        return address1;
    }

    @Override
    public Integer updateAddress(Address address) {
        Address address1 = new Address();
        BeanUtils.copyProperties(address, address1);
        address1.setUpdateTime(LocalDateTime.now());
        int insert = baseMapper.updateById(address1);
        return insert;
    }
}
