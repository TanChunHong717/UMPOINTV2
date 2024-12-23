package my.edu.um.umpoint.modules.service.controller;

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
import my.edu.um.umpoint.modules.service.dto.SvcCategoryDTO;
import my.edu.um.umpoint.modules.service.service.SvcCategoryService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service Category
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-20
 */
@RestController
@RequestMapping("service/category")
@Tag(name="Service Category")
public class SvcCategoryController {
    @Autowired
    private SvcCategoryService svcCategoryService;

    @GetMapping("page")
    @Operation(summary = "Pagination")
    @Parameters({
        @Parameter(name = Constant.PAGE, description = "Current page number, starting from 1", in = ParameterIn.QUERY, required = true, schema = @Schema(type = "integer")) ,
        @Parameter(name = Constant.LIMIT, description = "Number of records per page", in = ParameterIn.QUERY,required = true, schema = @Schema(type = "integer")) ,
        @Parameter(name = Constant.ORDER_FIELD, description = "Sort field", in = ParameterIn.QUERY, schema = @Schema(type = "string")) ,
        @Parameter(name = Constant.ORDER, description = "Sort order, optional values (asc, desc)", in = ParameterIn.QUERY, schema = @Schema(type = "string")) ,
        @Parameter(name = Constant.NAME, description = "Category name", in = ParameterIn.QUERY, schema = @Schema(type = "string"))
    })
    @RequiresPermissions("service:category:page")
    public Result<PageData<SvcCategoryDTO>> page(@Parameter(hidden = true) @RequestParam Map<String, Object> params){
        PageData<SvcCategoryDTO> page = svcCategoryService.page(params);

        return new Result<PageData<SvcCategoryDTO>>().ok(page);
    }

    @GetMapping("list")
    @Operation(summary = "list")
    @RequiresPermissions("service:category:list")
    public Result<List<SvcCategoryDTO>> list(){
        List<SvcCategoryDTO> page = svcCategoryService.list(new HashMap<>());

        return new Result<List<SvcCategoryDTO>>().ok(page);
    }

    @GetMapping("list/filter")
    @Operation(summary = "filter list")
    public Result<List<SvcCategoryDTO>> filterList(){
        HashMap<String, Object> params = new HashMap<>();
        params.put("filter", true);
        List<SvcCategoryDTO> page = svcCategoryService.list(params);

        return new Result<List<SvcCategoryDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @Operation(summary = "Information")
    @RequiresPermissions("service:category:info")
    public Result<SvcCategoryDTO> get(@PathVariable("id") Long id){
        SvcCategoryDTO data = svcCategoryService.get(id);

        return new Result<SvcCategoryDTO>().ok(data);
    }

    @PostMapping
    @Operation(summary = "Save")
    @LogOperation("Save")
    @RequiresPermissions("service:category:save")
    public Result save(@RequestBody SvcCategoryDTO dto){
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        svcCategoryService.save(dto);

        return new Result();
    }

    @PutMapping
    @Operation(summary = "Update")
    @LogOperation("Update")
    @RequiresPermissions("service:category:update")
    public Result update(@RequestBody SvcCategoryDTO dto){
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        svcCategoryService.update(dto);

        return new Result();
    }

    @DeleteMapping
    @Operation(summary = "Delete")
    @LogOperation("Delete")
    @RequiresPermissions("service:category:delete")
    public Result delete(@RequestBody Long[] ids){
        AssertUtils.isArrayEmpty(ids, "id");

        svcCategoryService.delete(ids);

        return new Result();
    }
}
