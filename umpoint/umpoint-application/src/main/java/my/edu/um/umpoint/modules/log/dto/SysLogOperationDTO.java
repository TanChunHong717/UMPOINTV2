package my.edu.um.umpoint.modules.log.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@Schema(title = "operation log")
public class SysLogOperationDTO implements Serializable {

	@Serial
    private static final long serialVersionUID = 7553003749818425440L;

	@Schema(title = "id")
	private Long id;

	@Schema(title = "operation")
	private String operation;

	@Schema(title = "request URI")
	private String requestUri;

	@Schema(title = "request method")
	private String requestMethod;

	@Schema(title = "params")
	private String requestParams;

	@Schema(title = "times(ms)")
	private Integer requestTime;

	@Schema(title = "user agent")
	private String userAgent;

	@Schema(title = "IP")
	private String ip;

	@Schema(title = "status 0:failed 1:success")
	private Integer status;

	@Schema(title = "username")
	private String creatorName;

	@Schema(title = "create date")
	private Date createDate;

}
