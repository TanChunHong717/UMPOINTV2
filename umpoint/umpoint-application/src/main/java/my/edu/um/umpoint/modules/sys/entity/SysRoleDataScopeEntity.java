package my.edu.um.umpoint.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import my.edu.um.umpoint.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sys_role_data_scope")
public class SysRoleDataScopeEntity extends BaseEntity {

	@Serial
	private static final long serialVersionUID = 4100432544592075822L;

	private Long roleId;

	private Long deptId;

}