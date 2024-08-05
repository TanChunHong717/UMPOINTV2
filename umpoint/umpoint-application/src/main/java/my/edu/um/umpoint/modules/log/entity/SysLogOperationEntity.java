package my.edu.um.umpoint.modules.log.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import my.edu.um.umpoint.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sys_log_operation")
public class SysLogOperationEntity extends BaseEntity {

	@Serial
	private static final long serialVersionUID = -3202672928464176242L;

	private String operation;

	private String requestUri;

	private String requestMethod;

	private String requestParams;

	private Integer requestTime;

	private String userAgent;

	private String ip;

	private Integer status;

	private String creatorName;
}