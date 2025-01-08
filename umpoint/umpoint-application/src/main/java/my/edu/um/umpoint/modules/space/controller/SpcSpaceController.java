package my.edu.um.umpoint.modules.space.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import my.edu.um.umpoint.common.annotation.LogOperation;
import my.edu.um.umpoint.common.constant.Constant;
import my.edu.um.umpoint.common.exception.RenException;
import my.edu.um.umpoint.common.page.PageData;
import my.edu.um.umpoint.common.utils.ExcelUtils;
import my.edu.um.umpoint.common.utils.Result;
import my.edu.um.umpoint.common.validator.AssertUtils;
import my.edu.um.umpoint.common.validator.ValidatorUtils;
import my.edu.um.umpoint.common.validator.group.AddGroup;
import my.edu.um.umpoint.common.validator.group.DefaultGroup;
import my.edu.um.umpoint.common.validator.group.InsertGroup;
import my.edu.um.umpoint.common.validator.group.UpdateGroup;
import my.edu.um.umpoint.modules.space.dto.SpcBookingRuleDTO;
import my.edu.um.umpoint.modules.space.dto.SpcSpaceDTO;
import my.edu.um.umpoint.modules.space.excel.SpcSpaceExcel;
import my.edu.um.umpoint.modules.space.service.SpcSpaceService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
        @Parameter(name = Constant.PAGE, description = "Current page number, starting from 1", in = ParameterIn.QUERY, required = true, schema = @Schema(type = "integer")) ,
        @Parameter(name = Constant.LIMIT, description = "Number of records per page", in = ParameterIn.QUERY,required = true, schema = @Schema(type = "integer")) ,
        @Parameter(name = Constant.ORDER_FIELD, description = "Sort field", in = ParameterIn.QUERY, schema = @Schema(type = "string")) ,
        @Parameter(name = Constant.ORDER, description = "Sort order, optional values (asc, desc)", in = ParameterIn.QUERY, schema = @Schema(type = "string")) ,
        @Parameter(name = Constant.NAME, description = "Name", in = ParameterIn.QUERY, schema = @Schema(type = "string")) ,
        @Parameter(name = Constant.DEPT_ID, description = "Department ID", in = ParameterIn.QUERY, schema = @Schema(type = "integer")) ,
        @Parameter(name = Constant.CAT_ID, description = "Category ID", in = ParameterIn.QUERY, schema = @Schema(type = "integer")) ,
        @Parameter(name = Constant.TAG_ID, description = "Tag ID", in = ParameterIn.QUERY, schema = @Schema(type = "integer"))
    })
    @RequiresPermissions("space:space:page")
    public Result<PageData<SpcSpaceDTO>> page(@Parameter(hidden = true) @RequestParam Map<String, Object> params){
        PageData<SpcSpaceDTO> page = spcSpaceService.spacePage(params);

        return new Result<PageData<SpcSpaceDTO>>().ok(page);
    }

    @GetMapping("list")
    @Operation(summary = "List out all space with booking rule")
    @RequiresPermissions("space:space:page")
    public Result<List<SpcSpaceDTO>> list(){
        List<SpcSpaceDTO> data = spcSpaceService.listWithBookingRule();

        return new Result<List<SpcSpaceDTO>>().ok(data);
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
    public Result update(@RequestBody SpcSpaceDTO dto){
        //Allow to update partial field
        ValidatorUtils.validateEntity(dto, UpdateGroup.class);
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
            SpcBookingRuleDTO bookingRuleDTO = dto.getSpcBookingRuleDTO();
            if (dto.getBookingRuleId() == null)
                ValidatorUtils.validateEntity(bookingRuleDTO, AddGroup.class, DefaultGroup.class);
            else
                ValidatorUtils.validateEntity(bookingRuleDTO, UpdateGroup.class);

            if (bookingRuleDTO.getBookingMode() == 1) {
                long diffInMin = (bookingRuleDTO.getStartTime().getTime() - bookingRuleDTO.getEndTime().getTime()) / 60000;
                if (diffInMin % bookingRuleDTO.getBookingUnit().intValue() != 0)
                    throw new RenException("The difference between start time and end time must be a multiple of the booking unit.");
            }
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
