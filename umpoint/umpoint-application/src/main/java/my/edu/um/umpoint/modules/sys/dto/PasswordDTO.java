package my.edu.um.umpoint.modules.sys.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@Schema(title = "change password")
public class PasswordDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 954777639434092954L;

    @Schema(title = "original password")
    @NotBlank(message="{sysuser.password.require}")
    private String password;

    @Schema(title = "new password")
    @NotBlank(message="{sysuser.password.require}")
    private String newPassword;

}
