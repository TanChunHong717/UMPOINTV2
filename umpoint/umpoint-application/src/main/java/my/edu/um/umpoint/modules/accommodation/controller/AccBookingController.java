package my.edu.um.umpoint.modules.accommodation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
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
import my.edu.um.umpoint.modules.accommodation.dto.AccBookingDTO;
import my.edu.um.umpoint.modules.accommodation.excel.AccBookingExcel;
import my.edu.um.umpoint.modules.accommodation.service.AccBookingService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Accommodation Booking
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-09-08
 */
@RestController
@RequestMapping("accommodation/booking")
@Tag(name="Accommodation Booking")
public class AccBookingController {
    @Autowired
    private AccBookingService accBookingService;

    @GetMapping("page")
    @Operation(summary = "Pagination")
    @Parameters({
        @Parameter(name = Constant.PAGE, description = "Current page number, starting from 1", in = ParameterIn.QUERY, required = true, ref="int") ,
        @Parameter(name = Constant.LIMIT, description = "Number of records per page", in = ParameterIn.QUERY,required = true, ref="int") ,
        @Parameter(name = Constant.ORDER_FIELD, description = "Sort field", in = ParameterIn.QUERY, ref="String") ,
        @Parameter(name = Constant.ORDER, description = "Sort order, optional values (asc, desc)", in = ParameterIn.QUERY, ref="String"),
        @Parameter(name = Constant.ID, description = "Booking ID", in = ParameterIn.QUERY) ,
        @Parameter(name = Constant.STATUS, description = "Booking status", in = ParameterIn.QUERY, ref="int") ,
        @Parameter(name = Constant.EVENT, description = "Booking purpose description", in = ParameterIn.QUERY, ref="String")
    })
    @RequiresPermissions("accommodation:booking:page")
    public Result<PageData<AccBookingDTO>> page(@Parameter(hidden = true) @RequestParam Map<String, Object> params){
        PageData<AccBookingDTO> page = accBookingService.page(params);

        return new Result<PageData<AccBookingDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @Operation(summary = "Information")
    @RequiresPermissions("accommodation:booking:info")
    public Result<AccBookingDTO> get(@PathVariable("id") Long id){
        AccBookingDTO data = accBookingService.get(id);

        return new Result<AccBookingDTO>().ok(data);
    }

    @PostMapping
    @Operation(summary = "Save")
    @LogOperation("Save")
    @RequiresPermissions("accommodation:booking:save")
    public Result save(@RequestBody AccBookingDTO dto){
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        accBookingService.save(dto);

        return new Result();
    }

    @PutMapping
    @Operation(summary = "Update")
    @LogOperation("Update")
    @RequiresPermissions("accommodation:booking:update")
    public Result update(@RequestBody AccBookingDTO dto){
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        accBookingService.update(dto);

        return new Result();
    }

    @PutMapping("approve/{id}")
    @Operation(summary = "Approve")
    @LogOperation("Approve")
    @RequiresPermissions("accommodation:booking:approve")
    public Result approve(@PathVariable("id") Long id){
        accBookingService.approve(id);

        return new Result();
    }

    @PutMapping("reject/{id}")
    @Operation(summary = "Reject")
    @LogOperation("Reject")
    @RequiresPermissions("accommodation:booking:reject")
    public Result reject(@PathVariable("id") Long id){
        accBookingService.reject(id);

        return new Result();
    }

    @PutMapping("cancel/{id}")
    @Operation(summary = "Cancel")
    @LogOperation("Cancel")
    @RequiresPermissions("accommodation:booking:cancel")
    public Result cancel(@PathVariable("id") Long id){
        accBookingService.cancel(id);

        return new Result();
    }

    @DeleteMapping
    @Operation(summary = "Delete")
    @LogOperation("Delete")
    @RequiresPermissions("accommodation:booking:delete")
    public Result delete(@RequestBody Long[] ids){
        AssertUtils.isArrayEmpty(ids, "id");

        accBookingService.delete(ids);

        return new Result();
    }

    @GetMapping("export")
    @Operation(summary = "Export")
    @LogOperation("Export")
    @RequiresPermissions("accommodation:booking:export")
    public void export(@Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        List<AccBookingDTO> list = accBookingService.list(params);

        ExcelUtils.exportExcelToTarget(response, null, "Accommodation Booking", list, AccBookingExcel.class);
    }

}
