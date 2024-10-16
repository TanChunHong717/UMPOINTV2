package my.edu.um.umpoint.modules.accommodation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import my.edu.um.umpoint.common.validator.group.DefaultGroup;

import java.io.Serial;
import java.io.Serializable;


/**
 * Accommodation Tag
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-23
 */
@Data
@Schema(title = "Accommodation Tag")
public class AccTagDTO implements Serializable {
	@Serial
    private static final long serialVersionUID = 5995528915989449681L;

	@Schema(title = "ID")
	private Long id;

	@Schema(title = "Tag name")
	@NotEmpty(groups = {DefaultGroup.class})
	private String tagName;

	@Schema(title = "Accommodation count")
	private Long accommodationCount;
}
