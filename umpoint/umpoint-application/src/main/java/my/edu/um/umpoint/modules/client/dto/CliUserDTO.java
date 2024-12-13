package my.edu.um.umpoint.modules.client.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(title = "User")
public class CliUserDTO implements Serializable {
	@Serial
    private static final long serialVersionUID = 6325567844814722921L;

	@Schema(title = "ID")
	@Null(groups = AddGroup.class)
	@NotNull(groups = UpdateGroup.class)
	private Long id;

	@Schema(title = "Status, 0:Disable, 1:Enabled")
	private Integer status;

	@Schema(title = "Username")
	@NotEmpty(groups = DefaultGroup.class)
	private String username;

	@Schema(title = "Real Name")
	@NotEmpty(groups = DefaultGroup.class)
	private String realName;

	@Schema(title = "Password")
	@NotEmpty(groups = DefaultGroup.class)
	private String password;

	@Schema(title = "Type")
	@NotEmpty(groups = DefaultGroup.class)
	private String type;

	@Schema(title = "Mobile")
	@NotEmpty(groups = DefaultGroup.class)
	private String mobile;

	@Schema(title = "Email")
	@NotEmpty(groups = DefaultGroup.class)
	private String email;

	@Schema(title = "Address")
	@NotEmpty(groups = DefaultGroup.class)
	private String address;

	@Schema(title = "Matric Number")
	@NotEmpty(groups = DefaultGroup.class)
	private String matricNumber;

	@Schema(title = "Have permission to book space")
	private Integer spacePermission;

	@Schema(title = "Have permission to book service")
	private Integer servicePermission;

	@Schema(title = "Have permission to book accommodation")
	private Integer accommodationPermission;

	@Schema(title = "Create date")
	private Date createDate;
}
