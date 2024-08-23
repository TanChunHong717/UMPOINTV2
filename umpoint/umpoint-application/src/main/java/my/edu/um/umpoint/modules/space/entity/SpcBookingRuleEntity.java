package my.edu.um.umpoint.modules.space.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Space Availability
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-11
 */
@Data
@TableName("spc_booking_rule")
public class SpcBookingRuleEntity {
    /**
     * ID
     */
	@TableId
	private Long id;
    /**
     * Price for book an hour
     */
	private BigDecimal hourPrice;
    /**
     * Price for book four hours
     */
	private BigDecimal fourHoursPrice;
    /**
     * Price for book a day
     */
	private BigDecimal dayPrice;
    /**
     * Days open for booking before event
     */
	private Integer openDaysBeforeEvent;
    /**
     * Days close for booking before event
     */
	private Integer closeDaysBeforeEvent;
    /**
     * Maximum reservation days
     */
	private Integer maxReservationDays;
    /**
     * Is booking require approve by manager
     */
	private Integer approvalRequired;
    /**
     * Minimum booking hours per day
     */
	private Integer minBookingHours;
}