package my.edu.um.umpoint.modules.payment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Service Payment
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-09-08
 */
@Data
@Schema(title = "Service Payment")
public class SvcPaymentDTO implements Serializable {
	@Serial
    private static final long serialVersionUID = -1511545322184053328L;

	@Schema(title = "ID")
	private Long id;

	@Schema(title = "Status: 0:Pending, 1:Success, 2:Failed, 3:Refund")
	private Integer status;

	@Schema(title = "Service Booking ID")
	private Long bookingId;

	@Schema(title = "User ID")
	private Long userId;

	@Schema(title = "Payment Method ID")
	private String methodId;

	@Schema(title = "Payment Amount")
	private BigDecimal amount;

	@Schema(title = "Payment Date")
	private Date date;

	@Schema(title = "Payment Method name")
	private String method;
}
