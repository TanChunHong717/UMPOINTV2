package my.edu.um.umpoint.modules.booking.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;


/**
 * Payment Method
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-09-10
 */
@Data
@Schema(name = "Payment Method")
public class PaymentMethodDTO implements Serializable {
	@Serial
    private static final long serialVersionUID = 4396241986506494703L;

	@SchemaProperty(name = "ID")
	private Long id;

	@SchemaProperty(name = "Method")
	private String method;
}
