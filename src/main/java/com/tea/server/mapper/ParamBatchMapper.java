package com.tea.server.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tea.server.entity.ParamBatch;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tea.server.entity.vo.ParamBatchVo;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 规格参数配方表 Mapper 接口
 * </p>
 *
 * @author zyy
 * @since 2023-08-24
 */
public interface ParamBatchMapper extends BaseMapper<ParamBatch> {
    Page<ParamBatchVo> pageList(@Param("paramBatchVoPage") Page<ParamBatchVo> paramBatchVoPage, @Param("shopId") Long shopId, @Param("keyword") String keyword);

}
