package my.edu.um.umpoint.modules.client.controller;

import my.edu.um.umpoint.common.annotation.LogOperation;
import my.edu.um.umpoint.common.constant.Constant;
import my.edu.um.umpoint.common.page.PageData;
import my.edu.um.umpoint.common.utils.ConvertUtils;
import my.edu.um.umpoint.common.utils.ExcelUtils;
import my.edu.um.umpoint.common.utils.Result;
import my.edu.um.umpoint.common.validator.AssertUtils;
import my.edu.um.umpoint.common.validator.ValidatorUtils;
import my.edu.um.umpoint.common.validator.group.AddGroup;
import my.edu.um.umpoint.common.validator.group.DefaultGroup;
import my.edu.um.umpoint.common.validator.group.UpdateGroup;
import my.edu.um.umpoint.modules.client.dto.CliUserDTO;
import my.edu.um.umpoint.modules.client.excel.CliUserExcel;
import my.edu.um.umpoint.modules.client.service.CliUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import my.edu.um.umpoint.modules.security.user.SecurityUser;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


/**
 * User
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-10-01
 */
@RestController
@RequestMapping("client/user")
@Tag(name="Client management")
public class CliUserController {
    private static final Logger log = LoggerFactory.getLogger(CliUserController.class);
    @Autowired
    private CliUserService cliUserService;

    @GetMapping("page")
    @Operation(summary = "Pagination")
    @Parameters({
        @Parameter(name = Constant.PAGE, description = "Current page number, starting from 1", in = ParameterIn.QUERY, required = true, ref="int") ,
        @Parameter(name = Constant.LIMIT, description = "Number of records per page", in = ParameterIn.QUERY,required = true, ref="int") ,
        @Parameter(name = Constant.ORDER_FIELD, description = "Sort field", in = ParameterIn.QUERY, ref="String") ,
        @Parameter(name = Constant.ORDER, description = "Sort order, optional values (asc, desc)", in = ParameterIn.QUERY, ref="String"),
        @Parameter(name = "Username", description = "Client username", in = ParameterIn.QUERY, ref="String")
    })
    @RequiresPermissions("client:user:page")
    public Result<PageData<CliUserDTO>> page(@Parameter(hidden = true) @RequestParam Map<String, Object> params){
        PageData<CliUserDTO> page = cliUserService.page(params);

        return new Result<PageData<CliUserDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @Operation(summary = "Information")
    @RequiresPermissions("client:user:info")
    public Result<CliUserDTO> get(@PathVariable("id") Long id){
        CliUserDTO data = cliUserService.get(id);

        return new Result<CliUserDTO>().ok(data);
    }

    @GetMapping("info")
    @Operation(summary = "login user info")
    public Result<CliUserDTO> info() {
        CliUserDTO data = ConvertUtils.sourceToTarget(SecurityUser.getUser(), CliUserDTO.class);
        return new Result<CliUserDTO>().ok(data);
    }

    @PostMapping
    @Operation(summary = "Save")
    @LogOperation("Save")
    public Result save(@RequestBody CliUserDTO dto){
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);
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

    @PutMapping
    @Operation(summary = "Update")
    @LogOperation("Update")
    public Result update(@RequestBody CliUserDTO dto){
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        cliUserService.update(dto);

        return new Result();
    }

    @DeleteMapping
    @Operation(summary = "Delete")
    @LogOperation("Delete")
    public Result delete(@RequestBody Long[] ids){
        AssertUtils.isArrayEmpty(ids, "id");

        cliUserService.delete(ids);

        return new Result();
    }

    @GetMapping("export")
    @Operation(summary = "Export")
    @LogOperation("Export")
    @RequiresPermissions("client:user:export")
    public void export(@Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        List<CliUserDTO> list = cliUserService.list(params);

        ExcelUtils.exportExcelToTarget(response, null, "User", list, CliUserExcel.class);
    }

}
