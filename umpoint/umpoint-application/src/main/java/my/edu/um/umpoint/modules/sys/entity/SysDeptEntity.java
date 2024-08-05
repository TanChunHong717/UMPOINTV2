package my.edu.um.umpoint.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.*;
import my.edu.um.umpoint.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sys_dept")
public class SysDeptEntity extends BaseEntity {

	@Serial
	private static final long serialVersionUID = 8059379798908945389L;

	private Long pid;

	private String pids;

	private String name;

	private Integer sort;

	@TableField(fill = FieldFill.INSERT_UPDATE)
	private Long updater;

	@TableField(fill = FieldFill.INSERT_UPDATE)
	private Date updateDate;

	@TableField(exist = false)
	private String parentName;

}