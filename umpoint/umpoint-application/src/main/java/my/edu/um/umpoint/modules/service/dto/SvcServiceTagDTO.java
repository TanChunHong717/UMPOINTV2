package my.edu.um.umpoint.modules.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * Service tag relationship
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-20
 */
@Data
@Schema(title = "Service tag relationship")
public class SvcServiceTagDTO implements Serializable {
	@Serial
    private static final long serialVersionUID = -5947001415023450228L;

	@Schema(title = "Service ID")
	private Long serviceId;

	@Schema(title = "Tag ID")
	private Long tagId;
}
