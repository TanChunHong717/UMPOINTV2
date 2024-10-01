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
@Schema(name = "UserToken")
public class CliTokenDTO implements Serializable {
	@Serial
    private static final long serialVersionUID = -719366732130846716L;

	@SchemaProperty(name = "ID")
	private Long id;

	@SchemaProperty(name = "User ID")
	private Long userId;

	@SchemaProperty(name = "token")
	private String token;

	@SchemaProperty(name = "Expired date")
	private Date expireDate;

	@SchemaProperty(name = "Update date")
	private Date updateDate;
}
