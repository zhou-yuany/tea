package com.tea.server.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tea.server.entity.ParamAllBatch;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tea.server.entity.vo.ParamAllBatchVo;

/**
 * <p>
 * 总规格参数配方表 服务类
 * </p>
 *
 * @author zyy
 * @since 2024-04-03
 */
public interface ParamAllBatchService extends IService<ParamAllBatch> {

    // 添加规格参数配比
    Integer addParamAllBatch(ParamAllBatch paramAllBatch);

    // 修改规格参数配比
    Integer updateParamBatch(ParamAllBatch paramAllBatch);

    // 规格参数配方 分页
    Page<ParamAllBatchVo> pageList(Page<ParamAllBatchVo> paramBatchVoPage, String keyword);
}
