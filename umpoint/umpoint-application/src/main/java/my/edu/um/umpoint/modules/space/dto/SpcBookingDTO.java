package my.edu.um.umpoint.modules.space.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import my.edu.um.umpoint.modules.payment.dto.SpcPaymentDTO;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

import java.math.BigDecimal;
import java.util.List;

/**
 * Space Booking
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-09-08
 */
@Data
@Schema(title = "Space Booking")
public class SpcBookingDTO implements Serializable {
	@Serial
    private static final long serialVersionUID = -6117663789310186548L;

	@Schema(title = "ID")
	private Long id;

	@Schema(title = "Status: 0:Pending, 1:Reject, 2:Approve(Pending Payment), 3:Approve(Payment Complete), 4:Cancel")
	private Integer status;

	@Schema(title = "Space ID")
	private Long spaceId;

	@Schema(title = "Admin that approve/reject, user will contact this admin rather manager if umpoint.booking.space.find-approve-admin-first=true")
	private Long adminId;

	@Schema(title = "User ID")
	private Long userId;

	@Schema(title = "Amount need to be pay")
	private BigDecimal paymentAmount;

	@Schema(title = "Description of the event or purpose for the booking")
	private String event;

	@Schema(title = "Start day of booking")
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date startDay;

	@Schema(title = "End day of booking")
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date endDay;

	@Schema(title = "Start time of booking in a day")
	private Time startTime;

	@Schema(title = "End time of booking in a day")
	private Time endTime;

	@Schema(title = "Number of technician")
	private Integer technicianNumber;

	@Schema(title = "Create date")
	private Date createDate;

	@Schema(title = "Update date")
	private Date updateDate;

	@Schema(title = "Name of space that being booked")
	private String space;

	@Schema(title = "Name of admin who approve/reject booking")
	private String admin;

	@Schema(title = "Name of user who make booking")
	private String username;

	@Schema(title = "Technician dto list")
	private List<SpcBookingTechnicianDTO> spcBookingTechnicianDTOList;

	@Schema(title = "Attachment dto list")
	private List<SpcBookingAttachmentDTO> spcBookingAttachmentDTOList;

	@Schema(title = "Payment dto list")
	private List<SpcPaymentDTO> spcPaymentDTOList;
}
