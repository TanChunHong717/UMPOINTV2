package my.edu.um.umpoint.modules.client.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import my.edu.um.umpoint.common.annotation.LogOperation;
import my.edu.um.umpoint.common.constant.Constant;
import my.edu.um.umpoint.common.exception.BadHttpRequestException;
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
import my.edu.um.umpoint.modules.client.dto.CliUserDTO;
import my.edu.um.umpoint.modules.client.excel.CliUserExcel;
import my.edu.um.umpoint.modules.client.service.CliUserService;
import my.edu.um.umpoint.modules.security.password.PasswordUtils;
import my.edu.um.umpoint.modules.security.user.SecurityUser;
import my.edu.um.umpoint.modules.security.user.UserDetail;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @Autowired
    private CliUserService cliUserService;

    @GetMapping("page")
    @Operation(summary = "Pagination")
    @Parameters({
        @Parameter(name = Constant.PAGE, description = "Current page number, starting from 1", in = ParameterIn.QUERY, required = true, schema = @Schema(type = "integer")) ,
        @Parameter(name = Constant.LIMIT, description = "Number of records per page", in = ParameterIn.QUERY,required = true, schema = @Schema(type = "integer")) ,
        @Parameter(name = Constant.ORDER_FIELD, description = "Sort field", in = ParameterIn.QUERY, schema = @Schema(type = "string")) ,
        @Parameter(name = Constant.ORDER, description = "Sort order, optional values (asc, desc)", in = ParameterIn.QUERY, schema = @Schema(type = "string")),
        @Parameter(name = "Username", description = "Client username", in = ParameterIn.QUERY, schema = @Schema(type = "string")) ,
        @Parameter(name = "Type", description = "User type", in = ParameterIn.QUERY, schema = @Schema(type = "string"))
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
    public void export(
        @Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletResponse response
    ) throws Exception{
        List<CliUserDTO> list = cliUserService.list(params);

        ExcelUtils.exportExcelToTarget(response, null, "User", list, CliUserExcel.class);
    }
}
