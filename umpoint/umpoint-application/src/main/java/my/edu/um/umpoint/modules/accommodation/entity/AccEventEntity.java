package my.edu.um.umpoint.modules.accommodation.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * Accommodation Occupied Event
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-09-16
 */
@Data
@TableName("acc_event")
public class AccEventEntity {
	/**
	* ID
	*/
	@TableId
	private Long id;
	/**
	* Accommodation ID
	*/
	private Long accommodationId;
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