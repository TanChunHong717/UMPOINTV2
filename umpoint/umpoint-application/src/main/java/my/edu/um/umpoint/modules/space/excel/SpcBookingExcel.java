package my.edu.um.umpoint.modules.space.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.Date;

/**
 * Space Booking
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-09-08
 */
@Data
public class SpcBookingExcel {
    @ExcelProperty(value = "ID")
    private Long id;
    @ExcelProperty(value = "Status: 0:Pending, 1:Reject, 2:Approve(Pending Payment), 3:Approve(Payment Complete), 4:Cancel")
    private Integer status;
    @ExcelProperty(value = "Space ID")
    private Long spaceId;
    @ExcelProperty(value = "Admin that approve/reject, user will contact this admin rather manager if umpoint.booking.space.find-approve-admin-first=true")
    private Long adminId;
    @ExcelProperty(value = "User ID")
    private Long userId;
    @ExcelProperty(value = "Worker responsible if booking is not in working day")
    private Long workerId;
    @ExcelProperty(value = "Payment ID")
    private Long paymentId;
    @ExcelProperty(value = "Amount need to be pay")
    private BigDecimal paymentAmount;
    @ExcelProperty(value = "Start day of booking")
    private Date startDay;
    @ExcelProperty(value = "End day of booking")
    private Date endDay;
    @ExcelProperty(value = "Start time of booking in a day")
    private Time startTime;
    @ExcelProperty(value = "End time of booking in a day")
    private Time endTime;
    @ExcelProperty(value = "Create date")
    private Date createDate;
    @ExcelProperty(value = "Update date")
    private Date updateDate;

}