package my.edu.um.umpoint.modules.service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import my.edu.um.umpoint.common.annotation.LogOperation;
import my.edu.um.umpoint.common.utils.Result;
import my.edu.um.umpoint.common.validator.ValidatorUtils;
import my.edu.um.umpoint.common.validator.group.UpdateGroup;
import my.edu.um.umpoint.modules.service.dto.SvcBookingRuleDTO;
import my.edu.um.umpoint.modules.service.service.SvcBookingRuleService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Svcommodation Booking Rule
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-09-06
 */
@RestController
@RequestMapping("service/booking-rule")
@Tag(name="Service Booking Rule")
public class SvcBookingRuleController {

    @Autowired
    private SvcBookingRuleService svcBookingRuleService;

    @GetMapping("default")
    @Operation(summary = "Get Default Booking Rule")
    @RequiresPermissions("service:default-booking-rule:info")
    public Result<SvcBookingRuleDTO> getDefaultBookingRule() {
        return new Result<SvcBookingRuleDTO>().ok(svcBookingRuleService.get(0L));
    }

    @PostMapping("default")
    @Operation(summary = "Update Default Booking Rule")
    @LogOperation("Update")
    @RequiresPermissions("service:default-booking-rule:update")
    public Result updateDefaultBookingRule(@RequestBody SvcBookingRuleDTO svcBookingRuleDTO) {
        ValidatorUtils.validateEntity(svcBookingRuleDTO, UpdateGroup.class);

        svcBookingRuleService.update(svcBookingRuleDTO);

        return new Result();
    }
}
