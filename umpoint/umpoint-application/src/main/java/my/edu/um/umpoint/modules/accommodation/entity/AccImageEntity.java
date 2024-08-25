package my.edu.um.umpoint.modules.accommodation.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * Accommodation Image
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-23
 */
@Data
@TableName("acc_image")
public class AccImageEntity {
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
	* Image url
	*/
	private String imageUrl;
}