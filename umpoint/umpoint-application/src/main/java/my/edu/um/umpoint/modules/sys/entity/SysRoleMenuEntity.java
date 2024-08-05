package my.edu.um.umpoint.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import my.edu.um.umpoint.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sys_role_menu")
public class SysRoleMenuEntity extends BaseEntity {

	@Serial
	private static final long serialVersionUID = 1522063060817899478L;

	private Long roleId;

	private Long menuId;

}
