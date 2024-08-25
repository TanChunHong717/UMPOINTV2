package my.edu.um.umpoint.modules.accommodation.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * Accommodation Tag
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-23
 */
@Data
@TableName("acc_tag")
public class AccTagEntity {
	/**
	* ID
	*/
	@TableId
	private Long id;
	/**
	* Tag name
	*/
	private String tagName;
	/**
	 * Accommodation Count
	 */
	@TableField(exist = false)
	private Long accommodationCount;
}