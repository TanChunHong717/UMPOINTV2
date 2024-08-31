package my.edu.um.umpoint.modules.service.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Service
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-20
 */
@Data
public class SvcServiceExcel {
    @ExcelProperty(value = "ID")
    private Long id;
    @ExcelProperty(value = "Name")
    private String name;
    @ExcelProperty(value = "Category ID")
    private Long catId;
    @ExcelProperty(value = "Department ID")
    private Long deptId;
    @ExcelProperty(value = "Description")
    private String description;
    @ExcelProperty(value = "Manager ID")
    private Long manager;
    @ExcelProperty(value = "Booking Rule ID")
    private Long bookingRuleId;
    @ExcelProperty(value = "Creator")
    private Long creator;
    @ExcelProperty(value = "Create date")
    private Date createDate;
    @ExcelProperty(value = "Updater")
    private Long updater;
    @ExcelProperty(value = "Update date")
    private Date updateDate;
}