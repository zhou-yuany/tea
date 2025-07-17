package com.tea.server.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tea.server.entity.ParamAllBatch;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tea.server.entity.vo.ParamAllBatchVo;
import com.tea.server.entity.vo.ParamBatchVo;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 总规格参数配方表 Mapper 接口
 * </p>
 *
 * @author zyy
 * @since 2024-04-03
 */
public interface ParamAllBatchMapper extends BaseMapper<ParamAllBatch> {

    Page<ParamAllBatchVo> pageList(@Param("paramBatchVoPage") Page<ParamAllBatchVo> paramBatchVoPage, @Param("keyword") String keyword);
}
