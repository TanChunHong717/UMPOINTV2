package my.edu.um.umpoint.modules.accommodation.dto;

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
import java.util.Date;


/**
 * Accommodation Category
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-23
 */
@Data
@Schema(title = "Accommodation Category")
public class AccCategoryDTO implements Serializable {
	@Serial
    private static final long serialVersionUID = -7860476673705920334L;

	@Schema(title = "ID")
	@Null(groups = {AddGroup.class})
	@NotNull(groups = {UpdateGroup.class})
	private Long id;

	@Schema(title = "Name")
	@NotNull(groups = {DefaultGroup.class})
	private String name;

	@Schema(title = "Accommodation count")
	private Long accommodationCount;
}
