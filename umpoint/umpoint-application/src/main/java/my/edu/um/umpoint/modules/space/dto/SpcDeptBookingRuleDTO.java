package my.edu.um.umpoint.modules.space.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Space Department Level Booking Rule
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-11-02
 */
@Data
@Schema(title = "Space Department Level Booking Rule")
public class SpcDeptBookingRuleDTO implements Serializable {
	@Serial
    private static final long serialVersionUID = 8771111716730366292L;

	@Schema(title = "ID")
	private Long id;

	@Schema(title = "Dept ID")
	private Long deptId;

	@Schema(title = "Maximum booking number allow for each user in a day (0 mean no restrict)")
	private BigDecimal maxUserDailyBooking;

	@Schema(title = "Maximum booking hour allow for each user in a day (0 mean no restrict)")
	private BigDecimal maxUserDailyBookingHour;

	@Schema(title = "Department Name")
	private String name;
}
