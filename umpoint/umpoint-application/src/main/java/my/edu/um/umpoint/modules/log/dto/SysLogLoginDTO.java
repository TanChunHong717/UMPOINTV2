package my.edu.um.umpoint.modules.log.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@Schema(title = "login log")
public class SysLogLoginDTO implements Serializable {

	@Serial
    private static final long serialVersionUID = 4465501359408634982L;

	@Schema(title = "id")
	private Long id;

	@Schema(title = "operation 0:login 1:logout")
	private Integer operation;

	@Schema(title = "status 0:failed 1:success 2:account lock")
	private Integer status;

	@Schema(title = "user agent")
	private String userAgent;

	@Schema(title = "IP")
	private String ip;

	@Schema(title = "username")
	private String creatorName;

	@Schema(title = "create date")
	private Date createDate;

}
