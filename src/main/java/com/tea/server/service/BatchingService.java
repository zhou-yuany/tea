package com.tea.server.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tea.server.entity.BatchNumber;
import com.tea.server.entity.Batching;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 商品配料表 服务类
 * </p>
 *
 * @author zyy
 * @since 2023-08-24
 */
public interface BatchingService extends IService<Batching> {

    // 配料列表
    Page<Batching> getBatchList(Page<Batching> batchingPage, String keyword);

    // 当前店铺配料的所有品牌
    List<Batching> getBatchBrands(Long shopId);

    // 当前店铺配料的所有配料名称
    List<Batching> getBatchNames(Long shopId);

    // 当前店铺所选配料的所有配料规格
    List<BatchNumber> getBatchNumbers(Long shopId);
}
