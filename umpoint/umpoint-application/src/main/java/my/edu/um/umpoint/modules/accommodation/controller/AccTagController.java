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
import my.edu.um.umpoint.modules.accommodation.dto.AccTagDTO;
import my.edu.um.umpoint.modules.accommodation.service.AccTagService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Accommodation Tag
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-23
 */
@RestController
@RequestMapping("accommodation/tag")
@Tag(name="Accommodation Tag")
public class AccTagController {
    @Autowired
    private AccTagService accTagService;

    @GetMapping("page")
    @Operation(summary = "Pagination")
    @Parameters({
        @Parameter(name = Constant.PAGE, description = "Current page number, starting from 1", in = ParameterIn.QUERY, required = true, schema = @Schema(type = "integer")) ,
        @Parameter(name = Constant.LIMIT, description = "Number of records per page", in = ParameterIn.QUERY,required = true, schema = @Schema(type = "integer")) ,
        @Parameter(name = Constant.ORDER_FIELD, description = "Sort field", in = ParameterIn.QUERY, schema = @Schema(type = "string")) ,
        @Parameter(name = Constant.ORDER, description = "Sort order, optional values (asc, desc)", in = ParameterIn.QUERY, schema = @Schema(type = "string")) ,
        @Parameter(name = Constant.NAME, description = "Tag name", in = ParameterIn.QUERY, schema = @Schema(type = "string"))
    })
    @RequiresPermissions("accommodation:tag:page")
    public Result<PageData<AccTagDTO>> page(@Parameter(hidden = true) @RequestParam Map<String, Object> params){
        PageData<AccTagDTO> page = accTagService.page(params);

        return new Result<PageData<AccTagDTO>>().ok(page);
    }

    @GetMapping("list")
    @Operation(summary = "list")
    @RequiresPermissions("accommodation:tag:list")
    public Result<List<AccTagDTO>> list(){
        List<AccTagDTO> page = accTagService.list(new HashMap<>());

        return new Result<List<AccTagDTO>>().ok(page);
    }

    @GetMapping("list/filter")
    @Operation(summary = "list")
    @RequiresPermissions("accommodation:tag:list")
    public Result<List<AccTagDTO>> filterList(){
        HashMap<String, Object> params = new HashMap<>();
        params.put("filter", true);
        List<AccTagDTO> page = accTagService.list(params);

        return new Result<List<AccTagDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @Operation(summary = "Information")
    @RequiresPermissions("accommodation:tag:info")
    public Result<AccTagDTO> get(@PathVariable("id") Long id){
        AccTagDTO data = accTagService.get(id);

        return new Result<AccTagDTO>().ok(data);
    }

    @PostMapping
    @Operation(summary = "Save")
    @LogOperation("Save")
    @RequiresPermissions("accommodation:tag:save")
    public Result save(@RequestBody AccTagDTO dto){
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        accTagService.save(dto);

        return new Result();
    }

    @PutMapping
    @Operation(summary = "Update")
    @LogOperation("Update")
    @RequiresPermissions("accommodation:tag:update")
    public Result update(@RequestBody AccTagDTO dto){
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        accTagService.update(dto);

        return new Result();
    }

    @DeleteMapping
    @Operation(summary = "Delete")
    @LogOperation("Delete")
    @RequiresPermissions("accommodation:tag:delete")
    public Result delete(@RequestBody Long[] ids){
        AssertUtils.isArrayEmpty(ids, "id");

        accTagService.delete(ids);

        return new Result();
    }
}
