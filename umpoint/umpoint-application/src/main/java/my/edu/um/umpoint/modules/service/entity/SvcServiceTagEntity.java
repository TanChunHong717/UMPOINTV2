package my.edu.um.umpoint.modules.service.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * Service tag relationship
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-20
 */
@Data
@TableName("svc_service_tag")
public class SvcServiceTagEntity {

	/**
	* Service ID
	*/
	@TableId
	private Long serviceId;
	/**
	* Tag ID
	*/
	private Long tagId;
}