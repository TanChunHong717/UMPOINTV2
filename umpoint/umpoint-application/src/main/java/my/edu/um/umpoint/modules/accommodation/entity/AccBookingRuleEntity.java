package my.edu.um.umpoint.modules.accommodation.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Accommodation Booking Rule
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-23
 */
@Data
@TableName("acc_booking_rule")
public class AccBookingRuleEntity {
	/**
	* ID
	*/
	@TableId
	private Long id;
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
	* 0:Automatic approve 1: Require admin approve
	*/
	private Integer approvalRequired;
	/**
	* Minimum booking days
	*/
	private Integer minBookingDays;
}