package my.edu.um.umpoint.modules.space.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * Space Image
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-03
 */
@Data
@TableName("spc_image")
public class SpcImageEntity {

    /**
     * ID
     */
	@TableId
	private Long id;
    /**
     * Space ID
     */
	private Long spaceId;
    /**
     * Image url
     */
	private String imageUrl;
}