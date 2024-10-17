package my.edu.um.umpoint.modules.service.controller;

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
import my.edu.um.umpoint.modules.security.user.SecurityUser;
import my.edu.um.umpoint.modules.security.user.UserDetail;
import my.edu.um.umpoint.modules.service.dto.SvcBookingDTO;
import my.edu.um.umpoint.modules.service.excel.SvcBookingExcel;
import my.edu.um.umpoint.modules.service.service.SvcBookingService;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Space Booking
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-09-08
 */
@RestController
@RequestMapping("service/booking")
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
        @Parameter(name = Constant.ORDER, description = "Sort order, optional values (asc, desc)", in = ParameterIn.QUERY, ref="String") ,
        @Parameter(name = Constant.ID, description = "Booking ID", in = ParameterIn.QUERY) ,
        @Parameter(name = Constant.STATUS, description = "Booking status", in = ParameterIn.QUERY, ref="int") ,
        @Parameter(name = Constant.SERVICE, description = "Service name", in = ParameterIn.QUERY, ref="String")
    })
    @RequiresPermissions("service:booking:page")
    public Result<PageData<SvcBookingDTO>> page(@Parameter(hidden = true) @RequestParam Map<String, Object> params){
        PageData<SvcBookingDTO> page = svcBookingService.page(params);

        return new Result<PageData<SvcBookingDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @Operation(summary = "Information")
    @RequiresPermissions("service:booking:info")
    public Result<SvcBookingDTO> get(@PathVariable("id") Long id){
        SvcBookingDTO data = svcBookingService.get(id);

        return new Result<SvcBookingDTO>().ok(data);
    }

    @PostMapping
    @Operation(summary = "Save")
    @LogOperation("Save")
    @RequiresPermissions("service:booking:save")
    public Result save(@RequestBody SvcBookingDTO dto){
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        svcBookingService.save(dto);

        return new Result();
    }

    @PutMapping
    @Operation(summary = "Update")
    @LogOperation("Update")
    @RequiresPermissions("service:booking:update")
    public Result update(@RequestBody SvcBookingDTO dto){
        UserDetail user = SecurityUser.getUser();
        if (!Objects.equals(user.getId(), svcBookingService.getUserId(dto.getId())))
            throw new UnauthorizedException();
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        svcBookingService.update(dto);

        return new Result();
    }

    @PutMapping("approve/{id}")
    @Operation(summary = "Approve")
    @LogOperation("Approve")
    @RequiresPermissions("service:booking:update")
    public Result approve(@PathVariable("id") Long id){
        svcBookingService.approve(id);

        return new Result();
    }

    @PutMapping("reject/{id}")
    @Operation(summary = "Reject")
    @LogOperation("Reject")
    @RequiresPermissions("service:booking:update")
    public Result reject(@PathVariable("id") Long id){
        svcBookingService.reject(id);

        return new Result();
    }

    @PutMapping("cancel/{id}")
    @Operation(summary = "Cancel")
    @LogOperation("Cancel")
    @RequiresPermissions("space:booking:cancel")
    public Result cancel(@PathVariable("id") Long id){
        UserDetail user = SecurityUser.getUser();
        if (!Objects.equals(user.getId(), svcBookingService.getUserId(id)))
            throw new UnauthorizedException();

        svcBookingService.cancel(id);

        return new Result();
    }

    @DeleteMapping
    @Operation(summary = "Delete")
    @LogOperation("Delete")
    @RequiresPermissions("service:booking:delete")
    public Result delete(@RequestBody Long[] ids){
        AssertUtils.isArrayEmpty(ids, "id");

        svcBookingService.delete(ids);

        return new Result();
    }

    @GetMapping("export")
    @Operation(summary = "Export")
    @LogOperation("Export")
    @RequiresPermissions("service:booking:export")
    public void export(@Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        List<SvcBookingDTO> list = svcBookingService.list(params);

        ExcelUtils.exportExcelToTarget(response, null, "Space Booking", list, SvcBookingExcel.class);
    }

}
