package my.edu.um.umpoint.modules.space.controller;

import my.edu.um.umpoint.common.annotation.DataFilter;
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
import my.edu.um.umpoint.modules.space.dto.SpaceDTO;
import my.edu.um.umpoint.modules.space.excel.SpaceExcel;
import my.edu.um.umpoint.modules.space.service.SpaceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


/**
 * Space
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-03
 */
@RestController
@RequestMapping("space/space")
@Tag(name="Space")
public class SpaceController {
    @Autowired
    private SpaceService spaceService;

    @GetMapping("page")
    @Operation(summary = "Pagination")
    @Parameters({
        @Parameter(name = Constant.PAGE, description = "Current page number, starting from 1", in = ParameterIn.QUERY, required = true, ref="int") ,
        @Parameter(name = Constant.LIMIT, description = "Number of records per page", in = ParameterIn.QUERY,required = true, ref="int") ,
        @Parameter(name = Constant.ORDER_FIELD, description = "Sort field", in = ParameterIn.QUERY, ref="String") ,
        @Parameter(name = Constant.ORDER, description = "Sort order, optional values (asc, desc)", in = ParameterIn.QUERY, ref="String")
    })
    @RequiresPermissions("space:space:page")
    @DataFilter(tableAlias = "s")
    public Result<PageData<SpaceDTO>> page(@Parameter(hidden = true) @RequestParam Map<String, Object> params){
        PageData<SpaceDTO> page = spaceService.page(params);

        return new Result<PageData<SpaceDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @Operation(summary = "Information")
    @RequiresPermissions("space:space:info")
    public Result<SpaceDTO> get(@PathVariable("id") Long id){
        SpaceDTO data = spaceService.get(id);

        return new Result<SpaceDTO>().ok(data);
    }

    @PostMapping
    @Operation(summary = "Save")
    @LogOperation("Save")
    @RequiresPermissions("space:space:save")
    public Result save(@RequestBody SpaceDTO dto){
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        spaceService.save(dto);

        return new Result();
    }

    @PutMapping
    @Operation(summary = "Update")
    @LogOperation("Update")
    @RequiresPermissions("space:space:update")
    public Result update(@RequestBody SpaceDTO dto){
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        spaceService.update(dto);

        return new Result();
    }

    @DeleteMapping
    @Operation(summary = "Delete")
    @LogOperation("Delete")
    @RequiresPermissions("space:space:delete")
    public Result delete(@RequestBody Long[] ids){
        AssertUtils.isArrayEmpty(ids, "id");

        spaceService.delete(ids);

        return new Result();
    }

    @GetMapping("export")
    @Operation(summary = "Export")
    @LogOperation("Export")
    @RequiresPermissions("space:space:export")
    public void export(@Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        List<SpaceDTO> list = spaceService.list(params);

        ExcelUtils.exportExcelToTarget(response, null, "Space", list, SpaceExcel.class);
    }

}
