package my.edu.um.umpoint.modules.space.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * Space Occupied Event
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-09-16
 */
@Data
@TableName("spc_event")
public class SpcEventEntity {
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
	* Booking ID
	*/
	private Long bookingId;
	/**
	* Closure ID
	*/
	private Long closureId;
	/**
	* Type: 0:Booking, 1:Closure period
	*/
	private Integer type;
	/**
	* Event start time
	*/
	private Date startTime;
	/**
	* Event end time
	*/
	private Date endTime;
}