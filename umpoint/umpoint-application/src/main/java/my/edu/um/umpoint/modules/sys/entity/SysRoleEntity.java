package my.edu.um.umpoint.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import jdk.jfr.SettingDefinition;
import my.edu.um.umpoint.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sys_role")
public class SysRoleEntity extends BaseEntity {

	@Serial
	private static final long serialVersionUID = 3963982856335604079L;

	private String name;

	private String remark;

	@TableField(fill = FieldFill.INSERT)
	private Long deptId;

	@TableField(fill = FieldFill.INSERT_UPDATE)
	private Long updater;

	@TableField(fill = FieldFill.INSERT_UPDATE)
	private Date updateDate;
}
