package com.tea.server.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tea.server.entity.BatchUse;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tea.server.entity.vo.BatchingVo;

import java.util.List;

/**
 * <p>
 * 店家商品与配方消耗对应关系表 服务类
 * </p>
 *
 * @author zyy
 * @since 2023-08-24
 */
public interface BatchUseService extends IService<BatchUse> {

    // 配料列表
    Page<BatchUse> pageList(Page<BatchUse> batchPage, Long shopId);

    // 根据店铺id获取当前库配方存 分页
    Page<BatchingVo> pageInventories(Page<BatchingVo> batchingVoPage, Long shopId);

    // 茶饮竖屏机器 获取配方列表
    List<BatchUse> getListByName(Long id, String keyword);
}
