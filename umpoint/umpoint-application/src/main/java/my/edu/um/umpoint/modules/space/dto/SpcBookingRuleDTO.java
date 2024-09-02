package my.edu.um.umpoint.modules.space.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

import java.math.BigDecimal;

/**
 * Space Availability
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-11
 */
@Data
@Schema(name = "Space Availability")
public class SpcBookingRuleDTO implements Serializable {
	@Serial
    private static final long serialVersionUID = 3105155482684743857L;

	@SchemaProperty(name = "ID")
	private Long id;

	@SchemaProperty(name = "Days open for booking before event")
	private Integer openDaysBeforeEvent;

	@SchemaProperty(name = "Days close for booking before event")
	private Integer closeDaysBeforeEvent;

	@SchemaProperty(name = "Maximum reservation days")
	private Integer maxReservationDays;

	@SchemaProperty(name = "0:Automatic approve 1: Require admin approve")
	private Integer approvalRequired;

	@SchemaProperty(name = "Minimum booking hours per day")
	private Integer minBookingHours;
}
