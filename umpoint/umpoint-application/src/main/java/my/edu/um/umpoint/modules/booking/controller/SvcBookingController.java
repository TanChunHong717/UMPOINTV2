package my.edu.um.umpoint.modules.booking.controller;

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
import my.edu.um.umpoint.modules.booking.dto.SvcBookingDTO;
import my.edu.um.umpoint.modules.booking.excel.SvcBookingExcel;
import my.edu.um.umpoint.modules.booking.service.SvcBookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


/**
 * Space Booking
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-09-08
 */
@RestController
@RequestMapping("booking/service")
@Tag(name="Space Booking")
public class SvcBookingController {
    @Autowired
    private SvcBookingService svcBookingService;

    @GetMapping("page")
    @Operation(summary = "Pagination")
    @Parameters({
        @Parameter(name = Constant.PAGE, description = "Current page number, starting from 1", in = ParameterIn.QUERY, required = true, ref="int") ,
        @Parameter(name = Constant.LIMIT, description = "Number of records per page", in = ParameterIn.QUERY,required = true, ref="int") ,
        @Parameter(name = Constant.ORDER_FIELD, description = "Sort field", in = ParameterIn.QUERY, ref="String") ,
        @Parameter(name = Constant.ORDER, description = "Sort order, optional values (asc, desc)", in = ParameterIn.QUERY, ref="String")
    })
    @RequiresPermissions("booking:service:page")
    public Result<PageData<SvcBookingDTO>> page(@Parameter(hidden = true) @RequestParam Map<String, Object> params){
        PageData<SvcBookingDTO> page = svcBookingService.page(params);

        return new Result<PageData<SvcBookingDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @Operation(summary = "Information")
    @RequiresPermissions("booking:service:info")
    public Result<SvcBookingDTO> get(@PathVariable("id") Long id){
        SvcBookingDTO data = svcBookingService.get(id);

        return new Result<SvcBookingDTO>().ok(data);
    }

    @PostMapping
    @Operation(summary = "Save")
    @LogOperation("Save")
    @RequiresPermissions("booking:service:save")
    public Result save(@RequestBody SvcBookingDTO dto){
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        svcBookingService.save(dto);

        return new Result();
    }

    @PutMapping
    @Operation(summary = "Update")
    @LogOperation("Update")
    @RequiresPermissions("booking:service:update")
    public Result update(@RequestBody SvcBookingDTO dto){
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        svcBookingService.update(dto);

        return new Result();
    }

    @PutMapping("approve/{id}")
    @Operation(summary = "Approve")
    @LogOperation("Approve")
    @RequiresPermissions("booking:service:update")
    public Result approve(@PathVariable("id") Long id){
        svcBookingService.approve(id);

        return new Result();
    }

    @PutMapping("reject/{id}")
    @Operation(summary = "Reject")
    @LogOperation("Reject")
    @RequiresPermissions("booking:service:update")
    public Result reject(@PathVariable("id") Long id){
        svcBookingService.reject(id);

        return new Result();
    }

    @DeleteMapping
    @Operation(summary = "Delete")
    @LogOperation("Delete")
    @RequiresPermissions("booking:service:delete")
    public Result delete(@RequestBody Long[] ids){
        AssertUtils.isArrayEmpty(ids, "id");

        svcBookingService.delete(ids);

        return new Result();
    }

    @GetMapping("export")
    @Operation(summary = "Export")
    @LogOperation("Export")
    @RequiresPermissions("booking:service:export")
    public void export(@Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        List<SvcBookingDTO> list = svcBookingService.list(params);

        ExcelUtils.exportExcelToTarget(response, null, "Space Booking", list, SvcBookingExcel.class);
    }

}
