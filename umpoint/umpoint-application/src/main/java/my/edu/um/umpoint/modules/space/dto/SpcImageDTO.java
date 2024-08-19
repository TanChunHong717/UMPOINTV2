package my.edu.um.umpoint.modules.space.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Data;
import my.edu.um.umpoint.common.validator.group.AddGroup;
import my.edu.um.umpoint.common.validator.group.DefaultGroup;
import my.edu.um.umpoint.common.validator.group.UpdateGroup;

import java.io.Serial;
import java.io.Serializable;

/**
 * Space Image
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-03
 */
@Data
@Schema(name = "Space Image")
public class SpcImageDTO implements Serializable {
	@Serial
    private static final long serialVersionUID = -3389885882502784485L;

	@SchemaProperty(name = "ID")
	@Null(groups = AddGroup.class)
	@NotNull(groups = UpdateGroup.class)
	private Long id;

	@SchemaProperty(name = "Space ID")
	@NotNull(groups = DefaultGroup.class)
	private Long spaceId;

	@SchemaProperty(name = "Image url")
	@NotNull(groups = DefaultGroup.class)
	private String imageUrl;

}
