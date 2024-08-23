package my.edu.um.umpoint.modules.accommodation.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Accommodation
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-23
 */
@Data
public class AccAccommodationExcel {
    @ExcelProperty(value = "ID")
    private Long id;
    @ExcelProperty(value = "Name")
    private String name;
    @ExcelProperty(value = "Category ID")
    private Long catId;
    @ExcelProperty(value = "Department ID")
    private Long deptId;
    @ExcelProperty(value = "Address")
    private String address;
    @ExcelProperty(value = "Description")
    private String description;
    @ExcelProperty(value = "Facilities")
    private String facilities;
    @ExcelProperty(value = "Max capacity")
    private BigDecimal capacity;
    @ExcelProperty(value = "Manager ID")
    private Long manager;
    @ExcelProperty(value = "Booking Rule ID")
    private Long bookingRuleId;
    @ExcelProperty(value = "Status 0:Suspend 1:Normal")
    private Integer status;
    @ExcelProperty(value = "Creator")
    private Long creator;
    @ExcelProperty(value = "Create date")
    private Date createDate;
    @ExcelProperty(value = "Updater")
    private Long updater;
    @ExcelProperty(value = "Update date")
    private Date updateDate;
}