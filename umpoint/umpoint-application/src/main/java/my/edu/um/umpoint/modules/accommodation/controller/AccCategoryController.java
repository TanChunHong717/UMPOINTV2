package my.edu.um.umpoint.modules.accommodation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import my.edu.um.umpoint.common.annotation.LogOperation;
import my.edu.um.umpoint.common.constant.Constant;
import my.edu.um.umpoint.common.page.PageData;
import my.edu.um.umpoint.common.utils.Result;
import my.edu.um.umpoint.common.validator.AssertUtils;
import my.edu.um.umpoint.common.validator.ValidatorUtils;
import my.edu.um.umpoint.common.validator.group.AddGroup;
import my.edu.um.umpoint.common.validator.group.DefaultGroup;
import my.edu.um.umpoint.common.validator.group.UpdateGroup;
import my.edu.um.umpoint.modules.accommodation.dto.AccCategoryDTO;
import my.edu.um.umpoint.modules.accommodation.service.AccCategoryService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Accommodation Category
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-23
 */
@RestController
@RequestMapping("accommodation/category")
@Tag(name="Accommodation Category")
public class AccCategoryController {
    @Autowired
    private AccCategoryService accCategoryService;

    @GetMapping("page")
    @Operation(summary = "Pagination")
    @Parameters({
        @Parameter(name = Constant.PAGE, description = "Current page number, starting from 1", in = ParameterIn.QUERY, required = true, schema = @Schema(type = "integer")) ,
        @Parameter(name = Constant.LIMIT, description = "Number of records per page", in = ParameterIn.QUERY,required = true, schema = @Schema(type = "integer")) ,
        @Parameter(name = Constant.ORDER_FIELD, description = "Sort field", in = ParameterIn.QUERY, schema = @Schema(type = "string")) ,
        @Parameter(name = Constant.ORDER, description = "Sort order, optional values (asc, desc)", in = ParameterIn.QUERY, schema = @Schema(type = "string")) ,
        @Parameter(name = Constant.NAME, description = "Category name", in = ParameterIn.QUERY, schema = @Schema(type = "string"))
    })
    @RequiresPermissions("accommodation:category:page")
    public Result<PageData<AccCategoryDTO>> page(@Parameter(hidden = true) @RequestParam Map<String, Object> params){
        PageData<AccCategoryDTO> page = accCategoryService.page(params);

        return new Result<PageData<AccCategoryDTO>>().ok(page);
    }

    @GetMapping("list")
    @Operation(summary = "list")
    @RequiresPermissions("accommodation:category:list")
    public Result<List<AccCategoryDTO>> list(){
        List<AccCategoryDTO> page = accCategoryService.list(new HashMap<>());

        return new Result<List<AccCategoryDTO>>().ok(page);
    }

    @GetMapping("list/filter")
    @Operation(summary = "filter list")
    public Result<List<AccCategoryDTO>> filterList(){
        HashMap<String, Object> params = new HashMap<>();
        params.put("filter", true);
        List<AccCategoryDTO> page = accCategoryService.list(params);

        return new Result<List<AccCategoryDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @Operation(summary = "Information")
    @RequiresPermissions("accommodation:category:info")
    public Result<AccCategoryDTO> get(@PathVariable("id") Long id){
        AccCategoryDTO data = accCategoryService.get(id);

        return new Result<AccCategoryDTO>().ok(data);
    }

    @PostMapping
    @Operation(summary = "Save")
    @LogOperation("Save")
    @RequiresPermissions("accommodation:category:save")
    public Result save(@RequestBody AccCategoryDTO dto){
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        accCategoryService.save(dto);

        return new Result();
    }

    @PutMapping
    @Operation(summary = "Update")
    @LogOperation("Update")
    @RequiresPermissions("accommodation:category:update")
    public Result update(@RequestBody AccCategoryDTO dto){
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        accCategoryService.update(dto);

        return new Result();
    }

    @DeleteMapping
    @Operation(summary = "Delete")
    @LogOperation("Delete")
    @RequiresPermissions("accommodation:category:delete")
    public Result delete(@RequestBody Long[] ids){
        AssertUtils.isArrayEmpty(ids, "id");

        accCategoryService.delete(ids);

        return new Result();
    }
}
