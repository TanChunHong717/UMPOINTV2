package my.edu.um.umpoint.modules.space.controller;

import my.edu.um.umpoint.common.annotation.LogOperation;
import my.edu.um.umpoint.common.constant.Constant;
import my.edu.um.umpoint.common.page.PageData;
import my.edu.um.umpoint.common.utils.ExcelUtils;
import my.edu.um.umpoint.common.utils.Result;
import my.edu.um.umpoint.common.validator.AssertUtils;
import my.edu.um.umpoint.common.validator.ValidatorUtils;
import my.edu.um.umpoint.common.validator.group.AddGroup;
import my.edu.um.umpoint.common.validator.group.DefaultGroup;
import my.edu.um.umpoint.common.validator.group.UpdateGroup;
import my.edu.um.umpoint.modules.space.dto.SpcTagDTO;
import my.edu.um.umpoint.modules.space.excel.SpcTagExcel;
import my.edu.um.umpoint.modules.space.service.SpcTagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import jakarta.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Space Tag
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-03
 */
@RestController
@RequestMapping("space/tag")
@Tag(name="Space Tag")
public class SpcTagController {
    @Autowired
    private SpcTagService spcTagService;

    @GetMapping("page")
    @Operation(summary = "Pagination")
    @Parameters({
        @Parameter(name = Constant.PAGE, description = "Current page number, starting from 1", in = ParameterIn.QUERY, required = true, ref="int") ,
        @Parameter(name = Constant.LIMIT, description = "Number of records per page", in = ParameterIn.QUERY,required = true, ref="int") ,
        @Parameter(name = Constant.ORDER_FIELD, description = "Sort field", in = ParameterIn.QUERY, ref="String") ,
        @Parameter(name = Constant.ORDER, description = "Sort order, optional values (asc, desc)", in = ParameterIn.QUERY, ref="String")
    })
    @RequiresPermissions("space:tag:page")
    public Result<PageData<SpcTagDTO>> page(@Parameter(hidden = true) @RequestParam Map<String, Object> params){
        PageData<SpcTagDTO> page = spcTagService.page(params);

        return new Result<PageData<SpcTagDTO>>().ok(page);
    }

    @GetMapping("list")
    @Operation(summary = "list")
    @RequiresPermissions("space:tag:list")
    public Result<List<SpcTagDTO>> list(){
        List<SpcTagDTO> page = spcTagService.list(new HashMap<>());

        return new Result<List<SpcTagDTO>>().ok(page);
    }

    @GetMapping("list/filter")
    @Operation(summary = "list")
    @RequiresPermissions("space:tag:list")
    public Result<List<SpcTagDTO>> filterList(){
        HashMap<String, Object> params = new HashMap<>();
        params.put("filter", true);
        List<SpcTagDTO> page = spcTagService.list(params);

        return new Result<List<SpcTagDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @Operation(summary = "Information")
    @RequiresPermissions("space:tag:info")
    public Result<SpcTagDTO> get(@PathVariable("id") Long id){
        SpcTagDTO data = spcTagService.get(id);

        return new Result<SpcTagDTO>().ok(data);
    }

    @PostMapping
    @Operation(summary = "Save")
    @LogOperation("Save")
    @RequiresPermissions("space:tag:save")
    public Result save(@RequestBody SpcTagDTO dto){
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        spcTagService.save(dto);

        return new Result();
    }

    @PutMapping
    @Operation(summary = "Update")
    @LogOperation("Update")
    @RequiresPermissions("space:tag:update")
    public Result update(@RequestBody SpcTagDTO dto){
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        spcTagService.update(dto);

        return new Result();
    }

    @DeleteMapping
    @Operation(summary = "Delete")
    @LogOperation("Delete")
    @RequiresPermissions("space:tag:delete")
    public Result delete(@RequestBody Long[] ids){
        AssertUtils.isArrayEmpty(ids, "id");

        spcTagService.delete(ids);

        return new Result();
    }

    @GetMapping("export")
    @Operation(summary = "Export")
    @LogOperation("Export")
    @RequiresPermissions("space:tag:export")
    public void export(@Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        List<SpcTagDTO> list = spcTagService.list(params);

        ExcelUtils.exportExcelToTarget(response, null, "Space Tag", list, SpcTagExcel.class);
    }

}
