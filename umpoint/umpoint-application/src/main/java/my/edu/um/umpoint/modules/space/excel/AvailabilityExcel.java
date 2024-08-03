package my.edu.um.umpoint.modules.space.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Space Image
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-03
 */
@Data
public class AvailabilityExcel {
    @ExcelProperty(value = "ID")
    private Long id;
    @ExcelProperty(value = "Space ID")
    private Long spaceId;
    @ExcelProperty(value = "Year")
    private BigDecimal year;
    @ExcelProperty(value = "Availability of space, consist of 366*24 bit, 1 represent available in specific hour in one year")
    private byte[] availability;

}