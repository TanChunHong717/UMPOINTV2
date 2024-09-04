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
import my.edu.um.umpoint.common.validator.group.InsertGroup;
import my.edu.um.umpoint.common.validator.group.UpdateGroup;
import my.edu.um.umpoint.modules.space.dto.SpcSpaceDTO;
import my.edu.um.umpoint.modules.space.excel.SpcSpaceExcel;
import my.edu.um.umpoint.modules.space.service.SpcSpaceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
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
public class SpcSpaceController {
    @Autowired
    private SpcSpaceService spcSpaceService;

    @GetMapping("page")
    @Operation(summary = "Pagination")
    @Parameters({
        @Parameter(name = Constant.PAGE, description = "Current page number, starting from 1", in = ParameterIn.QUERY, required = true, ref="int") ,
        @Parameter(name = Constant.LIMIT, description = "Number of records per page", in = ParameterIn.QUERY,required = true, ref="int") ,
        @Parameter(name = Constant.ORDER_FIELD, description = "Sort field", in = ParameterIn.QUERY, ref="String") ,
        @Parameter(name = Constant.ORDER, description = "Sort order, optional values (asc, desc)", in = ParameterIn.QUERY, ref="String")
    })
    @RequiresPermissions("space:space:page")
    public Result<PageData<SpcSpaceDTO>> page(@Parameter(hidden = true) @RequestParam Map<String, Object> params){
        PageData<SpcSpaceDTO> page = spcSpaceService.page(params);

        return new Result<PageData<SpcSpaceDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @Operation(summary = "Information")
    @RequiresPermissions("space:space:info")
    public Result<SpcSpaceDTO> get(@PathVariable("id") Long id){
        SpcSpaceDTO data = spcSpaceService.get(id);

        return new Result<SpcSpaceDTO>().ok(data);
    }

    @PostMapping
    @Operation(summary = "Save")
    @LogOperation("Save")
    @RequiresPermissions("space:space:save")
    @Transactional
    public Result save(@RequestBody SpcSpaceDTO dto){
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);
        validateSpaceImageDTO(dto);
        validateSpaceTagDTO(dto);

        spcSpaceService.save(dto);

        return new Result();
    }

    @PutMapping
    @Operation(summary = "Update")
    @LogOperation("Update")
    @RequiresPermissions("space:space:update")
    @Transactional
    public Result update(@RequestBody SpcSpaceDTO dto){
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);
        validateSpaceBookingRuleDTO(dto);
        validateSpaceImageDTO(dto);
        validateSpaceTagDTO(dto);

        spcSpaceService.update(dto);

        return new Result();
    }

    @DeleteMapping
    @Operation(summary = "Delete")
    @LogOperation("Delete")
    @RequiresPermissions("space:space:delete")
    public Result delete(@RequestBody Long[] ids){
        AssertUtils.isArrayEmpty(ids, "id");

        spcSpaceService.delete(ids);

        return new Result();
    }

    @GetMapping("export")
    @Operation(summary = "Export")
    @LogOperation("Export")
    @RequiresPermissions("space:space:export")
    public void export(@Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        List<SpcSpaceDTO> list = spcSpaceService.list(params);

        ExcelUtils.exportExcelToTarget(response, null, "Space", list, SpcSpaceExcel.class);
    }

    private static void validateSpaceBookingRuleDTO(SpcSpaceDTO dto) {
        if (dto.getSpcBookingRuleDTO() != null) {
            if (dto.getBookingRuleId() == null)
                ValidatorUtils.validateEntity(dto.getSpcBookingRuleDTO(), AddGroup.class, DefaultGroup.class);
            else
                ValidatorUtils.validateEntity(dto.getSpcBookingRuleDTO(), UpdateGroup.class, DefaultGroup.class);
        }
    }

    private void validateSpaceImageDTO(SpcSpaceDTO dto) {
        if (dto.getSpcImageDTOList() != null && !dto.getSpcImageDTOList().isEmpty()) {
            dto.getSpcImageDTOList().forEach(imageDTO -> {
                ValidatorUtils.validateEntity(
                        imageDTO,
                        InsertGroup.class
                );
            });
        }
    }

    private void validateSpaceTagDTO(SpcSpaceDTO dto) {
        if (dto.getSpcTagDTOList() != null && !dto.getSpcTagDTOList().isEmpty()) {
            dto.getSpcTagDTOList().forEach(tagDTO -> {
                ValidatorUtils.validateEntity(
                        tagDTO,
                        InsertGroup.class
                );
            });
        }
    }
}
