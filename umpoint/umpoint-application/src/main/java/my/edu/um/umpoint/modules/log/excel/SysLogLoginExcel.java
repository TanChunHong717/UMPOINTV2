package my.edu.um.umpoint.modules.log.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import my.edu.um.umpoint.modules.log.excel.converter.SysLogOperationConverter;
import my.edu.um.umpoint.modules.log.excel.converter.SysLogStatusConverter;
import lombok.Data;

import java.util.Date;

@Data
@ContentRowHeight(20)
@HeadRowHeight(20)
@ColumnWidth(25)
public class SysLogLoginExcel {
    @ExcelProperty(value = "operation", converter = SysLogOperationConverter.class)
    private Integer operation;

    @ExcelProperty(value = "status", converter = SysLogStatusConverter.class)
    private Integer status;

    @ExcelProperty("user agent")
    private String userAgent;

    @ExcelProperty("IP")
    private String ip;

    @ExcelProperty("username")
    private String creatorName;

    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @ExcelProperty("create date")
    private Date createDate;

}
