package my.edu.um.umpoint.modules.accommodation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import lombok.Data;
import my.edu.um.umpoint.modules.payment.dto.AccPaymentDTO;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import java.math.BigDecimal;
import java.util.List;

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

	@SchemaProperty(name = "Amount need to be pay")
	private BigDecimal paymentAmount;

	@SchemaProperty(name = "Description of the event or purpose for the booking")
	private String event;

	@SchemaProperty(name = "Start day of booking")
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date startDay;

	@SchemaProperty(name = "End day of booking")
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date endDay;

	@SchemaProperty(name = "Number of technician")
	private Integer technicianNumber;

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

	@SchemaProperty(name = "Technician dto list")
	private List<AccBookingTechnicianDTO> accBookingTechnicianDTOList;

	@SchemaProperty(name = "Payment dto list")
	private List<AccPaymentDTO> accPaymentDTOList;
}
