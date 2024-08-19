package my.edu.um.umpoint.modules.space.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * Space Tag
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-03
 */
@Data
public class SpcTagExcel {
    @ExcelProperty(value = "ID")
    private Long id;
    @ExcelProperty(value = "Tag name")
    private String tagName;

}