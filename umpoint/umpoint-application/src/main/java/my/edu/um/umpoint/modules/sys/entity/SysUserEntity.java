package my.edu.um.umpoint.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import my.edu.um.umpoint.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sys_user")
public class SysUserEntity extends BaseEntity {

	@Serial
	private static final long serialVersionUID = 1929165859514964145L;

	private String username;

	private String password;

	private String realName;

	private String headUrl;

	private Integer gender;

	private String email;

	private String mobile;

	private Long deptId;

	private Integer superAdmin;

	private Integer status;

	@TableField(fill = FieldFill.INSERT_UPDATE)
	private Long updater;

	@TableField(fill = FieldFill.INSERT_UPDATE)
	private Date updateDate;

	@TableField(exist=false)
	private String deptName;

}