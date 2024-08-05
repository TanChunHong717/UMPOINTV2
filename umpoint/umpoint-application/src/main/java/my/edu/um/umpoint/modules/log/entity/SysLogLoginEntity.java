package my.edu.um.umpoint.modules.log.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import my.edu.um.umpoint.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sys_log_login")
public class SysLogLoginEntity extends BaseEntity {
	@Serial
	private static final long serialVersionUID = 4946332011638681133L;

	private Integer operation;

	private Integer status;

	private String userAgent;

	private String ip;

	private String creatorName;

}