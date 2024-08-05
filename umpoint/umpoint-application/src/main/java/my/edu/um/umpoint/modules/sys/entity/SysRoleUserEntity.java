package my.edu.um.umpoint.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import my.edu.um.umpoint.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sys_role_user")
public class SysRoleUserEntity extends BaseEntity {

	@Serial
	private static final long serialVersionUID = 8359226079557957419L;

	private Long roleId;

	private Long userId;

}
