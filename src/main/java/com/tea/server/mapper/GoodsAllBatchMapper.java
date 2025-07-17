package com.tea.server.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tea.server.entity.GoodsAllBatch;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tea.server.entity.vo.GoodsAllBatchList;
import com.tea.server.entity.vo.GoodsBatchList;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 商品与配料对应关系表 Mapper 接口
 * </p>
 *
 * @author zyy
 * @since 2024-04-03
 */
public interface GoodsAllBatchMapper extends BaseMapper<GoodsAllBatch> {

    Page<GoodsAllBatchList> getGoodsAllBatchingList(@Param("goodsBatchListPage") Page<GoodsAllBatchList> goodsBatchListPage, @Param("keyword") String keyword);
}
