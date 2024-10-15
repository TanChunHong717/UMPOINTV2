package my.edu.um.umpoint.modules.payment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(title = "Payment Method")
public class PaymentMethodDTO implements Serializable {
	@Serial
    private static final long serialVersionUID = 4396241986506494703L;

	@Schema(title = "ID")
	private Long id;

	@Schema(title = "Method")
	private String method;
}
