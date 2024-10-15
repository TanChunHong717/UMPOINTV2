package my.edu.um.umpoint.modules.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Data;
import my.edu.um.umpoint.common.validator.group.AddGroup;
import my.edu.um.umpoint.common.validator.group.DefaultGroup;
import my.edu.um.umpoint.common.validator.group.InsertGroup;
import my.edu.um.umpoint.common.validator.group.UpdateGroup;

import java.io.Serial;
import java.io.Serializable;

/**
 * Service Image
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-20
 */
@Data
@Schema(title = "Service Image")
public class SvcImageDTO implements Serializable {
	@Serial
    private static final long serialVersionUID = -1763475943198748177L;

	@Schema(title = "ID")
	private Long id;

	@Schema(title = "Service ID")
	private Long serviceId;

	@Schema(title = "Image url")
	@NotEmpty(groups = {InsertGroup.class})
	private String imageUrl;
}
