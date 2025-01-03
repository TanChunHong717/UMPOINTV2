package my.edu.um.umpoint.modules.accommodation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import lombok.Data;
import my.edu.um.umpoint.modules.payment.dto.AccPaymentDTO;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Accommodation Booking
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-09-08
 */
@Data
@Schema(title = "Accommodation Booking")
public class AccClientBookingDTO implements Serializable {
	@Serial
    private static final long serialVersionUID = -7813381979265040276L;

	@Schema(title = "Accommodation ID")
	private Long accommodationId;

	@Schema(title = "Description of the event or purpose for the booking")
	private String event;

	@Schema(title = "Start day of booking")
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date startDay;

	@Schema(title = "End day of booking")
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date endDay;
}
