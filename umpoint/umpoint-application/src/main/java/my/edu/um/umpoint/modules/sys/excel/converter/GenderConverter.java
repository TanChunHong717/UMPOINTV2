package my.edu.um.umpoint.modules.sys.excel.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;


public class GenderConverter implements Converter<Integer> {


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
        if(cellData.getStringValue().equals("male")){
            return 0;
        }else if(cellData.getStringValue().equals("female")){
            return 1;
        }else {
            return 2;
        }
    }

    @Override
    public WriteCellData<?> convertToExcelData(Integer value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        if(value == 0){
            return new WriteCellData<>("male");
        }else if(value == 1){
            return new WriteCellData<>("female");
        }else {
            return new WriteCellData<>("other");
        }
    }
}