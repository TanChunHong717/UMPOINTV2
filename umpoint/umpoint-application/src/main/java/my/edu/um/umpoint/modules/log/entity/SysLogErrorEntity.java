package my.edu.um.umpoint.modules.log.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import my.edu.um.umpoint.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sys_log_error")
public class SysLogErrorEntity extends BaseEntity {

	@Serial
	private static final long serialVersionUID = -404957136989602991L;

	private String requestUri;

	private String requestMethod;

	private String requestParams;

	private String userAgent;

	private String ip;

	private String errorInfo;

}