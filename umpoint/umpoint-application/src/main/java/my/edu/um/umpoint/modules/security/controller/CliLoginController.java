package my.edu.um.umpoint.modules.security.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import my.edu.um.umpoint.common.exception.ErrorCode;
import my.edu.um.umpoint.common.exception.RenException;
import my.edu.um.umpoint.common.utils.IpUtils;
import my.edu.um.umpoint.common.utils.Result;
import my.edu.um.umpoint.common.validator.AssertUtils;
import my.edu.um.umpoint.common.validator.ValidatorUtils;
import my.edu.um.umpoint.modules.client.dto.CliUserDTO;
import my.edu.um.umpoint.modules.client.service.CliTokenService;
import my.edu.um.umpoint.modules.client.service.CliUserService;
import my.edu.um.umpoint.modules.log.entity.SysLogLoginEntity;
import my.edu.um.umpoint.modules.log.enums.LoginOperationEnum;
import my.edu.um.umpoint.modules.log.enums.LoginStatusEnum;
import my.edu.um.umpoint.modules.log.service.SysLogLoginService;
import my.edu.um.umpoint.modules.security.dto.LoginDTO;
import my.edu.um.umpoint.modules.security.password.PasswordUtils;
import my.edu.um.umpoint.modules.security.service.CaptchaService;
import my.edu.um.umpoint.modules.security.service.SysUserTokenService;
import my.edu.um.umpoint.modules.security.user.SecurityUser;
import my.edu.um.umpoint.modules.security.user.UserDetail;
import my.edu.um.umpoint.modules.sys.dto.SysUserDTO;
import my.edu.um.umpoint.modules.sys.enums.UserStatusEnum;
import my.edu.um.umpoint.modules.sys.service.SysUserService;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Date;

@RestController
@Tag(name = "Client Login Controller")
@AllArgsConstructor
public class CliLoginController {
    private final CliTokenService cliTokenService;
    private final CliUserService cliUserService;

    @PostMapping("cli/login")
    @Operation(summary = "Login")
    public Result clientLogin(@RequestBody LoginDTO login){
        CliUserDTO user = cliUserService.getByUsername(login.getUsername());
        if (user == null || !PasswordUtils.matches(login.getPassword(), user.getPassword()))
            throw new RenException(ErrorCode.ACCOUNT_PASSWORD_ERROR);
        if (user.getStatus() == UserStatusEnum.DISABLE.value())
            throw new RenException(ErrorCode.ACCOUNT_DISABLE);

        return cliTokenService.createToken(user.getId());
    }

    @PostMapping("cli/logout")
    @Operation(summary = "logout")
    public Result clientLogout(){
        UserDetail user = SecurityUser.getUser();

        cliTokenService.logout(user.getId());

        return new Result();
    }
}
