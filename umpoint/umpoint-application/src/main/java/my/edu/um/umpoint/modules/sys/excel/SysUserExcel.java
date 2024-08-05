package my.edu.um.umpoint.modules.sys.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import my.edu.um.umpoint.modules.sys.excel.converter.GenderConverter;
import my.edu.um.umpoint.modules.sys.excel.converter.StatusConverter;
import lombok.Data;

import java.util.Date;

@Data
public class SysUserExcel {

    @ExcelProperty("username")
    private String username;

    @ExcelProperty("name")
    private String realName;

    @ExcelProperty(value = "gender", converter = GenderConverter.class)
    private Integer gender;

    @ExcelProperty("email")
    private String email;

    @ExcelProperty("mobile")
    private String mobile;

    @ExcelProperty("department name")
    private String deptName;

    @ExcelProperty(value = "status", converter = StatusConverter.class)
    private Integer status;

    @ExcelProperty("remark")
    private String remark;

    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @ExcelProperty("create date")
    private Date createDate;

}
