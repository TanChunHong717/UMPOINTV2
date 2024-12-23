package my.edu.um.umpoint.modules.space.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import my.edu.um.umpoint.common.annotation.LogOperation;
import my.edu.um.umpoint.common.constant.Constant;
import my.edu.um.umpoint.common.exception.RenException;
import my.edu.um.umpoint.common.page.PageData;
import my.edu.um.umpoint.common.utils.Result;
import my.edu.um.umpoint.common.validator.ValidatorUtils;
import my.edu.um.umpoint.common.validator.group.BatchUpdateGroup;
import my.edu.um.umpoint.modules.space.dto.SpcBookingRuleDTO;
import my.edu.um.umpoint.modules.space.dto.SpcSpaceDTO;
import my.edu.um.umpoint.modules.space.service.SpcBookingRuleService;
import my.edu.um.umpoint.modules.space.service.SpcSpaceService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
        @Parameter(name = Constant.PAGE, description = "Current page number, starting from 1", in = ParameterIn.QUERY, required = true, schema = @Schema(type = "integer")) ,
        @Parameter(name = Constant.LIMIT, description = "Number of records per page", in = ParameterIn.QUERY,required = true, schema = @Schema(type = "integer")) ,
        @Parameter(name = Constant.ORDER_FIELD, description = "Sort field", in = ParameterIn.QUERY, schema = @Schema(type = "string")) ,
        @Parameter(name = Constant.ORDER, description = "Sort order, optional values (asc, desc)", in = ParameterIn.QUERY, schema = @Schema(type = "string")) ,
        @Parameter(name = Constant.NAME, description = "Name", in = ParameterIn.QUERY, schema = @Schema(type = "string")) ,
        @Parameter(name = Constant.APPROVAL_REQUIRED, description = "Is admin approval required", in = ParameterIn.QUERY, schema = @Schema(type = "integer")),
    })
    public Result<PageData<SpcSpaceDTO>> page(@Parameter(hidden = true) @RequestParam Map<String, Object> params){
        PageData<SpcSpaceDTO> page = spcSpaceService.bookingRulePage(params);

        return new Result<PageData<SpcSpaceDTO>>().ok(page);
    }

    @PutMapping
    @Operation(summary = "Batch Update Booking Rule")
    @LogOperation("Update")
    @RequiresPermissions("space:booking-rule:update")
    public Result updateBatchBookingRule(@RequestParam List<Long> idList, @RequestBody SpcBookingRuleDTO spcBookingRuleDTO) {
        if (idList.isEmpty())
            throw new RenException("Empty booking id list");
        ValidatorUtils.validateEntity(spcBookingRuleDTO, BatchUpdateGroup.class);
        if (spcBookingRuleDTO.getBookingMode() == 1) {
            long diffInMin = (spcBookingRuleDTO.getStartTime().getTime() - spcBookingRuleDTO.getEndTime().getTime()) / 60000;
            if (diffInMin % spcBookingRuleDTO.getBookingUnit().intValue() != 0)
                throw new RenException("The difference between start time and end time must be a multiple of the booking unit.");
        }

        spcBookingRuleService.updateBatch(idList, spcBookingRuleDTO);

        return new Result();
    }
}
