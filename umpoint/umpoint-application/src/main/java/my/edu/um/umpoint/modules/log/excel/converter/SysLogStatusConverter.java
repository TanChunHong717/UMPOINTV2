package my.edu.um.umpoint.modules.log.excel.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

public class SysLogStatusConverter implements Converter<Integer> {

    @Override
    public Class<Integer> supportJavaTypeKey() {
        return Integer.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public Integer convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        if(cellData.getStringValue().equals("failed")){
            return 0;
        }else if(cellData.getStringValue().equals("success")){
            return 1;
        }else if(cellData.getStringValue().equals("account lock")){
            return 2;
        }else{
            return -1;
        }
    }

    @Override
    public WriteCellData<?> convertToExcelData(Integer value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
       if(value == 0){
           return new WriteCellData<>("failed");
       }else if(value == 1){
           return new WriteCellData<>("success");
       }else if(value == 2){
           return new WriteCellData<>("account lock");
       }else{
           return new WriteCellData<>("unknow");
       }
    }

}
