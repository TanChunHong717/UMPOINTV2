package my.edu.um.umpoint.modules.client.dto;

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
 * User
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-10-01
 */
@Data
@Schema(name = "User")
public class CliUserDTO implements Serializable {
	@Serial
    private static final long serialVersionUID = 6325567844814722921L;

	@SchemaProperty(name = "ID")
	@Null(groups = AddGroup.class)
	@NotNull(groups = UpdateGroup.class)
	private Long id;

	@SchemaProperty(name = "Username")
	@NotEmpty(groups = DefaultGroup.class)
	private String username;

	@SchemaProperty(name = "Mobile")
	@NotEmpty(groups = DefaultGroup.class)
	private String mobile;

	@SchemaProperty(name = "Password")
	@NotEmpty(groups = DefaultGroup.class)
	private String password;

	@SchemaProperty(name = "Email")
	@NotEmpty(groups = DefaultGroup.class)
	private String email;

	@SchemaProperty(name = "Type")
	private String type;

	@SchemaProperty(name = "Have permission to book space")
	private Integer spacePermission;

	@SchemaProperty(name = "Have permission to book service")
	private String servicePermission;

	@SchemaProperty(name = "Have permission to book accommodation")
	private String accommodationPermission;

	@SchemaProperty(name = "Create date")
	private Date createDate;
}
