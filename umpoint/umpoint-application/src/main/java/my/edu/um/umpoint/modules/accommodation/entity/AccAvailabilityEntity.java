package my.edu.um.umpoint.modules.accommodation.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * Accommodation Availability
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-23
 */
@Data
@TableName("acc_availability")
public class AccAvailabilityEntity {
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
	* Year
	*/
	private Integer year;
	/**
	* Availability of accommodation, consist of 366*24 bit, 1 represent available in specific day in one year
	*/
	private byte[] availability;
}