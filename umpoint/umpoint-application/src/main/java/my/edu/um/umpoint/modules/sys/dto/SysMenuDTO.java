package my.edu.um.umpoint.modules.sys.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import my.edu.um.umpoint.common.utils.TreeNode;
import my.edu.um.umpoint.common.validator.group.AddGroup;
import my.edu.um.umpoint.common.validator.group.DefaultGroup;
import my.edu.um.umpoint.common.validator.group.UpdateGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Range;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(title = "menu management")
public class SysMenuDTO extends TreeNode<SysMenuDTO> implements Serializable {

	@Serial
    private static final long serialVersionUID = 7579631684856540067L;

	@Schema(title = "id")
	@Null(message="{id.null}", groups = AddGroup.class)
	@NotNull(message="{id.require}", groups = UpdateGroup.class)
	private Long id;

	@Schema(title = "parent ID")
	@NotNull(message="{sysmenu.pid.require}", groups = DefaultGroup.class)
	private Long pid;

	@Schema(title = "name")
	@NotBlank(message="sysmenu.name.require", groups = DefaultGroup.class)
	private String name;

	@Schema(title = "menu URL")
	private String url;

	@Schema(title = "menu type 0:menu 1:button")
	@Range(min=0, max=1, message = "{sysmenu.type.range}", groups = DefaultGroup.class)
	private Integer menuType;

	@Schema(title = "icon")
	private String icon;

	@Schema(title = "permission(separate by comma, like: sys:user:list,sys:user:save)")
	private String permissions;

	@Schema(title = "sort")
	@Min(value = 0, message = "{sort.number}", groups = DefaultGroup.class)
	private Integer sort;

	@Schema(title = "create date")
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Date createDate;

	@Schema(title = "parent name")
	private String parentName;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public Long getPid() {
		return pid;
	}

	@Override
	public void setPid(Long pid) {
		this.pid = pid;
	}

}
