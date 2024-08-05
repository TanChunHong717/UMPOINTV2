package my.edu.um.umpoint.modules.log.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@Schema(title = "error log")
public class SysLogErrorDTO implements Serializable {

	@Serial
    private static final long serialVersionUID = -7616883403340856719L;

	@Schema(title = "id")
	private Long id;

	@Schema(title = "request URI")
	private String requestUri;

	@Schema(title = "request method")
	private String requestMethod;

	@Schema(title = "request params")
	private String requestParams;

	@Schema(title = "user agent")
	private String userAgent;

	@Schema(title = "ip")
	private String ip;

	@Schema(title = "error info")
	private String errorInfo;

	@Schema(title = "create date")
	private Date createDate;

}
