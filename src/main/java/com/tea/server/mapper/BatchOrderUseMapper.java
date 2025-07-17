package com.tea.server.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tea.server.entity.BatchOrderUse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tea.server.entity.query.BatchUseQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 物料消耗-订单表 Mapper 接口
 * </p>
 *
 * @author zyy
 * @since 2024-05-24
 */
public interface BatchOrderUseMapper extends BaseMapper<BatchOrderUse> {

    List<BatchOrderUse> shopBatchUseList(@Param("adminId") Long adminId, @Param("batchUseQuery") BatchUseQuery batchUseQuery);

    List<BatchOrderUse> batchUseList(@Param("batchUseQuery") BatchUseQuery batchUseQuery);


    Page<BatchOrderUse> pageBatchUseList(@Param("batchListPage") Page<BatchOrderUse> batchListPage, @Param("batchUseQuery")BatchUseQuery batchUseQuery);
}
