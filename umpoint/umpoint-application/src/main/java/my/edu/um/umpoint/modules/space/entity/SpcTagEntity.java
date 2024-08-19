package my.edu.um.umpoint.modules.space.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * Space Tag
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-03
 */
@Data
@TableName("spc_tag")
public class SpcTagEntity {
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
	 * Space Count
	 */
	@TableField(exist = false)
	private Long spaceCount;
}