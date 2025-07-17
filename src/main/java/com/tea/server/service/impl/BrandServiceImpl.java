package com.tea.server.service.impl;

import com.tea.server.entity.Brand;
import com.tea.server.mapper.BrandMapper;
import com.tea.server.service.BrandService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 品牌表 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2023-08-02
 */
@Service
public class BrandServiceImpl extends ServiceImpl<BrandMapper, Brand> implements BrandService {

}
