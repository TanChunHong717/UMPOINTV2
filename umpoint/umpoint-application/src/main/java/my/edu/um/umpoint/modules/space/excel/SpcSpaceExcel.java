package my.edu.um.umpoint.modules.space.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * Space
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-03
 */
@Data
public class SpcSpaceExcel {
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
    @ExcelProperty(value = "Facilities")
    private String facilities;

}