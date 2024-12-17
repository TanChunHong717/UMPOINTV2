package my.edu.um.umpoint.modules.space.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import my.edu.um.umpoint.common.constant.Constant;
import my.edu.um.umpoint.common.utils.Result;
import my.edu.um.umpoint.modules.space.service.SpcEventService;
import my.edu.um.umpoint.modules.space.dto.SpcEventDTO;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Space Occupied Event
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-09-16
 */
@RestController
@RequestMapping("space/event")
@Tag(name="Space Occupied Event")
public class SpcEventController {
    @Autowired
    private SpcEventService spcEventService;

    @GetMapping
    @Operation(summary = "List")
    @Parameters({
        @Parameter(name = Constant.SPACE_ID, description = "Space ID", in = ParameterIn.QUERY, required = true),
        @Parameter(name = Constant.START_TIME, description = "Start Date Time", in = ParameterIn.QUERY),
        @Parameter(name = Constant.END_TIME, description = "End Date Time", in = ParameterIn.QUERY),
    })
    public Result<List<SpcEventDTO>> page(@Parameter(hidden = true) @RequestParam Map<String, Object> params){
        List<SpcEventDTO> list = spcEventService.list(params);

        return new Result<List<SpcEventDTO>>().ok(list);
    }
}
