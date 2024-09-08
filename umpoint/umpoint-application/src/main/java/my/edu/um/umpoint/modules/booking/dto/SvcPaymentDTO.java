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
public class SvcPaymentDTO implements Serializable {
	@Serial
    private static final long serialVersionUID = -5115223823598000406L;

	@SchemaProperty(name = "ID")
	private Long id;

	@SchemaProperty(name = "Status: 0:Pending, 1:Success, 2:Failed, 3:Refund")
	private Integer status;

	@SchemaProperty(name = "User ID")
	private Long userId;

	@SchemaProperty(name = "Payment Amount")
	private BigDecimal amount;

	@SchemaProperty(name = "Payment Method")
	private String method;

	@SchemaProperty(name = "Payment date")
	private Date date;
}
