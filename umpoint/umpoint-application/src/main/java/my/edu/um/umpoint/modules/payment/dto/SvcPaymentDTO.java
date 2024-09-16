package my.edu.um.umpoint.modules.payment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
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
@Schema(name = "Service Payment")
public class SvcPaymentDTO implements Serializable {
	@Serial
    private static final long serialVersionUID = -1511545322184053328L;

	@SchemaProperty(name = "ID")
	private Long id;

	@SchemaProperty(name = "Status: 0:Pending, 1:Success, 2:Failed, 3:Refund")
	private Integer status;

	@SchemaProperty(name = "Service Booking ID")
	private Long bookingId;

	@SchemaProperty(name = "User ID")
	private Long userId;

	@SchemaProperty(name = "Payment Method ID")
	private String methodId;

	@SchemaProperty(name = "Payment Amount")
	private BigDecimal amount;

	@SchemaProperty(name = "Payment Date")
	private Date date;

	@SchemaProperty(name = "Payment Method name")
	private String method;
}
