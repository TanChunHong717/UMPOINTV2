package my.edu.um.umpoint.modules.space.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import my.edu.um.umpoint.common.annotation.LogOperation;
import my.edu.um.umpoint.common.utils.Result;
import my.edu.um.umpoint.common.validator.AssertUtils;
import my.edu.um.umpoint.common.validator.ValidatorUtils;
import my.edu.um.umpoint.common.validator.group.UpdateGroup;
import my.edu.um.umpoint.modules.space.dto.SpcBookingRuleDTO;
import my.edu.um.umpoint.modules.space.service.SpcBookingRuleService;
import my.edu.um.umpoint.modules.space.service.SpcSpaceService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("default")
    public Result<SpcBookingRuleDTO> getDefaultBookingRule() {
        return new Result<SpcBookingRuleDTO>().ok(spcBookingRuleService.get(0L));
    }

    @PutMapping("default")
    @Operation(summary = "Update Default Booking Rule")
    @LogOperation("Update")
    @RequiresPermissions("space:booking-rule:update")
    public Result updateDefaultBookingRule(@RequestBody SpcBookingRuleDTO spcBookingRuleDTO) {
        ValidatorUtils.validateEntity(spcBookingRuleDTO, UpdateGroup.class);

        spcBookingRuleService.update(spcBookingRuleDTO);

        return new Result();
    }
}
