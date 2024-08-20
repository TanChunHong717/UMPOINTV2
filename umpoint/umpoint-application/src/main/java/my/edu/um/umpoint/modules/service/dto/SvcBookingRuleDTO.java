package my.edu.um.umpoint.modules.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

import java.math.BigDecimal;

/**
 * Service Booking Rule
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-20
 */
@Data
@Schema(name = "Service Booking Rule")
public class SvcBookingRuleDTO implements Serializable {
	@Serial
    private static final long serialVersionUID = 162449243906820661L;

	@SchemaProperty(name = "ID")
	private Long id;

	@SchemaProperty(name = "Price")
	private BigDecimal price;

	@SchemaProperty(name = "Is booking require approve by manager")
	private Integer approvalRequired;

}
