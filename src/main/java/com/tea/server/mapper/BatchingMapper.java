package com.tea.server.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tea.server.entity.Batching;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 商品配料表 Mapper 接口
 * </p>
 *
 * @author zyy
 * @since 2023-08-24
 */
public interface BatchingMapper extends BaseMapper<Batching> {

    // 配料列表
    Page<Batching> getBatchList(@Param("batchingPage") Page<Batching> batchingPage, @Param("keyword") String keyword);
}
