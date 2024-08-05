package my.edu.um.umpoint.modules.sys.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class SysParamsExcel {

    @ExcelProperty("param code")
    private String paramCode;

    @ExcelProperty("param value")
    private String paramValue;

    @ExcelProperty("remark")
    private String remark;

}