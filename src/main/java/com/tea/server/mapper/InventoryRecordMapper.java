package com.tea.server.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tea.server.entity.InventoryRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 配料出/入库记录表 Mapper 接口
 * </p>
 *
 * @author zyy
 * @since 2023-08-24
 */
public interface InventoryRecordMapper extends BaseMapper<InventoryRecord> {
    Page<InventoryRecord> pageInventoryRecords(@Param("inventory") Page<InventoryRecord> inventory, @Param("shopId") Long shopId, @Param("type") Integer type);

}
