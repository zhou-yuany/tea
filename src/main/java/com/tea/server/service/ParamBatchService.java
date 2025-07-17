package com.tea.server.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tea.server.entity.ParamBatch;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tea.server.entity.vo.ParamBatchVo;

/**
 * <p>
 * 规格参数配方表 服务类
 * </p>
 *
 * @author zyy
 * @since 2023-08-24
 */
public interface ParamBatchService extends IService<ParamBatch> {

    Integer addParamBatch(ParamBatch paramBatch);

    Integer updateParamBatch(ParamBatch paramBatch);

    Page<ParamBatchVo> pageList(Page<ParamBatchVo> paramBatchVoPage, Long shopId, String keyword);

}
