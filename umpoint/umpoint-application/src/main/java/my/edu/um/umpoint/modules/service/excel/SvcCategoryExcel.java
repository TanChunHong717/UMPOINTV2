package my.edu.um.umpoint.modules.service.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * Service Category
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-20
 */
@Data
public class SvcCategoryExcel {
    @ExcelProperty(value = "ID")
    private Long id;
    @ExcelProperty(value = "Name")
    private String name;

}