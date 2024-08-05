package my.edu.um.umpoint.modules.job.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import my.edu.um.umpoint.common.validator.group.AddGroup;
import my.edu.um.umpoint.common.validator.group.DefaultGroup;
import my.edu.um.umpoint.common.validator.group.UpdateGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@Schema(title = "Schedule Job")
public class ScheduleJobDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -36053881056163033L;

    @Schema(title = "id")
    @Null(message="{id.null}", groups = AddGroup.class)
    @NotNull(message="{id.require}", groups = UpdateGroup.class)
    private Long id;

    @Schema(title = "spring bean name")
    @NotBlank(message = "{schedule.bean.require}", groups = DefaultGroup.class)
    private String beanName;

    @Schema(title = "params")
    private String params;

    @Schema(title = "cron expression")
    @NotBlank(message = "{schedule.cron.require}", groups = DefaultGroup.class)
    private String cronExpression;

    @Schema(title = "status 0:Stop 1:Run")
    @Range(min=0, max=1, message = "{schedule.status.range}", groups = DefaultGroup.class)
    private Integer status;

    @Schema(title = "remark")
    private String remark;

    @Schema(title = "create date")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date createDate;

}
