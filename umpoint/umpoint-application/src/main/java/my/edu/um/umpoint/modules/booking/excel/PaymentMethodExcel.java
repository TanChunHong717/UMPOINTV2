package my.edu.um.umpoint.modules.booking.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;

import java.util.Date;

/**
 * Payment Method
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-09-10
 */
@Data
public class PaymentMethodExcel {
    @ExcelProperty(value = "ID")
    private Long id;
    @ExcelProperty(value = "Method")
    private String method;

}