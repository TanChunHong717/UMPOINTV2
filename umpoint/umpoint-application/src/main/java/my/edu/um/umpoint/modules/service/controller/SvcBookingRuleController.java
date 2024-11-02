package my.edu.um.umpoint.modules.service.controller;

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
import my.edu.um.umpoint.modules.service.dto.SvcBookingRuleDTO;
import my.edu.um.umpoint.modules.service.dto.SvcServiceDTO;
import my.edu.um.umpoint.modules.service.service.SvcBookingRuleService;
import my.edu.um.umpoint.modules.service.service.SvcServiceService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @Autowired
    private SvcServiceService svcServiceService;

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
    @RequiresPermissions("service:service:page")
    public Result<PageData<SvcServiceDTO>> page(@Parameter(hidden = true) @RequestParam Map<String, Object> params){
        PageData<SvcServiceDTO> page = svcServiceService.bookingRulePage(params);

        return new Result<PageData<SvcServiceDTO>>().ok(page);
    }

    @PostMapping
    @Operation(summary = "Batch Update Booking Rule")
    @LogOperation("Update")
    @RequiresPermissions("service:booking-rule:update")
    public Result updateDefaultBookingRule(@RequestParam List<Long> idList, @RequestBody SvcBookingRuleDTO svcBookingRuleDTO) {
        if (idList.isEmpty())
            throw new RenException("Empty booking id list");
        ValidatorUtils.validateEntity(svcBookingRuleDTO, BatchUpdateGroup.class);

        svcBookingRuleService.updateBatch(idList, svcBookingRuleDTO);

        return new Result();
    }
}
