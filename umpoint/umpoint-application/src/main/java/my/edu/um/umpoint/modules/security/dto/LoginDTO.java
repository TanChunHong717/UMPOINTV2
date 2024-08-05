package my.edu.um.umpoint.modules.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@Schema(title = "登录表单")
public class LoginDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1244986875041980916L;

    @Schema(title = "username", required = true)
    @NotBlank(message="{sysuser.username.require}")
    private String username;

    @Schema(title = "password")
    @NotBlank(message="{sysuser.password.require}")
    private String password;

    @Schema(title = "captcha")
    @NotBlank(message="{sysuser.captcha.require}")
    private String captcha;

    @Schema(title = "uuid")
    @NotBlank(message="{sysuser.uuid.require}")
    private String uuid;

}
