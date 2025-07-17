package com.tea.server.entity.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.tea.server.controller.converter.BigDecimalToStringConverter;
import com.tea.server.controller.converter.PlatIntToStringConverter;
import com.tea.server.controller.converter.TimeToStringConverter;
import com.tea.server.controller.converter.TypeIntToStringConverter;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Created with IntelliJ IDEA.
 * 导出成绩   Excel文件
 */
@Data
public class FlowData extends BaseRowModel {
    /**
     * 列名: serialNum
     * 注释: 订单编号
     */
    //设置表头名称
    @ExcelProperty(value = "订单编号", index = 0)
    private String serialNum;
    /**
     * 列名: type
     */
    //设置表头名称
    @ExcelProperty(value = "账单类型", index = 1, converter = TypeIntToStringConverter.class)
    private Integer type;
    /**
     * 列名: shopDistributionPrice
     * 注释: 金额
     */
    //设置表头名称
    @ExcelProperty(value = "金额", index = 2, converter = BigDecimalToStringConverter.class)
    private BigDecimal shopDistributionPrice;


    //设置表头名称
    @ExcelProperty(value = "平台", index = 3, converter = PlatIntToStringConverter.class)
    private Integer payPlatform;


    //设置表头名称
    @ExcelProperty(value = "创建时间", index = 4)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String createTime;

}
