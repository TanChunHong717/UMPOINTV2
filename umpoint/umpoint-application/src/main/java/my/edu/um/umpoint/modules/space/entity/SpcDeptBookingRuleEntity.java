package my.edu.um.umpoint.modules.space.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Space Department Level Booking Rule
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-11-02
 */
@Data
@TableName("spc_dept_booking_rule")
public class SpcDeptBookingRuleEntity {
	/**
	* ID
	*/
	@TableId
	private Long id;
	/**
	* Dept ID
	*/
	private Long deptId;
	/**
	* Maximum booking number allow for each user in a day (0 mean no restrict)
	*/
	private BigDecimal maxUserDailyBooking;
	/**
	* Maximum booking hour allow for each user in a day (0 mean no restrict)
	*/
	private BigDecimal maxUserDailyBookingHour;
	/**
	 * Department name
	 */
	@TableField(exist = false)
	private String name;
}