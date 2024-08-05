package my.edu.um.umpoint.modules.sys.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import my.edu.um.umpoint.common.validator.group.AddGroup;
import my.edu.um.umpoint.common.validator.group.DefaultGroup;
import my.edu.um.umpoint.common.validator.group.UpdateGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Schema(title = "role management")
public class SysRoleDTO implements Serializable {

	@Serial
    private static final long serialVersionUID = -4357045472156537619L;

	@Schema(title = "id")
	@Null(message="{id.null}", groups = AddGroup.class)
	@NotNull(message="{id.require}", groups = UpdateGroup.class)
	private Long id;

	@Schema(title = "name")
	@NotBlank(message="{sysrole.name.require}", groups = DefaultGroup.class)
	private String name;

	@Schema(title = "remark")
	private String remark;

	@Schema(title = "create date")
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Date createDate;

	@Schema(title = "menu id list")
	private List<Long> menuIdList;

	@Schema(title = "dept id list")
	private List<Long> deptIdList;

}
