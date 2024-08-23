package my.edu.um.umpoint.modules.accommodation.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * Accommodation tag relationship
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-23
 */
@Data
@TableName("acc_accommodation_tag")
public class AccAccommodationTagEntity {
	/**
	* Accommodation ID
	*/
	@TableId
	private Long accommodationId;
	/**
	* Tag ID
	*/
	private Long tagId;
}