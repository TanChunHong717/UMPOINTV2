package my.edu.um.umpoint.modules.space.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import my.edu.um.umpoint.common.constant.Constant;
import my.edu.um.umpoint.common.exception.BadHttpRequestException;
import my.edu.um.umpoint.common.exception.ErrorCode;
import my.edu.um.umpoint.common.utils.Result;
import my.edu.um.umpoint.modules.space.dto.SpcEventDTO;
import my.edu.um.umpoint.modules.space.service.SpcEventService;
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
@Tag(name = "Space Occupied Event")
public class SpcEventController{
    @Autowired
    private SpcEventService spcEventService;

    @GetMapping
    @Operation(summary = "List")
    @Parameters(
        {
            @Parameter(
                name = Constant.SPACE_ID, description = "Space ID", in = ParameterIn.QUERY, required = true,
                schema = @Schema(type = "integer")
            ),
            @Parameter(
                name = Constant.START_TIME, description = "Start Date Time", in = ParameterIn.QUERY,
                schema = @Schema(type = "string", format = "date-time")
            ),
            @Parameter(
                name = Constant.END_TIME, description = "End Date Time", in = ParameterIn.QUERY,
                schema = @Schema(type = "string", format = "date-time")
            ),
        }
    )
    public Result<List<SpcEventDTO>> page(@Parameter(hidden = true) @RequestParam Map<String, Object> params){
        if (
            !params.containsKey(Constant.SPACE_ID) ||
            params.get(Constant.SPACE_ID).toString().isEmpty()
        )
            throw new BadHttpRequestException(ErrorCode.PARAMS_GET_ERROR, "No Space ID selected");
        if (params.get(Constant.SPACE_ID).toString().contains(",")) {
            params.put(Constant.SPACE_ID_LIST, params.get(Constant.SPACE_ID).toString().split(","));
            params.remove(Constant.SPACE_ID);
        }
        System.out.println(params);
        return new Result<List<SpcEventDTO>>().ok(spcEventService.list(params));
    }
}
