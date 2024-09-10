package my.edu.um.umpoint.modules.booking.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Payment
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-09-08
 */
@Data
public class PaymentExcel {
    @ExcelProperty(value = "ID")
    private Long id;
    @ExcelProperty(value = "Status: 0:Pending, 1:Success, 2:Failed, 3:Refund")
    private Integer status;
    @ExcelProperty(value = "User ID")
    private Long userId;
    @ExcelProperty(value = "Payment Amount")
    private BigDecimal amount;
    @ExcelProperty(value = "Payment Method ID")
    private Long method_id;
    @ExcelProperty(value = "Payment date")
    private Date date;

}