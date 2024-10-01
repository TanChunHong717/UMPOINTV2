package my.edu.um.umpoint.modules.client.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;

import java.util.Date;

/**
 * User
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-10-01
 */
@Data
public class CliUserExcel {
    @ExcelProperty(value = "ID")
    private Long id;
    @ExcelProperty(value = "Username")
    private String username;
    @ExcelProperty(value = "Mobile")
    private String mobile;
    @ExcelProperty(value = "Password")
    private String password;
    @ExcelProperty(value = "Email")
    private String email;
    @ExcelProperty(value = "Create date")
    private Date createDate;

}