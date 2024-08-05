package my.edu.um.umpoint.modules.sys.controller;

import my.edu.um.umpoint.common.annotation.LogOperation;
import my.edu.um.umpoint.common.constant.Constant;
import my.edu.um.umpoint.common.exception.ErrorCode;
import my.edu.um.umpoint.common.page.PageData;
import my.edu.um.umpoint.common.utils.ConvertUtils;
import my.edu.um.umpoint.common.utils.ExcelUtils;
import my.edu.um.umpoint.common.utils.Result;
import my.edu.um.umpoint.common.validator.AssertUtils;
import my.edu.um.umpoint.common.validator.ValidatorUtils;
import my.edu.um.umpoint.common.validator.group.AddGroup;
import my.edu.um.umpoint.common.validator.group.DefaultGroup;
import my.edu.um.umpoint.common.validator.group.UpdateGroup;
import my.edu.um.umpoint.modules.security.password.PasswordUtils;
import my.edu.um.umpoint.modules.security.user.SecurityUser;
import my.edu.um.umpoint.modules.security.user.UserDetail;
import my.edu.um.umpoint.modules.sys.dto.PasswordDTO;
import my.edu.um.umpoint.modules.sys.dto.SysUserDTO;
import my.edu.um.umpoint.modules.sys.excel.SysUserExcel;
import my.edu.um.umpoint.modules.sys.service.SysRoleUserService;
import my.edu.um.umpoint.modules.sys.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sys/user")
@Tag(name = "user management")
@AllArgsConstructor
public class SysUserController {
    private final SysUserService sysUserService;
    private final SysRoleUserService sysRoleUserService;

    @GetMapping("page")
    @Operation(summary = "page")
    @Parameters({
            @Parameter(name = Constant.PAGE, description = "Current page number, starting from 1", in = ParameterIn.QUERY, required = true, ref="int") ,
            @Parameter(name = Constant.LIMIT, description = "Number of records per page", in = ParameterIn.QUERY,required = true, ref="int") ,
            @Parameter(name = Constant.ORDER_FIELD, description = "Sort field", in = ParameterIn.QUERY, ref="String") ,
            @Parameter(name = Constant.ORDER, description = "Sort order, optional values (asc, desc)", in = ParameterIn.QUERY, ref="String"),
            @Parameter(name = "username", description = "Username", in = ParameterIn.QUERY, ref = "String"),
            @Parameter(name = "gender", description = "Gender", in = ParameterIn.QUERY, ref = "String"),
            @Parameter(name = "deptId", description = "Department ID", in = ParameterIn.QUERY, ref = "String")
    })
    @RequiresPermissions("sys:user:page")
    public Result<PageData<SysUserDTO>> page(@Parameter(hidden = true) @RequestParam Map<String, Object> params) {
        PageData<SysUserDTO> page = sysUserService.page(params);

        return new Result<PageData<SysUserDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @Operation(summary = "info")
    @RequiresPermissions("sys:user:info")
    public Result<SysUserDTO> get(@PathVariable("id") Long id) {
        SysUserDTO data = sysUserService.get(id);

        List<Long> roleIdList = sysRoleUserService.getRoleIdList(id);
        data.setRoleIdList(roleIdList);

        return new Result<SysUserDTO>().ok(data);
    }

    @GetMapping("info")
    @Operation(summary = "login user info")
    public Result<SysUserDTO> info() {
        SysUserDTO data = ConvertUtils.sourceToTarget(SecurityUser.getUser(), SysUserDTO.class);
        return new Result<SysUserDTO>().ok(data);
    }

    @PutMapping("password")
    @Operation(summary = "change password")
    @LogOperation("change password")
    public Result password(@RequestBody PasswordDTO dto) {
        ValidatorUtils.validateEntity(dto);

        UserDetail user = SecurityUser.getUser();

        if (!PasswordUtils.matches(dto.getPassword(), user.getPassword())) {
            return new Result().error(ErrorCode.PASSWORD_ERROR);
        }

        sysUserService.updatePassword(user.getId(), dto.getNewPassword());

        return new Result();
    }

    @PostMapping
    @Operation(summary = "save")
    @LogOperation("save")
    @RequiresPermissions("sys:user:save")
    public Result save(@RequestBody SysUserDTO dto) {
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        sysUserService.save(dto);

        return new Result();
    }

    @PutMapping
    @Operation(summary = "update")
    @LogOperation("update")
    @RequiresPermissions("sys:user:update")
    public Result update(@RequestBody SysUserDTO dto) {
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        sysUserService.update(dto);

        return new Result();
    }

    @DeleteMapping
    @Operation(summary = "delete")
    @LogOperation("delete")
    @RequiresPermissions("sys:user:delete")
    public Result delete(@RequestBody Long[] ids) {
        AssertUtils.isArrayEmpty(ids, "id");

        sysUserService.deleteBatchIds(Arrays.asList(ids));

        return new Result();
    }

    @GetMapping("export")
    @Operation(summary = "export")
    @LogOperation("export")
    @RequiresPermissions("sys:user:export")
    @Parameter(name = "username", description = "username", in = ParameterIn.QUERY, ref = "String")
    public void export(@Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        List<SysUserDTO> list = sysUserService.list(params);

        ExcelUtils.exportExcelToTarget(response, null, "User management", list, SysUserExcel.class);
    }
}
