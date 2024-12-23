package my.edu.um.umpoint.modules.space.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import my.edu.um.umpoint.common.annotation.LogOperation;
import my.edu.um.umpoint.common.constant.Constant;
import my.edu.um.umpoint.common.page.PageData;
import my.edu.um.umpoint.common.utils.Result;
import my.edu.um.umpoint.common.validator.AssertUtils;
import my.edu.um.umpoint.common.validator.ValidatorUtils;
import my.edu.um.umpoint.common.validator.group.AddGroup;
import my.edu.um.umpoint.common.validator.group.DefaultGroup;
import my.edu.um.umpoint.common.validator.group.UpdateGroup;
import my.edu.um.umpoint.modules.space.dto.SpcDeptBookingRuleDTO;
import my.edu.um.umpoint.modules.space.service.SpcDeptBookingRuleService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


/**
 * Space Department Level Booking Rule
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-11-02
 */
@RestController
@RequestMapping("space/dept-booking-rule")
@Tag(name="Space Department Level Booking Rule")
public class SpcDeptBookingRuleController {
    @Autowired
    private SpcDeptBookingRuleService spcDeptBookingRuleService;

    @GetMapping("page")
    @Operation(summary = "Pagination")
    @Parameters({
        @Parameter(name = Constant.PAGE, description = "Current page number, starting from 1", in = ParameterIn.QUERY, required = true, schema = @Schema(type = "integer")) ,
        @Parameter(name = Constant.LIMIT, description = "Number of records per page", in = ParameterIn.QUERY,required = true, schema = @Schema(type = "integer")) ,
        @Parameter(name = Constant.ORDER_FIELD, description = "Sort field", in = ParameterIn.QUERY, schema = @Schema(type = "string")) ,
        @Parameter(name = Constant.ORDER, description = "Sort order, optional values (asc, desc)", in = ParameterIn.QUERY, schema = @Schema(type = "string")) ,
        @Parameter(name = "deptName", description = "Department name", in = ParameterIn.QUERY, schema = @Schema(type = "string"))
    })
    @RequiresPermissions("space:dept-booking-rule:page")
    public Result<PageData<SpcDeptBookingRuleDTO>> page(@Parameter(hidden = true) @RequestParam Map<String, Object> params){
        PageData<SpcDeptBookingRuleDTO> page = spcDeptBookingRuleService.page(params);

        return new Result<PageData<SpcDeptBookingRuleDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @Operation(summary = "Information")
    @RequiresPermissions("space:dept-booking-rule:info")
    public Result<SpcDeptBookingRuleDTO> get(@PathVariable("id") Long id){
        SpcDeptBookingRuleDTO data = spcDeptBookingRuleService.get(id);

        return new Result<SpcDeptBookingRuleDTO>().ok(data);
    }

    @PostMapping
    @Operation(summary = "Save")
    @LogOperation("Save")
    @RequiresPermissions("space:dept-booking-rule:save")
    public Result save(@RequestBody SpcDeptBookingRuleDTO dto){
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        spcDeptBookingRuleService.save(dto);

        return new Result();
    }

    @PutMapping
    @Operation(summary = "Update")
    @LogOperation("Update")
    @RequiresPermissions("space:dept-booking-rule:update")
    public Result update(@RequestBody SpcDeptBookingRuleDTO dto){
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        spcDeptBookingRuleService.update(dto);

        return new Result();
    }

    @DeleteMapping
    @Operation(summary = "Delete")
    @LogOperation("Delete")
    @RequiresPermissions("space:dept-booking-rule:delete")
    public Result delete(@RequestBody Long[] ids){
        AssertUtils.isArrayEmpty(ids, "id");

        spcDeptBookingRuleService.delete(ids);

        return new Result();
    }
}
