package my.edu.um.umpoint.modules.job.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import my.edu.um.umpoint.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper=false)
@TableName("schedule_job")
public class ScheduleJobEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;

	private String beanName;

	private String params;

	private String cronExpression;

	private Integer status;

	private String remark;

	@TableField(fill = FieldFill.INSERT_UPDATE)
	private Long updater;

	@TableField(fill = FieldFill.INSERT_UPDATE)
	private Date updateDate;
}