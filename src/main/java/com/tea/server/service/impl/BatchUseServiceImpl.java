package com.tea.server.service.impl;

import com.tea.server.entity.BatchUse;
import com.tea.server.mapper.BatchUseMapper;
import com.tea.server.service.BatchUseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 店家商品与配方消耗对应关系表 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2023-08-08
 */
@Service
public class BatchUseServiceImpl extends ServiceImpl<BatchUseMapper, BatchUse> implements BatchUseService {

}
