package my.edu.um.umpoint.modules.space.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Space Availability
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-11
 */
@Data
@TableName("spc_booking_rule")
public class BookingRuleEntity {

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
	private BigDecimal openDaysBeforeEvent;
    /**
     * Days close for booking before event
     */
	private BigDecimal closeDaysBeforeEvent;
    /**
     * Maximum reservation days
     */
	private BigDecimal maxReservationDays;
    /**
     * Is booking require approve by manager
     */
	private Integer approvalRequired;
    /**
     * Minimum booking hours per day
     */
	private BigDecimal minBookingHours;
}