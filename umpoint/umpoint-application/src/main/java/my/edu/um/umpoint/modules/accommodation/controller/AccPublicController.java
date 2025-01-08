package my.edu.um.umpoint.modules.accommodation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import my.edu.um.umpoint.common.annotation.LogOperation;
import my.edu.um.umpoint.common.constant.Constant;
import my.edu.um.umpoint.common.page.PageData;
import my.edu.um.umpoint.common.utils.ExcelUtils;
import my.edu.um.umpoint.common.utils.Result;
import my.edu.um.umpoint.common.validator.AssertUtils;
import my.edu.um.umpoint.common.validator.ValidatorUtils;
import my.edu.um.umpoint.common.validator.group.AddGroup;
import my.edu.um.umpoint.common.validator.group.DefaultGroup;
import my.edu.um.umpoint.common.validator.group.InsertGroup;
import my.edu.um.umpoint.common.validator.group.UpdateGroup;
import my.edu.um.umpoint.modules.accommodation.dto.AccAccommodationDTO;
import my.edu.um.umpoint.modules.accommodation.excel.AccAccommodationExcel;
import my.edu.um.umpoint.modules.accommodation.service.AccAccommodationService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Accommodation
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-23
 */
@RestController
@RequestMapping("accommodation/public")
@Tag(name="Accommodation")
public class AccPublicController {
    @Autowired
    private AccAccommodationService accAccommodationService;

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
    public Result<PageData<AccAccommodationDTO>> page(@Parameter(hidden = true) @RequestParam Map<String, Object> params){
        PageData<AccAccommodationDTO> page = accAccommodationService.accommodationPage(params);

        return new Result<PageData<AccAccommodationDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @Operation(summary = "Information")
    public Result<AccAccommodationDTO> get(@PathVariable("id") Long id){
        AccAccommodationDTO data = accAccommodationService.get(id);

        return new Result<AccAccommodationDTO>().ok(data);
    }
}
