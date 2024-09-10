package my.edu.um.umpoint.modules.booking.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import java.math.BigDecimal;

/**
 * Payment
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-09-08
 */
@Data
@Schema(name = "Payment")
public class PaymentDTO implements Serializable {
	@Serial
    private static final long serialVersionUID = -1511545322184053328L;

	@SchemaProperty(name = "ID")
	private Long id;

	@SchemaProperty(name = "Status: 0:Pending, 1:Success, 2:Failed, 3:Refund")
	private Integer status;

	@SchemaProperty(name = "Resource Type: 0:Space, 1:Service, 2:Accommodation")
	private Integer resourceType;

	@SchemaProperty(name = "Space/Service/Accommodation ID")
	private Long resourceId;

	@SchemaProperty(name = "User ID")
	private Long userId;

	@SchemaProperty(name = "Payment Method ID")
	private String method_id;

	@SchemaProperty(name = "Payment Amount")
	private BigDecimal amount;

	@SchemaProperty(name = "Payment Date")
	private Date date;

	@SchemaProperty(name = "Payment Method name")
	private String method;

	@SchemaProperty(name = "Name of user who make payment")
	private String username;
}
