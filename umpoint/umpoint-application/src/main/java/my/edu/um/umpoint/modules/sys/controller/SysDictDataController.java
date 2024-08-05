package my.edu.um.umpoint.modules.sys.controller;

import my.edu.um.umpoint.common.annotation.LogOperation;
import my.edu.um.umpoint.common.constant.Constant;
import my.edu.um.umpoint.common.page.PageData;
import my.edu.um.umpoint.common.utils.Result;
import my.edu.um.umpoint.common.validator.AssertUtils;
import my.edu.um.umpoint.common.validator.ValidatorUtils;
import my.edu.um.umpoint.common.validator.group.DefaultGroup;
import my.edu.um.umpoint.common.validator.group.UpdateGroup;
import my.edu.um.umpoint.modules.sys.dto.SysDictDataDTO;
import my.edu.um.umpoint.modules.sys.service.SysDictDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("sys/dict/data")
@Tag(name = "dictionary data")
@AllArgsConstructor
public class SysDictDataController {
    private final SysDictDataService sysDictDataService;

    @GetMapping("page")
    @Operation(summary = "dictionary data")
    @Parameters({
            @Parameter(name = Constant.PAGE, description = "Current page number, starting from 1", in = ParameterIn.QUERY, required = true, ref="int") ,
            @Parameter(name = Constant.LIMIT, description = "Number of records per page", in = ParameterIn.QUERY,required = true, ref="int") ,
            @Parameter(name = Constant.ORDER_FIELD, description = "Sort field", in = ParameterIn.QUERY, ref="String") ,
            @Parameter(name = Constant.ORDER, description = "Sort order, optional values (asc, desc)", in = ParameterIn.QUERY, ref="String"),
            @Parameter(name = "dictLabel", description = "dict label", in = ParameterIn.QUERY, ref = "String"),
            @Parameter(name = "dictValue", description = "dict value", in = ParameterIn.QUERY, ref = "String")
    })
    @RequiresPermissions("sys:dict:page")
    public Result<PageData<SysDictDataDTO>> page(@Parameter(hidden = true) @RequestParam Map<String, Object> params) {
        PageData<SysDictDataDTO> page = sysDictDataService.page(params);

        return new Result<PageData<SysDictDataDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @Operation(summary = "info")
    @RequiresPermissions("sys:dict:info")
    public Result<SysDictDataDTO> get(@PathVariable("id") Long id) {
        SysDictDataDTO data = sysDictDataService.get(id);

        return new Result<SysDictDataDTO>().ok(data);
    }

    @PostMapping
    @Operation(summary = "save")
    @LogOperation("save")
    @RequiresPermissions("sys:dict:save")
    public Result save(@RequestBody SysDictDataDTO dto) {
        ValidatorUtils.validateEntity(dto, DefaultGroup.class);

        sysDictDataService.save(dto);

        return new Result();
    }

    @PutMapping
    @Operation(summary = "update")
    @LogOperation("update")
    @RequiresPermissions("sys:dict:update")
    public Result update(@RequestBody SysDictDataDTO dto) {
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        sysDictDataService.update(dto);

        return new Result();
    }

    @DeleteMapping
    @Operation(summary = "delete")
    @LogOperation("delete")
    @RequiresPermissions("sys:dict:delete")
    public Result delete(@RequestBody Long[] ids) {
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        sysDictDataService.delete(ids);

        return new Result();
    }

}
