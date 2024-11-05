package my.edu.um.umpoint.modules.accommodation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import my.edu.um.umpoint.common.annotation.LogOperation;
import my.edu.um.umpoint.common.constant.Constant;
import my.edu.um.umpoint.common.exception.RenException;
import my.edu.um.umpoint.common.page.PageData;
import my.edu.um.umpoint.common.utils.Result;
import my.edu.um.umpoint.common.validator.ValidatorUtils;
import my.edu.um.umpoint.common.validator.group.BatchUpdateGroup;
import my.edu.um.umpoint.common.validator.group.UpdateGroup;
import my.edu.um.umpoint.modules.accommodation.dto.AccAccommodationDTO;
import my.edu.um.umpoint.modules.accommodation.dto.AccBookingRuleDTO;
import my.edu.um.umpoint.modules.accommodation.service.AccAccommodationService;
import my.edu.um.umpoint.modules.accommodation.service.AccBookingRuleService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    @RequiresPermissions("accommodation:accommodation:page")
    public Result<PageData<AccAccommodationDTO>> page(@Parameter(hidden = true) @RequestParam Map<String, Object> params){
        PageData<AccAccommodationDTO> page = accAccommodationService.bookingRulePage(params);

        return new Result<PageData<AccAccommodationDTO>>().ok(page);
    }

    @PutMapping
    @Operation(summary = "Batch Update Booking Rule")
    @LogOperation("Update")
    @RequiresPermissions("accommodation:booking-rule:update")
    public Result updateDefaultBookingRule(@RequestParam List<Long> idList, @RequestBody AccBookingRuleDTO accBookingRuleDTO) {
        if (idList.isEmpty())
            throw new RenException("Empty booking id list");
        ValidatorUtils.validateEntity(accBookingRuleDTO, BatchUpdateGroup.class);

        accBookingRuleService.updateBatch(idList, accBookingRuleDTO);

        return new Result();
    }
}
