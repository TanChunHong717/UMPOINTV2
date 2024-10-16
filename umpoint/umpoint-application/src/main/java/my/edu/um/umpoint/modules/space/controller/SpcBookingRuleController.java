package my.edu.um.umpoint.modules.space.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import my.edu.um.umpoint.common.annotation.LogOperation;
import my.edu.um.umpoint.common.constant.Constant;
import my.edu.um.umpoint.common.page.PageData;
import my.edu.um.umpoint.common.utils.Result;
import my.edu.um.umpoint.common.validator.ValidatorUtils;
import my.edu.um.umpoint.common.validator.group.UpdateGroup;
import my.edu.um.umpoint.modules.space.dto.SpcBookingRuleDTO;
import my.edu.um.umpoint.modules.space.dto.SpcSpaceDTO;
import my.edu.um.umpoint.modules.space.service.SpcBookingRuleService;
import my.edu.um.umpoint.modules.space.service.SpcSpaceService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Space Booking Rule
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-09-06
 */
@RestController
@RequestMapping("space/booking-rule")
@Tag(name="Space Booking Rule")
public class SpcBookingRuleController {

    @Autowired
    private SpcBookingRuleService spcBookingRuleService;

    @Autowired
    private SpcSpaceService spcSpaceService;

    @GetMapping("page")
    @Operation(summary = "Pagination")
    @Parameters({
        @Parameter(name = Constant.PAGE, description = "Current page number, starting from 1", in = ParameterIn.QUERY, required = true, ref="int") ,
        @Parameter(name = Constant.LIMIT, description = "Number of records per page", in = ParameterIn.QUERY,required = true, ref="int") ,
        @Parameter(name = Constant.ORDER_FIELD, description = "Sort field", in = ParameterIn.QUERY, ref="String") ,
        @Parameter(name = Constant.ORDER, description = "Sort order, optional values (asc, desc)", in = ParameterIn.QUERY, ref="String") ,
        @Parameter(name = Constant.NAME, description = "Name", in = ParameterIn.QUERY, ref="String") ,
        @Parameter(name = Constant.APPROVAL_REQUIRED, description = "Is admin approval required", in = ParameterIn.QUERY, ref="int"),
    })
    public Result<PageData<SpcSpaceDTO>> page(@Parameter(hidden = true) @RequestParam Map<String, Object> params){
        PageData<SpcSpaceDTO> page = spcSpaceService.bookingRulePage(params);

        return new Result<PageData<SpcSpaceDTO>>().ok(page);
    }

    @GetMapping("default")
    @Operation(summary = "Get Default Booking Rule")
    @RequiresPermissions("space:default-booking-rule:info")
    public Result<SpcBookingRuleDTO> getDefaultBookingRule() {
        return new Result<SpcBookingRuleDTO>().ok(spcBookingRuleService.get(0L));
    }

    @PutMapping("default")
    @Operation(summary = "Update Default Booking Rule")
    @LogOperation("Update")
    @RequiresPermissions("space:default-booking-rule:update")
    public Result updateDefaultBookingRule(@RequestBody SpcBookingRuleDTO spcBookingRuleDTO) {
        ValidatorUtils.validateEntity(spcBookingRuleDTO, UpdateGroup.class);

        spcBookingRuleService.update(spcBookingRuleDTO);

        return new Result();
    }
}
