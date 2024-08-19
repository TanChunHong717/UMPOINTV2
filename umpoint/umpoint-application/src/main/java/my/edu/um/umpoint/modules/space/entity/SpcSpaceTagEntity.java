package my.edu.um.umpoint.modules.space.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * Space tag relationship
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-03
 */
@Data
@TableName("spc_space_tag")
public class SpcSpaceTagEntity {

    /**
     * Space ID
     */
		@TableId
		private Long spaceId;
    /**
     * Tag ID
     */
		private Long tagId;
}