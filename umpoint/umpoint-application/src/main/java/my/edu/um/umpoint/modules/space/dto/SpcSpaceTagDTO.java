package my.edu.um.umpoint.modules.space.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import my.edu.um.umpoint.common.validator.group.DefaultGroup;

import java.io.Serial;
import java.io.Serializable;


/**
 * Space tag relationship
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-03
 */
@Data
@Schema(title = "Space tag relationship")
public class SpcSpaceTagDTO implements Serializable {
	@Serial
    private static final long serialVersionUID = 4886806046843501106L;

	@Schema(title = "Space ID")
	private Long spaceId;

	@Schema(title = "Tag ID")
	private Long tagId;

}
