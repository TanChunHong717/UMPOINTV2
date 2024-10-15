package my.edu.um.umpoint.modules.client.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;


/**
 * UserToken
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-10-01
 */
@Data
@Schema(title = "UserToken")
public class CliTokenDTO implements Serializable {
	@Serial
    private static final long serialVersionUID = -719366732130846716L;

	@Schema(title = "ID")
	private Long id;

	@Schema(title = "User ID")
	private Long userId;

	@Schema(title = "token")
	private String token;

	@Schema(title = "Expired date")
	private Date expireDate;

	@Schema(title = "Update date")
	private Date updateDate;
}
