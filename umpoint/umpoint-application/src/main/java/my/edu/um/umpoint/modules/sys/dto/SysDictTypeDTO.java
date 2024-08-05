package my.edu.um.umpoint.modules.sys.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import my.edu.um.umpoint.common.validator.group.AddGroup;
import my.edu.um.umpoint.common.validator.group.DefaultGroup;
import my.edu.um.umpoint.common.validator.group.UpdateGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@Schema(title = "dict type")
public class SysDictTypeDTO implements Serializable {

	@Serial
    private static final long serialVersionUID = 990673513081622663L;

	@Schema(title = "id")
	@Null(message="{id.null}", groups = AddGroup.class)
	@NotNull(message="{id.require}", groups = UpdateGroup.class)
	private Long id;

	@Schema(title = "dict type")
	@NotBlank(message="{sysdict.type.require}", groups = DefaultGroup.class)
	private String dictType;

	@Schema(title = "dict name")
	@NotBlank(message="{sysdict.name.require}", groups = DefaultGroup.class)
	private String dictName;

	@Schema(title = "remark")
	private String remark;

	@Schema(title = "sort")
	@Min(value = 0, message = "{sort.number}", groups = DefaultGroup.class)
	private Integer sort;

	@Schema(title = "create date")
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Date createDate;

	@Schema(title = "update date")
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Date updateDate;
}
