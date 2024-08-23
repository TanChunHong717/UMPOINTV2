package my.edu.um.umpoint.modules.accommodation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import java.math.BigDecimal;

/**
 * Accommodation Booking Rule
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-23
 */
@Data
@Schema(name = "Accommodation Booking Rule")
public class AccBookingRuleDTO implements Serializable {
	@Serial
    private static final long serialVersionUID = 9187174069158066308L;

	@SchemaProperty(name = "ID")
	private Long id;

	@SchemaProperty(name = "Price for book a day")
	private BigDecimal dayPrice;

	@SchemaProperty(name = "Price for book a week")
	private BigDecimal weekPrice;

	@SchemaProperty(name = "Days open for booking before event")
	private Integer openDaysBeforeEvent;

	@SchemaProperty(name = "Days close for booking before event")
	private Integer closeDaysBeforeEvent;

	@SchemaProperty(name = "Maximum reservation days")
	private Integer maxReservationDays;

	@SchemaProperty(name = "Is booking require approve by manager")
	private Integer approvalRequired;

	@SchemaProperty(name = "Minimum booking days")
	private Integer minBookingDays;
}
