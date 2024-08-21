package my.edu.um.umpoint.modules.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import my.edu.um.umpoint.common.validator.group.DefaultGroup;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;


/**
 * Service tag relationship
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-20
 */
@Data
@Schema(name = "Service tag relationship")
public class SvcServiceTagDTO implements Serializable {
	@Serial
    private static final long serialVersionUID = -5947001415023450228L;

	@SchemaProperty(name = "Service ID")
	@NotNull(groups = DefaultGroup.class)
	private Long serviceId;

	@SchemaProperty(name = "Tag ID")
	@NotNull(groups = DefaultGroup.class)
	private Long tagId;


}
