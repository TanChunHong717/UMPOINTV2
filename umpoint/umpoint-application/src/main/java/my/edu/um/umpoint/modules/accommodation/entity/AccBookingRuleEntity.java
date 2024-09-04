package my.edu.um.umpoint.modules.accommodation.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

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
	 * 0:Automatic approve 1: Require admin approve
	 */
	private Integer approvalRequired;
	/**
	 * 0:Staff not allow to book 1:Staff allow to book
	 */
	private Integer openForStaff;
	/**
	 * 0:Student not allow to book 1:Student allow to book
	 */
	private Integer openForStudent;
	/**
	 * 0:Public not allow to book 1:Public allow to book
	 */
	private Integer openForPublic;
	/**
	 * Days close for booking before event
	 */
	private Integer closeDaysBeforeEvent;
	/**
	 * Days close for booking after event
	 */
	private Integer closeDaysAfterEvent;
	/**
	* Maximum reservation days
	*/
	private Integer maxReservationDays;
	/**
	* Minimum booking days
	*/
	private Integer minBookingDays;
}