package my.edu.um.umpoint.modules.accommodation.dto;

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
import java.util.Date;


/**
 * Accommodation Tag
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-23
 */
@Data
@Schema(name = "Accommodation Tag")
public class AccTagDTO implements Serializable {
	@Serial
    private static final long serialVersionUID = 5995528915989449681L;

	@SchemaProperty(name = "ID")
	private Long id;

	@SchemaProperty(name = "Tag name")
	@NotEmpty(groups = {DefaultGroup.class})
	private String tagName;

	@SchemaProperty(name = "Accommodation count")
	private Long accommodationCount;
}
