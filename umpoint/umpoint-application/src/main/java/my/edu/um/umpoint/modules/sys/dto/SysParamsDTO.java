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

@Data
@Schema(title = "params management")
public class SysParamsDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -3808601957414792102L;

    @Schema(title = "id")
    @Null(message="{id.null}", groups = AddGroup.class)
    @NotNull(message="{id.require}", groups = UpdateGroup.class)
    private Long id;

    @Schema(title = "param code")
    @NotBlank(message="{sysparams.paramcode.require}", groups = DefaultGroup.class)
    private String paramCode;

    @Schema(title = "param value")
    @NotBlank(message="{sysparams.paramvalue.require}", groups = DefaultGroup.class)
    private String paramValue;

    @Schema(title = "remark")
    private String remark;

    @Schema(title = "create date")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date createDate;

    @Schema(title = "update date")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date updateDate;

}
