package com.tea.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tea.server.entity.BatchNumber;
import com.tea.server.entity.BatchUse;
import com.tea.server.entity.Batching;
import com.tea.server.entity.InventoryRecord;
import com.tea.server.mapper.BatchNumberMapper;
import com.tea.server.mapper.BatchUseMapper;
import com.tea.server.mapper.BatchingMapper;
import com.tea.server.mapper.InventoryRecordMapper;
import com.tea.server.service.InventoryRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * <p>
 * 配料出/入库记录表 服务实现类
 * </p>
 *
 * @author zyy
 * @since 2023-08-24
 */
@Service
public class InventoryRecordServiceImpl extends ServiceImpl<InventoryRecordMapper, InventoryRecord> implements InventoryRecordService {

    @Resource
    private BatchingMapper batchingMapper;

    @Resource
    private BatchUseMapper batchUseMapper;

    @Resource
    private BatchNumberMapper batchNumberMapper;

    @Override
    public Page<InventoryRecord> pageInventoryRecords(Page<InventoryRecord> inventory, Long shopId, Integer type) {
        Page<InventoryRecord> list = baseMapper.pageInventoryRecords(inventory, shopId, type);
        return list;
    }

    @Override
    public Integer insertInventory(InventoryRecord obj) {
        LambdaQueryWrapper<Batching> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Batching :: getBrand, obj.getBrand()).eq(Batching :: getStatus, 1).eq(Batching :: getName, obj.getBatchName());
        Batching batching1 = batchingMapper.selectOne(queryWrapper);
        InventoryRecord inventoryRecord = new InventoryRecord();
        inventoryRecord.setCreateTime(LocalDateTime.now());
        inventoryRecord.setUpdateTime(LocalDateTime.now());
        inventoryRecord.setType(1);
        inventoryRecord.setCount(obj.getCount());
        inventoryRecord.setShopId(obj.getShopId());
        inventoryRecord.setBrand(obj.getBrand());
        inventoryRecord.setBatchName(obj.getBatchName());
        inventoryRecord.setUseNumber(obj.getUseNumber());
        inventoryRecord.setUnit(obj.getUnit());
        if(null == batching1){
            Batching batching = new Batching();
            batching.setBrand(obj.getBrand());
            batching.setName(obj.getBatchName());
            batching.setCreateTime(LocalDateTime.now());
            batching.setUpdateTime(LocalDateTime.now());
            batching.setUnit(obj.getUnit());

            int insert = batchingMapper.insert(batching);
            if (insert > 0) {
                inventoryRecord.setBatchId(batching.getId());
                LambdaQueryWrapper<BatchNumber> batchWrapper = new LambdaQueryWrapper<>();
                batchWrapper.eq(BatchNumber :: getStatus, 1).eq(BatchNumber :: getShopId, obj.getShopId()).eq(BatchNumber :: getBatchId, batching.getId()).eq(BatchNumber :: getUseNumber, obj.getUseNumber());
                BatchNumber batchNumber = batchNumberMapper.selectOne(batchWrapper);
                if (null == batchNumber) {
                    BatchNumber batchNumber1 = new BatchNumber();
                    batchNumber1.setBatchId(batching.getId());
                    batchNumber1.setShopId(obj.getShopId());
                    batchNumber1.setUseNumber(obj.getUseNumber());
                    batchNumber1.setUnit(obj.getUnit());
                    batchNumber1.setBatchName(obj.getBatchName());
                    batchNumber1.setCreateTime(LocalDateTime.now());
                    batchNumber1.setUpdateTime(LocalDateTime.now());
                    batchNumberMapper.insert(batchNumber1);
                }
                LambdaQueryWrapper<BatchUse> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(BatchUse :: getStatus, 1).eq(BatchUse :: getBatchId, batching.getId()).eq(BatchUse :: getShopId, obj.getShopId());
                BatchUse batchUse = batchUseMapper.selectOne(wrapper);
                if (null == batchUse) {
                    BatchUse batchUse1 = new BatchUse();
                    batchUse1.setShopId(obj.getShopId());
                    batchUse1.setBatchId(batching.getId());
                    batchUse1.setCreateTime(LocalDateTime.now());
                    batchUse1.setUpdateTime(LocalDateTime.now());
                    batchUse1.setTotalCount(obj.getCount() * obj.getUseNumber());
                    batchUseMapper.insert(batchUse1);
                }else {
                    batchUse.setTotalCount(batchUse.getTotalCount() + obj.getUseNumber() * obj.getCount());
                    batchUse.setUpdateTime(LocalDateTime.now());
                    batchUseMapper.updateById(batchUse);
                }


            }
        }else {
            LambdaQueryWrapper<BatchUse> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(BatchUse :: getStatus, 1).eq(BatchUse :: getBatchId, batching1.getId()).eq(BatchUse :: getShopId, obj.getShopId());
            BatchUse batchUse = batchUseMapper.selectOne(wrapper);
            if (null == batchUse) {
                BatchUse batchUse1 = new BatchUse();
                batchUse1.setShopId(obj.getShopId());
                batchUse1.setBatchId(batching1.getId());
                batchUse1.setCreateTime(LocalDateTime.now());
                batchUse1.setUpdateTime(LocalDateTime.now());
                batchUse1.setTotalCount(obj.getCount() * obj.getUseNumber());
                batchUseMapper.insert(batchUse1);
            }else {
                batchUse.setTotalCount(batchUse.getTotalCount() + obj.getUseNumber() * obj.getCount());
                batchUse.setUpdateTime(LocalDateTime.now());
                batchUseMapper.updateById(batchUse);
            }
            LambdaQueryWrapper<BatchNumber> batchWrapper = new LambdaQueryWrapper<>();
            batchWrapper.eq(BatchNumber :: getStatus, 1).eq(BatchNumber :: getShopId, obj.getShopId()).eq(BatchNumber :: getBatchId, batching1.getId()).eq(BatchNumber :: getUseNumber, obj.getUseNumber());
            BatchNumber batchNumber = batchNumberMapper.selectOne(batchWrapper);
            if (null == batchNumber) {
                BatchNumber batchNumber1 = new BatchNumber();
                batchNumber1.setBatchId(batching1.getId());
                batchNumber1.setUseNumber(obj.getUseNumber());
                batchNumber1.setUnit(obj.getUnit());
                batchNumber1.setShopId(obj.getShopId());
                batchNumber1.setBatchName(obj.getBatchName());
                batchNumber1.setCreateTime(LocalDateTime.now());
                batchNumber1.setUpdateTime(LocalDateTime.now());
                batchNumberMapper.insert(batchNumber1);
            }
            inventoryRecord.setBatchId(batching1.getId());
        }
        int insert = baseMapper.insert(inventoryRecord);
        return insert;
    }

    @Override
    public Integer outInventory(Long batchNumberId, Integer count, String remarks) {
        BatchNumber batchNumber = batchNumberMapper.selectById(batchNumberId);
        Batching batching = batchingMapper.selectById(batchNumber.getBatchId());
        LambdaQueryWrapper<BatchUse> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BatchUse :: getStatus, 1).eq(BatchUse :: getBatchId, batching.getId()).eq(BatchUse :: getShopId, batchNumber.getShopId());
        BatchUse batchUse = batchUseMapper.selectOne(queryWrapper);
        if (null != batchUse) {
            batchUse.setUseCount(batchUse.getUseCount() + (count * batchNumber.getUseNumber()));
            batchUse.setTotalCount(batchUse.getTotalCount() - (count * batchNumber.getUseNumber()) > 0 ? batchUse.getTotalCount() - (count * batchNumber.getUseNumber()) : 0);
            batchUse.setUpdateTime(LocalDateTime.now());
            batchUseMapper.updateById(batchUse);
        }

        InventoryRecord inventoryRecord = new InventoryRecord();
        inventoryRecord.setRemarks(remarks);
        inventoryRecord.setCreateTime(LocalDateTime.now());
        inventoryRecord.setUpdateTime(LocalDateTime.now());
        inventoryRecord.setShopId(batchNumber.getShopId());
        inventoryRecord.setBatchId(batchNumber.getBatchId());
        inventoryRecord.setBatchName(batchNumber.getBatchName());
        inventoryRecord.setUnit(batchNumber.getUnit());
        inventoryRecord.setUseNumber(batchNumber.getUseNumber());
        inventoryRecord.setBrand(batching.getBrand());
        inventoryRecord.setCount(count);
        inventoryRecord.setType(2);
        int insert = baseMapper.insert(inventoryRecord);

        return insert;
    }

}
