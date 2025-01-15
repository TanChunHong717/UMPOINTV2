package my.edu.um.umpoint.modules.space.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import my.edu.um.umpoint.common.constant.Constant;
import my.edu.um.umpoint.common.page.PageData;
import my.edu.um.umpoint.common.utils.Result;
import my.edu.um.umpoint.modules.space.dto.SpcSpaceDTO;
import my.edu.um.umpoint.modules.space.service.SpcSpaceService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Space
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2025-01-07
 */
@RestController
@RequestMapping("space/public")
@Tag(name="Space Public")
public class SpcPublicController {
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
            @Parameter(name = Constant.DEPT_ID, description = "Department ID", in = ParameterIn.QUERY, schema = @Schema(type = "integer")) ,
            @Parameter(name = Constant.CAT_ID, description = "Category ID", in = ParameterIn.QUERY, schema = @Schema(type = "integer")) ,
            @Parameter(name = Constant.TAG_ID, description = "Tag ID", in = ParameterIn.QUERY, schema = @Schema(type = "integer"))
    })
    public Result<PageData<SpcSpaceDTO>> page(@Parameter(hidden = true) @RequestParam Map<String, Object> params){
        PageData<SpcSpaceDTO> page = spcSpaceService.spacePage(params);

        return new Result<PageData<SpcSpaceDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @Operation(summary = "Information")
    public Result<SpcSpaceDTO> get(@PathVariable("id") Long id){
        SpcSpaceDTO data = spcSpaceService.get(id);

        return new Result<SpcSpaceDTO>().ok(data);
    }
}