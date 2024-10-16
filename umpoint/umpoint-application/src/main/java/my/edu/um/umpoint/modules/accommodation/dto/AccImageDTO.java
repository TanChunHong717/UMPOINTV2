package my.edu.um.umpoint.modules.accommodation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import my.edu.um.umpoint.common.validator.group.DefaultGroup;
import my.edu.um.umpoint.common.validator.group.InsertGroup;

import java.io.Serial;
import java.io.Serializable;

/**
 * Accommodation Image
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-23
 */
@Data
@Schema(title = "Accommodation Image")
public class AccImageDTO implements Serializable {
	@Serial
    private static final long serialVersionUID = -8677818412532826317L;

	@Schema(title = "ID")
	private Long id;

	@Schema(title = "Accommodation ID")
	@NotNull(groups = DefaultGroup.class)
	private Long accommodationId;

	@Schema(title = "Image url")
	@NotEmpty(groups = {DefaultGroup.class, InsertGroup.class})
	private String imageUrl;
}
