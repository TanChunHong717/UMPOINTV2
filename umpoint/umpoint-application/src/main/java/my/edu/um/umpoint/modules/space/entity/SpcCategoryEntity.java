package my.edu.um.umpoint.modules.space.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * Space Category
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-03
 */
@Data
@TableName("spc_category")
public class SpcCategoryEntity {

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
	 * Space Count
	 */
	@TableField(exist = false)
	private Long spaceCount;
}