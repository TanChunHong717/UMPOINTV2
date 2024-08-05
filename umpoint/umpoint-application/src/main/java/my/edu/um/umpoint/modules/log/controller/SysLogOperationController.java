package my.edu.um.umpoint.modules.log.controller;

import my.edu.um.umpoint.common.annotation.LogOperation;
import my.edu.um.umpoint.common.constant.Constant;
import my.edu.um.umpoint.common.page.PageData;
import my.edu.um.umpoint.common.utils.ExcelUtils;
import my.edu.um.umpoint.common.utils.Result;
import my.edu.um.umpoint.modules.log.dto.SysLogOperationDTO;
import my.edu.um.umpoint.modules.log.excel.SysLogOperationExcel;
import my.edu.um.umpoint.modules.log.service.SysLogOperationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("sys/log/operation")
@Tag(name = "operation log")
@AllArgsConstructor
public class SysLogOperationController {
    private final SysLogOperationService sysLogOperationService;

    @GetMapping("page")
    @Operation(summary = "page")
    @Parameters({
            @Parameter(name = Constant.PAGE, description = "Current page number, starting from 1", in = ParameterIn.QUERY, required = true, ref="int") ,
            @Parameter(name = Constant.LIMIT, description = "Number of records per page", in = ParameterIn.QUERY,required = true, ref="int") ,
            @Parameter(name = Constant.ORDER_FIELD, description = "Sort field", in = ParameterIn.QUERY, ref="String") ,
            @Parameter(name = Constant.ORDER, description = "Sort order, optional values (asc, desc)", in = ParameterIn.QUERY, ref="String"),
            @Parameter(name = "status", description = "Status 0:Failed 1:Success", in = ParameterIn.QUERY, ref = "int")
    })
    @RequiresPermissions("sys:log:operation")
    public Result<PageData<SysLogOperationDTO>> page(@Parameter(hidden = true) @RequestParam Map<String, Object> params) {
        PageData<SysLogOperationDTO> page = sysLogOperationService.page(params);

        return new Result<PageData<SysLogOperationDTO>>().ok(page);
    }

    @GetMapping("export")
    @Operation(summary = "export")
    @LogOperation("export")
    @RequiresPermissions("sys:log:operation")
    public void export(@Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        List<SysLogOperationDTO> list = sysLogOperationService.list(params);

        ExcelUtils.exportExcelToTarget(response, null, "Operation log", list, SysLogOperationExcel.class);
    }

}
