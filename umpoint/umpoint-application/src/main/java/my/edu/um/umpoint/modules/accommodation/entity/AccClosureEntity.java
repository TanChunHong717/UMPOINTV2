package my.edu.um.umpoint.modules.accommodation.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * Accommodation Closure Period
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-09-16
 */
@Data
@TableName("acc_closure")
public class AccClosureEntity {
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
	* Start day of booking
	*/
	private Date startDay;
	/**
	* End day of booking
	*/
	private Date endDay;
}