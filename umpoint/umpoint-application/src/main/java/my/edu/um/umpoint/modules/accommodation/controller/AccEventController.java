package my.edu.um.umpoint.modules.accommodation.controller;

import cn.hutool.core.util.ArrayUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import my.edu.um.umpoint.common.constant.Constant;
import my.edu.um.umpoint.common.exception.BadHttpRequestException;
import my.edu.um.umpoint.common.utils.Result;
import my.edu.um.umpoint.modules.accommodation.dto.AccEventDTO;
import my.edu.um.umpoint.modules.accommodation.service.AccEventService;
import my.edu.um.umpoint.modules.space.dto.SpcEventDTO;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Accommodation Occupied Event
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-09-16
 */
@RestController
@RequestMapping("accommodation/event")
@Tag(name="Accommodation Occupied Event")
public class AccEventController {
    @Autowired
    private AccEventService accEventService;

    @GetMapping
    @Operation(summary = "List")
    @Parameters({
        @Parameter(name = Constant.ACCOMMODATION_ID, description = "Accommodation ID", in = ParameterIn.QUERY),
        @Parameter(name = Constant.START_TIME, description = "Start Date Time", in = ParameterIn.QUERY),
        @Parameter(name = Constant.END_TIME, description = "End Date Time", in = ParameterIn.QUERY),
    })
    public Result<List<AccEventDTO>> page(@Parameter(hidden = true) @RequestParam Map<String, Object> params, @RequestBody(required = false) Long[] ids){
        if (ArrayUtil.isNotEmpty(ids))
            params.put(Constant.ACCOMMODATION_ID_LIST, ids);
        return new Result<List<AccEventDTO>>().ok(accEventService.list(params));
    }
}
