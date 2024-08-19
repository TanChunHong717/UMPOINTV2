package my.edu.um.umpoint.modules.space.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * Space Category
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-03
 */
@Data
public class SpcCategoryExcel {
    @ExcelProperty(value = "ID")
    private Long id;
    @ExcelProperty(value = "Name")
    private String name;

}