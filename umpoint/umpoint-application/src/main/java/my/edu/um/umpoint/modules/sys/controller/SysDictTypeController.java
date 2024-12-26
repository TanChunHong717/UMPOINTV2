package my.edu.um.umpoint.modules.sys.controller;

import io.swagger.v3.oas.annotations.media.Schema;
import my.edu.um.umpoint.common.annotation.LogOperation;
import my.edu.um.umpoint.common.constant.Constant;
import my.edu.um.umpoint.common.page.PageData;
import my.edu.um.umpoint.common.utils.Result;
import my.edu.um.umpoint.common.validator.AssertUtils;
import my.edu.um.umpoint.common.validator.ValidatorUtils;
import my.edu.um.umpoint.common.validator.group.DefaultGroup;
import my.edu.um.umpoint.common.validator.group.UpdateGroup;
import my.edu.um.umpoint.modules.sys.dto.SysDictTypeDTO;
import my.edu.um.umpoint.modules.sys.entity.DictType;
import my.edu.um.umpoint.modules.sys.service.SysDictTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("sys/dict/type")
@Tag(name = "Dictionary Type")
@AllArgsConstructor
public class SysDictTypeController {
    private final SysDictTypeService sysDictTypeService;

    @GetMapping("page")
    @Operation(summary = "page")
    @Parameters({
            @Parameter(name = Constant.PAGE, description = "Current page number, starting from 1", in = ParameterIn.QUERY, required = true, schema = @Schema(type = "integer")) ,
            @Parameter(name = Constant.LIMIT, description = "Number of records per page", in = ParameterIn.QUERY,required = true, schema = @Schema(type = "integer")) ,
            @Parameter(name = Constant.ORDER_FIELD, description = "Sort field", in = ParameterIn.QUERY, schema = @Schema(type = "string")) ,
            @Parameter(name = Constant.ORDER, description = "Sort order, optional values (asc, desc)", in = ParameterIn.QUERY, schema = @Schema(type = "string")),
            @Parameter(name = "dictType", description = "dict type", in = ParameterIn.QUERY, schema = @Schema(type = "string")),
            @Parameter(name = "dictName", description = "dict name", in = ParameterIn.QUERY, schema = @Schema(type = "string"))
    })
    @RequiresPermissions("sys:dict:page")
    public Result<PageData<SysDictTypeDTO>> page(@Parameter(hidden = true) @RequestParam Map<String, Object> params) {
        PageData<SysDictTypeDTO> page = sysDictTypeService.page(params);

        return new Result<PageData<SysDictTypeDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @Operation(summary = "info")
    @RequiresPermissions("sys:dict:info")
    public Result<SysDictTypeDTO> get(@PathVariable("id") Long id) {
        SysDictTypeDTO data = sysDictTypeService.get(id);

        return new Result<SysDictTypeDTO>().ok(data);
    }

    @PostMapping
    @Operation(summary = "save")
    @LogOperation("save")
    @RequiresPermissions("sys:dict:save")
    public Result save(@RequestBody SysDictTypeDTO dto) {
        ValidatorUtils.validateEntity(dto, DefaultGroup.class);

        sysDictTypeService.save(dto);

        return new Result();
    }

    @PutMapping
    @Operation(summary = "update")
    @LogOperation("update")
    @RequiresPermissions("sys:dict:update")
    public Result update(@RequestBody SysDictTypeDTO dto) {
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        sysDictTypeService.update(dto);

        return new Result();
    }

    @DeleteMapping
    @Operation(summary = "delete")
    @LogOperation("delete")
    @RequiresPermissions("sys:dict:delete")
    public Result delete(@RequestBody Long[] ids) {
        AssertUtils.isArrayEmpty(ids, "id");

        sysDictTypeService.delete(ids);

        return new Result();
    }

    @GetMapping("all")
    @Operation(summary = "all")
    public Result<List<DictType>> all() {
        List<DictType> list = sysDictTypeService.getAllList();

        return new Result<List<DictType>>().ok(list);
    }

}
