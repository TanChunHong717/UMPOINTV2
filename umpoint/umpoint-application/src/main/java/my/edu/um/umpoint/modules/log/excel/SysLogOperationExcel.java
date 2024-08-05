package my.edu.um.umpoint.modules.log.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;

import java.util.Date;

@Data
@ContentRowHeight(20)
@HeadRowHeight(20)
@ColumnWidth(25)
public class SysLogOperationExcel {
    @ExcelProperty("operation")
    private String operation;

    @ExcelProperty("request URI")
    private String requestUri;

    @ExcelProperty("request method")
    private String requestMethod;

    @ExcelProperty("params")
    private String requestParams;

    @ExcelProperty("times(ms)")
    private Integer requestTime;

    @ExcelProperty("user agent")
    private String userAgent;

    @ExcelProperty("IP")
    private String ip;

    @ExcelProperty("status")
    private Integer status;

    @ExcelProperty("username")
    private String creatorName;

    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @ExcelProperty("create date")
    private Date createDate;

}
