package my.edu.um.umpoint.modules.security.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import my.edu.um.umpoint.common.entity.BaseEntity;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@TableName("sys_user_token")
public class SysUserTokenEntity implements Serializable {

	@Serial
	private static final long serialVersionUID = -1473601376278180057L;

	@TableId
	private Long id;

	private Long userId;

	private String token;

	private Date expireDate;

	private Date updateDate;

	@TableField(fill = FieldFill.INSERT)
	private Date createDate;

}