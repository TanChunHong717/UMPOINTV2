package my.edu.um.umpoint.modules.space.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Time;
import java.util.Date;

/**
 * Space Closure Period
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-09-16
 */
@Data
@TableName("spc_closure")
public class SpcClosureEntity {
	/**
	* ID
	*/
	@TableId
	private Long id;
	/**
	* Space ID
	*/
	private Long spaceId;
	/**
	* Start day of booking
	*/
	private Date startDay;
	/**
	* End day of booking
	*/
	private Date endDay;
	/**
	* Start time of booking in a day
	*/
	private Time startTime;
	/**
	* End time of booking in a day
	*/
	private Time endTime;
	/**
	* Recur on Monday, 0:No, 1:Yes
	*/
	private Integer recurMonday;
	/**
	* Recur on Tuesday, 0:No, 1:Yes
	*/
	private Integer recurTuesday;
	/**
	* Recur on Wednesday, 0:No, 1:Yes
	*/
	private Integer recurWednesday;
	/**
	* Recur on Thursday, 0:No, 1:Yes
	*/
	private Integer recurThursday;
	/**
	* Recur on Friday, 0:No, 1:Yes
	*/
	private Integer recurFriday;
	/**
	* Recur on Saturday, 0:No, 1:Yes
	*/
	private Integer recurSaturday;
	/**
	* Recur on Sunday, 0:No, 1:Yes
	*/
	private Integer recurSunday;
}