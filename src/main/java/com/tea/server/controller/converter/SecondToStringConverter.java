package com.tea.server.controller.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

public class SecondToStringConverter implements Converter<Long> {
    @Override
    public Class supportJavaTypeKey() {
        return null;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return null;
    }

    @Override
    public Long convertToJavaData(CellData cellData, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        return null;
    }

    @Override
    public CellData convertToExcelData(Long integer, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        if(integer < 60){
            return new CellData(integer + "秒");
        }else if (integer >=60 && integer < 3600) {
            return new CellData(integer / 60 + "分" + integer % 60 + "秒");
        }else {
            return new CellData(integer / 3600 + "小时" + (integer % 3600) / 60+ "分" + (integer % 3600) % 60 + "秒");
        }

    }

}
