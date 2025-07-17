package com.tea.server.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tea.server.entity.BatchUse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tea.server.entity.vo.BatchingVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 店家商品与配方消耗对应关系表 Mapper 接口
 * </p>
 *
 * @author zyy
 * @since 2023-08-24
 */
public interface BatchUseMapper extends BaseMapper<BatchUse> {

    Page<BatchUse> pageList(@Param("batchPage") Page<BatchUse> batchPage, @Param("shopId") Long shopId);

    // 配方库存 分页
    Page<BatchingVo> pageInventories(@Param("batchingVoPage") Page<BatchingVo> batchingVoPage, @Param("shopId") Long shopId);

    List<BatchUse> getListByName(@Param("id") Long id, @Param("keyword") String keyword);
}
