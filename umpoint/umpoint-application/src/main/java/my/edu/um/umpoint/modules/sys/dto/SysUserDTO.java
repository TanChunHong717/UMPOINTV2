package my.edu.um.umpoint.modules.sys.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import my.edu.um.umpoint.common.validator.group.AddGroup;
import my.edu.um.umpoint.common.validator.group.DefaultGroup;
import my.edu.um.umpoint.common.validator.group.UpdateGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Schema(title = "user management")
public class SysUserDTO implements Serializable {

	@Serial
    private static final long serialVersionUID = -6247407850753989660L;

	@Schema(title = "id")
	@Null(message="{id.null}", groups = AddGroup.class)
	@NotNull(message="{id.require}", groups = UpdateGroup.class)
	private Long id;

	@Schema(title = "username", required = true)
	@NotBlank(message="{sysuser.username.require}", groups = DefaultGroup.class)
	private String username;

	@Schema(title = "password")
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@NotBlank(message="{sysuser.password.require}", groups = AddGroup.class)
	private String password;

	@Schema(title = "name", requiredMode = Schema.RequiredMode.REQUIRED)
	@NotBlank(message="{sysuser.realName.require}", groups = DefaultGroup.class)
	private String realName;

	@Schema(title = "head icon url")
	private String headUrl;

	@Schema(title = "gender 0:male 1:female", required = true)
	@Range(min=0, max=2, message = "{sysuser.gender.range}", groups = DefaultGroup.class)
	private Integer gender;

	@Schema(title = "email")
	@Email(message="{sysuser.email.error}", groups = DefaultGroup.class)
	private String email;

	@Schema(title = "mobike")
	private String mobile;

	@Schema(title = "department ID", required = true)
	@NotNull(message="{sysuser.deptId.require}", groups = DefaultGroup.class)
	private Long deptId;

	@Schema(title = "status 0:suspend 1:normal", required = true)
	@Range(min=0, max=1, message = "{sysuser.status.range}", groups = DefaultGroup.class)
	private Integer status;

	@Schema(title = "create date")
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Date createDate;

	@Schema(title = "super admin 0:no 1:yes")
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Integer superAdmin;

	@Schema(title = "role id list")
	private List<Long> roleIdList;

	@Schema(title = "department name")
	private String deptName;

}
