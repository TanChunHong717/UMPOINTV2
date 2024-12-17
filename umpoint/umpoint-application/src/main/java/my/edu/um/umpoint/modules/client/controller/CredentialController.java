package my.edu.um.umpoint.modules.client.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import my.edu.um.umpoint.common.annotation.LogOperation;
import my.edu.um.umpoint.common.exception.BadHttpRequestException;
import my.edu.um.umpoint.common.exception.ErrorCode;
import my.edu.um.umpoint.common.utils.ConvertUtils;
import my.edu.um.umpoint.common.utils.Result;
import my.edu.um.umpoint.common.validator.ValidatorUtils;
import my.edu.um.umpoint.common.validator.group.AddGroup;
import my.edu.um.umpoint.common.validator.group.DefaultGroup;
import my.edu.um.umpoint.common.validator.group.UpdateGroup;
import my.edu.um.umpoint.modules.client.dto.CliUserDTO;
import my.edu.um.umpoint.modules.client.service.CliUserService;
import my.edu.um.umpoint.modules.security.password.PasswordUtils;
import my.edu.um.umpoint.modules.security.user.SecurityUser;
import my.edu.um.umpoint.modules.security.user.UserDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Client Credential
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-10-01
 */
@RestController
@RequestMapping("cli")
@Tag(name="Client credential")
public class CredentialController {
    @Autowired
    private CliUserService cliUserService;

    @GetMapping("info")
    @Operation(summary = "login user info")
    public Result<CliUserDTO> info(){
        CliUserDTO data = ConvertUtils.sourceToTarget(SecurityUser.getUser(), CliUserDTO.class);
        return new Result<CliUserDTO>().ok(data);
    }

    @PostMapping("register")
    @Operation(summary = "Save")
    @LogOperation("Save")
    public Result save(@RequestBody CliUserDTO dto){
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);
        dto.setStatus(1);
        if (dto.getType().equals("Staff")) {
            dto.setSpacePermission(1);
            dto.setServicePermission(1);
            dto.setAccommodationPermission(1);
        } else if (dto.getType().equals("Student")) {
            dto.setSpacePermission(1);
            dto.setAccommodationPermission(1);
        } else {
            dto.setSpacePermission(1);
            dto.setServicePermission(1);
        }

        cliUserService.save(dto);

        return new Result();
    }

    @PutMapping("profile/{userId}")
    @Operation(summary = "Update User profile")
    @LogOperation("Update")
    public Result updateUser(@PathVariable("userId") Long userId, @RequestBody Map<String, String> request){
        ValidatorUtils.validateEntity(request, UpdateGroup.class, DefaultGroup.class);

        UserDetail loggedInUser = SecurityUser.getUser();
        if (!userId.equals(loggedInUser.getId()))
            throw new BadHttpRequestException(ErrorCode.UNAUTHORIZED);

        CliUserDTO matchedUserDetail = cliUserService.get(loggedInUser.getId());
        if (!PasswordUtils.matches(request.getOrDefault("password", ""), matchedUserDetail.getPassword()))
            throw new BadHttpRequestException(ErrorCode.UNAUTHORIZED, "Current password is incorrect");

        // check if new password empty
        if (request.containsKey("newPassword") && !request.get("newPassword").isEmpty())
            matchedUserDetail.setPassword(PasswordUtils.encode(request.get("newPassword")));

        // check if phone empty
        if (request.containsKey("mobile") && !request.get("mobile").isEmpty())
            matchedUserDetail.setMobile(request.get("mobile"));

        cliUserService.update(matchedUserDetail);

        return new Result();
    }
}
