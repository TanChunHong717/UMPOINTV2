package my.edu.um.umpoint.modules.job.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@Schema(title = "Schedule Job Log")
public class ScheduleJobLogDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -6765306551122816895L;

    @Schema(title = "id")
    private Long id;

    @Schema(title = "job id")
    private Long jobId;

    @Schema(title = "spring bean name")
    private String beanName;

    @Schema(title = "params")
    private String params;

    @Schema(title = "status 0:failed 1:success")
    private Integer status;

    @Schema(title = "error")
    private String error;

    @Schema(title = "times(ms)")
    private Integer times;

    @Schema(title = "create date")
    private Date createDate;

}
