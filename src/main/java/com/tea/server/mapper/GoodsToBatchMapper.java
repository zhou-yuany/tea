package com.tea.server.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tea.server.entity.GoodsToBatch;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tea.server.entity.query.GoodsBatchQuery;
import com.tea.server.entity.vo.CostCardVo;
import com.tea.server.entity.vo.GoodsBatchList;
import com.tea.server.entity.vo.GoodsToBatchList;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 商品与配料对应关系表 Mapper 接口
 * </p>
 *
 * @author zyy
 * @since 2023-08-24
 */
public interface GoodsToBatchMapper extends BaseMapper<GoodsToBatch> {
    Page<CostCardVo> pageCostCards(@Param("costCardVoPage")Page<CostCardVo> costCardVoPage, @Param("shopId") Long shopId);

    Page<GoodsBatchList> getGoodsBatchingList1(@Param("goodsBatchListPage") Page<GoodsBatchList> goodsBatchListPage, @Param("shopId") Long shopId, @Param("keyword") String keyword);

    List<GoodsBatchList> getGoodsBatchings(@Param("shopId") Long id, @Param("keyword") String keyword);

    List<GoodsToBatchList> getGoodsAllBatchingList(@Param("shopId") Long shopId, @Param("goodsBatchQuery") GoodsBatchQuery goodsBatchQuery);


    Page<GoodsBatchList> getGoodsBatchingList(@Param("goodsBatchListPage") Page<GoodsBatchList> goodsBatchListPage, @Param("shopId") Long shopId, @Param("keyword") String keyword);
}
