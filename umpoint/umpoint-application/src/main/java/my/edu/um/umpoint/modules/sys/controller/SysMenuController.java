package my.edu.um.umpoint.modules.sys.controller;

import io.swagger.v3.oas.annotations.media.Schema;
import my.edu.um.umpoint.common.annotation.LogOperation;
import my.edu.um.umpoint.common.exception.ErrorCode;
import my.edu.um.umpoint.common.utils.Result;
import my.edu.um.umpoint.common.validator.AssertUtils;
import my.edu.um.umpoint.common.validator.ValidatorUtils;
import my.edu.um.umpoint.common.validator.group.DefaultGroup;
import my.edu.um.umpoint.modules.security.service.ShiroService;
import my.edu.um.umpoint.modules.security.user.SecurityUser;
import my.edu.um.umpoint.modules.security.user.UserDetail;
import my.edu.um.umpoint.modules.sys.dto.SysMenuDTO;
import my.edu.um.umpoint.modules.sys.enums.MenuTypeEnum;
import my.edu.um.umpoint.modules.sys.service.SysMenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/sys/menu")
@Tag(name = "System Menu")
@AllArgsConstructor
public class SysMenuController {
    private final SysMenuService sysMenuService;
    private final ShiroService shiroService;

    @GetMapping("nav")
    @Operation(summary = "nav")
    public Result<List<SysMenuDTO>> nav() {
        UserDetail user = SecurityUser.getUser();
        List<SysMenuDTO> list = sysMenuService.getUserMenuList(user, MenuTypeEnum.MENU.value());

        return new Result<List<SysMenuDTO>>().ok(list);
    }

    @GetMapping("permissions")
    @Operation(summary = "permissions")
    public Result<Set<String>> permissions() {
        UserDetail user = SecurityUser.getUser();
        Set<String> set = shiroService.getUserPermissions(user);

        return new Result<Set<String>>().ok(set);
    }

    @GetMapping("list")
    @Operation(summary = "list")
    @Parameter(name = "type", description = "type 0:menu 1:button", in = ParameterIn.QUERY, schema = @Schema(type = "int"))
    @RequiresPermissions("sys:menu:list")
    public Result<List<SysMenuDTO>> list(Integer type) {
        List<SysMenuDTO> list = sysMenuService.getAllMenuList(type);

        return new Result<List<SysMenuDTO>>().ok(list);
    }

    @GetMapping("{id}")
    @Operation(summary = "info")
    @RequiresPermissions("sys:menu:info")
    public Result<SysMenuDTO> get(@PathVariable("id") Long id) {
        SysMenuDTO data = sysMenuService.get(id);

        return new Result<SysMenuDTO>().ok(data);
    }

    @PostMapping
    @Operation(summary = "save")
    @LogOperation("save")
    @RequiresPermissions("sys:menu:save")
    public Result save(@RequestBody SysMenuDTO dto) {
        ValidatorUtils.validateEntity(dto, DefaultGroup.class);

        sysMenuService.save(dto);

        return new Result();
    }

    @PutMapping
    @Operation(summary = "update")
    @LogOperation("update")
    @RequiresPermissions("sys:menu:update")
    public Result update(@RequestBody SysMenuDTO dto) {
        ValidatorUtils.validateEntity(dto, DefaultGroup.class);

        sysMenuService.update(dto);

        return new Result();
    }

    @DeleteMapping("{id}")
    @Operation(summary = "delete")
    @LogOperation("delete")
    @RequiresPermissions("sys:menu:delete")
    public Result delete(@PathVariable("id") Long id) {
        AssertUtils.isNull(id, "id");

        List<SysMenuDTO> list = sysMenuService.getListPid(id);
        if (list.size() > 0) {
            return new Result().error(ErrorCode.SUB_MENU_EXIST);
        }

        sysMenuService.delete(id);

        return new Result();
    }

    @GetMapping("select")
    @Operation(summary = "menu select")
    @RequiresPermissions("sys:menu:select")
    public Result<List<SysMenuDTO>> select() {
        UserDetail user = SecurityUser.getUser();
        List<SysMenuDTO> list = sysMenuService.getUserMenuList(user, null);

        return new Result<List<SysMenuDTO>>().ok(list);
    }
}
