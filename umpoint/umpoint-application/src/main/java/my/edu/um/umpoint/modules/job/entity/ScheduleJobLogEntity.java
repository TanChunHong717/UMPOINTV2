package my.edu.um.umpoint.modules.job.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("schedule_job_log")
public class ScheduleJobLogEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableId
	private Long id;

	private Long jobId;

	private String beanName;

	private String params;

	private Integer status;

	private String error;

	private Integer times;

	private Date createDate;

}