package my.edu.um.umpoint.modules.service.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * Service Tag
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-20
 */
@Data
@TableName("svc_tag")
public class SvcTagEntity {
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
	 * Service Count
	 */
	@TableField(exist = false)
	private Long serviceCount;
}