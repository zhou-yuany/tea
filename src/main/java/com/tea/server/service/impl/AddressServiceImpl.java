package com.tea.server.service.impl;

import com.tea.server.entity.Address;
import com.tea.server.mapper.AddressMapper;
import com.tea.server.service.AddressService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 地址管理表 服务实现类
 * </p>
 *
 * @author zyy
 * @since 2024-09-04
 */
@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements AddressService {

}
