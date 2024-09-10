package my.edu.um.umpoint.modules.booking.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import java.math.BigDecimal;

/**
 * Accommodation Booking
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-09-08
 */
@Data
@Schema(name = "Accommodation Booking")
public class AccBookingDTO implements Serializable {
	@Serial
    private static final long serialVersionUID = 1L;

	@SchemaProperty(name = "ID")
	private Long id;

	@SchemaProperty(name = "Status: 0:Pending, 1:Reject, 2:Approve(Pending Payment), 3:Approve(Payment Complete), 4:Cancel")
	private Integer status;

	@SchemaProperty(name = "Accommodation ID")
	private Long accommodationId;

	@SchemaProperty(name = "Admin that approve/reject, user will contact this admin rather manager if umpoint.booking.accommodation.find-approve-admin-first=true")
	private Long adminId;

	@SchemaProperty(name = "User ID")
	private Long userId;

	@SchemaProperty(name = "Worker responsible if booking is not in working day")
	private Long workerId;

	@SchemaProperty(name = "Payment ID, point latest payment if there is no success/refund payment")
	private Long paymentId;

	@SchemaProperty(name = "Amount need to be pay")
	private BigDecimal paymentAmount;

	@SchemaProperty(name = "Start day of booking")
	private Date startDay;

	@SchemaProperty(name = "End day of booking")
	private Date endDay;

	@SchemaProperty(name = "Create date")
	private Date createDate;

	@SchemaProperty(name = "Update date")
	private Date updateDate;

	@SchemaProperty(name = "Name of space that being booked")
	private String space;

	@SchemaProperty(name = "Name of admin who approve/reject booking")
	private String admin;

	@SchemaProperty(name = "Name of user who make booking")
	private String username;

	@SchemaProperty(name = "Name of worker that being assigned to this booking")
	private String worker;

	@SchemaProperty(name = "Payment status")
	private String paymentStatus;
}
