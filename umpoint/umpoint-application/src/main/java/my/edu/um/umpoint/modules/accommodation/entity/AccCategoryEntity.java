package my.edu.um.umpoint.modules.accommodation.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * Accommodation Category
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-23
 */
@Data
@TableName("acc_category")
public class AccCategoryEntity {
	/**
	* ID
	*/
	@TableId
	private Long id;
	/**
	* Name
	*/
	private String name;
	/**
	 * Accommodation Count
	 */
	@TableField(exist = false)
	private Long accommodationCount;
}