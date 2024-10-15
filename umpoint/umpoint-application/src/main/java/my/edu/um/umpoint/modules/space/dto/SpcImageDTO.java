package my.edu.um.umpoint.modules.space.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import my.edu.um.umpoint.common.validator.group.InsertGroup;

import java.io.Serial;
import java.io.Serializable;

/**
 * Space Image
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-03
 */
@Data
@Schema(title = "Space Image")
public class SpcImageDTO implements Serializable {
	@Serial
    private static final long serialVersionUID = -3389885882502784485L;

	@Schema(title = "ID")
	private Long id;

	@Schema(title = "Space ID")
	private Long spaceId;

	@Schema(title = "Image url")
	@NotEmpty(groups = {InsertGroup.class})
	private String imageUrl;
}
