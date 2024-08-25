package my.edu.um.umpoint.modules.space.dto;

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
 * Space Tag
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-03
 */
@Data
@Schema(name = "Space Tag")
public class SpcTagDTO implements Serializable {
	@Serial
    private static final long serialVersionUID = -6417179702850712420L;

	@SchemaProperty(name = "ID")
	@Null(groups = {AddGroup.class})
	@NotNull(groups = {UpdateGroup.class, InsertGroup.class})
	private Long id;

	@SchemaProperty(name = "Tag name")
	@NotEmpty(groups = {DefaultGroup.class})
	private String tagName;

	@SchemaProperty(name = "Space count")
	private Long spaceCount;
}