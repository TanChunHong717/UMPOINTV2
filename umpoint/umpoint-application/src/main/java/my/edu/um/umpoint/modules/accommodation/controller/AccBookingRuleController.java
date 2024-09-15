package my.edu.um.umpoint.modules.accommodation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import my.edu.um.umpoint.common.annotation.LogOperation;
import my.edu.um.umpoint.common.utils.Result;
import my.edu.um.umpoint.common.validator.AssertUtils;
import my.edu.um.umpoint.common.validator.ValidatorUtils;
import my.edu.um.umpoint.common.validator.group.UpdateGroup;
import my.edu.um.umpoint.modules.accommodation.dto.AccBookingRuleDTO;
import my.edu.um.umpoint.modules.accommodation.service.AccAccommodationService;
import my.edu.um.umpoint.modules.accommodation.service.AccBookingRuleService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Accommodation Booking Rule
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-09-06
 */
@RestController
@RequestMapping("accommodation/booking-rule")
@Tag(name="Accommodation Booking Rule")
public class AccBookingRuleController {

    @Autowired
    private AccBookingRuleService accBookingRuleService;

    @Autowired
    private AccAccommodationService accAccommodationService;

    @GetMapping("default")
    public Result<AccBookingRuleDTO> getDefaultBookingRule() {
        return new Result<AccBookingRuleDTO>().ok(accBookingRuleService.get(0L));
    }

    @PostMapping("default")
    @Operation(summary = "Update Default Booking Rule")
    @LogOperation("Update")
    @RequiresPermissions("accommodation:booking-rule:update")
    public Result updateDefaultBookingRule(@RequestBody AccBookingRuleDTO accBookingRuleDTO) {
        ValidatorUtils.validateEntity(accBookingRuleDTO, UpdateGroup.class);

        accBookingRuleService.update(accBookingRuleDTO);

        return new Result();
    }
}
