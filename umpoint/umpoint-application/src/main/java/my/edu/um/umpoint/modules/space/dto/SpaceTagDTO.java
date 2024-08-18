package my.edu.um.umpoint.modules.space.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import my.edu.um.umpoint.common.validator.group.DefaultGroup;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;


/**
 * Space tag relationship
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-03
 */
@Data
@Schema(name = "Space tag relationship")
public class SpaceTagDTO implements Serializable {
	@Serial
    private static final long serialVersionUID = 4886806046843501106L;

	@SchemaProperty(name = "Space ID")
	@NotNull(groups = DefaultGroup.class)
	private Long spaceId;

	@SchemaProperty(name = "Tag ID")
	@NotNull(groups = DefaultGroup.class)
	private Long tagId;

}
