package my.edu.um.umpoint.modules.accommodation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Data;
import my.edu.um.umpoint.common.validator.group.AddGroup;
import my.edu.um.umpoint.common.validator.group.DefaultGroup;
import my.edu.um.umpoint.common.validator.group.UpdateGroup;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;


/**
 * Accommodation Image
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-23
 */
@Data
@Schema(name = "Accommodation Image")
public class AccImageDTO implements Serializable {
	@Serial
    private static final long serialVersionUID = -8677818412532826317L;

	@SchemaProperty(name = "ID")
	@Null(groups = AddGroup.class)
	@NotNull(groups = UpdateGroup.class)
	private Long id;

	@SchemaProperty(name = "Accommodation ID")
	@NotNull(groups = DefaultGroup.class)
	private Long accommodationId;

	@SchemaProperty(name = "Image url")
	@NotEmpty(groups = DefaultGroup.class)
	private String imageUrl;
}
