package my.edu.um.umpoint.modules.payment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import my.edu.um.umpoint.common.annotation.LogOperation;
import my.edu.um.umpoint.common.constant.Constant;
import my.edu.um.umpoint.common.page.PageData;
import my.edu.um.umpoint.common.utils.ExcelUtils;
import my.edu.um.umpoint.common.utils.Result;
import my.edu.um.umpoint.common.validator.AssertUtils;
import my.edu.um.umpoint.common.validator.ValidatorUtils;
import my.edu.um.umpoint.common.validator.group.AddGroup;
import my.edu.um.umpoint.common.validator.group.DefaultGroup;
import my.edu.um.umpoint.common.validator.group.UpdateGroup;
import my.edu.um.umpoint.modules.payment.dto.SpcPaymentDTO;
import my.edu.um.umpoint.modules.payment.excel.PaymentExcel;
import my.edu.um.umpoint.modules.payment.service.SpcPaymentService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Space Payment
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-09-08
 */
@RestController
@RequestMapping("payment/space")
@Tag(name="Space Payment")
public class SpcPaymentController {
    @Autowired
    private SpcPaymentService spcPaymentService;

    @GetMapping("page")
    @Operation(summary = "Pagination")
    @Parameters({
        @Parameter(name = Constant.PAGE, description = "Current page number, starting from 1", in = ParameterIn.QUERY, required = true, schema = @Schema(type = "integer")) ,
        @Parameter(name = Constant.LIMIT, description = "Number of records per page", in = ParameterIn.QUERY,required = true, schema = @Schema(type = "integer")) ,
        @Parameter(name = Constant.ORDER_FIELD, description = "Sort field", in = ParameterIn.QUERY, schema = @Schema(type = "string")) ,
        @Parameter(name = Constant.ORDER, description = "Sort order, optional values (asc, desc)", in = ParameterIn.QUERY, schema = @Schema(type = "string"))
    })
    @RequiresPermissions("payment:space:page")
    public Result<PageData<SpcPaymentDTO>> page(@Parameter(hidden = true) @RequestParam Map<String, Object> params){
        PageData<SpcPaymentDTO> page = spcPaymentService.page(params);

        return new Result<PageData<SpcPaymentDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @Operation(summary = "Information")
    @RequiresPermissions("payment:space:info")
    public Result<SpcPaymentDTO> get(@PathVariable("id") Long id){
        SpcPaymentDTO data = spcPaymentService.get(id);

        return new Result<SpcPaymentDTO>().ok(data);
    }

    @PostMapping
    @Operation(summary = "Save")
    @LogOperation("Save")
    @RequiresPermissions("payment:space:save")
    public Result save(@RequestBody SpcPaymentDTO dto){
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        spcPaymentService.save(dto);

        return new Result();
    }

    @PutMapping
    @Operation(summary = "Update")
    @LogOperation("Update")
    @RequiresPermissions("payment:space:update")
    public Result update(@RequestBody SpcPaymentDTO dto){
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        spcPaymentService.update(dto);

        return new Result();
    }

    @PutMapping("refund/{id}")
    @Operation(summary = "Update")
    @LogOperation("Update")
    @RequiresPermissions("payment:space:refund")
    public Result refund(@PathVariable("id") Long id){
        spcPaymentService.refund(id);

        return new Result();
    }

    @DeleteMapping
    @Operation(summary = "Delete")
    @LogOperation("Delete")
    @RequiresPermissions("payment:space:delete")
    public Result delete(@RequestBody Long[] ids){
        AssertUtils.isArrayEmpty(ids, "id");

        spcPaymentService.delete(ids);

        return new Result();
    }

    @GetMapping("export")
    @Operation(summary = "Export")
    @LogOperation("Export")
    @RequiresPermissions("payment:space:export")
    public void export(@Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        List<SpcPaymentDTO> list = spcPaymentService.list(params);

        ExcelUtils.exportExcelToTarget(response, null, "Payment", list, PaymentExcel.class);
    }

}
