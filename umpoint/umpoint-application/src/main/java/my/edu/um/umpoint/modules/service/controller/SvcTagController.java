package my.edu.um.umpoint.modules.service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
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
import my.edu.um.umpoint.modules.service.dto.SvcTagDTO;
import my.edu.um.umpoint.modules.service.service.SvcTagService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service Tag
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-20
 */
@RestController
@RequestMapping("service/tag")
@Tag(name="Service Tag")
public class SvcTagController {
    @Autowired
    private SvcTagService svcTagService;

    @GetMapping("page")
    @Operation(summary = "Pagination")
    @Parameters({
        @Parameter(name = Constant.PAGE, description = "Current page number, starting from 1", in = ParameterIn.QUERY, required = true, ref="int") ,
        @Parameter(name = Constant.LIMIT, description = "Number of records per page", in = ParameterIn.QUERY,required = true, ref="int") ,
        @Parameter(name = Constant.ORDER_FIELD, description = "Sort field", in = ParameterIn.QUERY, ref="String") ,
        @Parameter(name = Constant.ORDER, description = "Sort order, optional values (asc, desc)", in = ParameterIn.QUERY, ref="String") ,
        @Parameter(name = Constant.NAME, description = "Tag name", in = ParameterIn.QUERY, ref="String")
    })
    @RequiresPermissions("service:tag:page")
    public Result<PageData<SvcTagDTO>> page(@Parameter(hidden = true) @RequestParam Map<String, Object> params){
        PageData<SvcTagDTO> page = svcTagService.page(params);

        return new Result<PageData<SvcTagDTO>>().ok(page);
    }

    @GetMapping("list")
    @Operation(summary = "list")
    @RequiresPermissions("service:tag:list")
    public Result<List<SvcTagDTO>> list(){
        List<SvcTagDTO> page = svcTagService.list(new HashMap<>());

        return new Result<List<SvcTagDTO>>().ok(page);
    }

    @GetMapping("list/filter")
    @Operation(summary = "list")
    @RequiresPermissions("service:tag:list")
    public Result<List<SvcTagDTO>> filterList(){
        HashMap<String, Object> params = new HashMap<>();
        params.put("filter", true);
        List<SvcTagDTO> page = svcTagService.list(params);

        return new Result<List<SvcTagDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @Operation(summary = "Information")
    @RequiresPermissions("service:tag:info")
    public Result<SvcTagDTO> get(@PathVariable("id") Long id){
        SvcTagDTO data = svcTagService.get(id);

        return new Result<SvcTagDTO>().ok(data);
    }

    @PostMapping
    @Operation(summary = "Save")
    @LogOperation("Save")
    @RequiresPermissions("service:tag:save")
    public Result save(@RequestBody SvcTagDTO dto){
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        svcTagService.save(dto);

        return new Result();
    }

    @PutMapping
    @Operation(summary = "Update")
    @LogOperation("Update")
    @RequiresPermissions("service:tag:update")
    public Result update(@RequestBody SvcTagDTO dto){
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        svcTagService.update(dto);

        return new Result();
    }

    @DeleteMapping
    @Operation(summary = "Delete")
    @LogOperation("Delete")
    @RequiresPermissions("service:tag:delete")
    public Result delete(@RequestBody Long[] ids){
        AssertUtils.isArrayEmpty(ids, "id");

        svcTagService.delete(ids);

        return new Result();
    }
}
