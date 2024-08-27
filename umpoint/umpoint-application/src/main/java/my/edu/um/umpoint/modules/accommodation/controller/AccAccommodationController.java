package my.edu.um.umpoint.modules.accommodation.controller;

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
import my.edu.um.umpoint.modules.accommodation.dto.AccAccommodationDTO;
import my.edu.um.umpoint.modules.accommodation.excel.AccAccommodationExcel;
import my.edu.um.umpoint.modules.accommodation.service.AccAccommodationService;
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
 * Accommodation
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-23
 */
@RestController
@RequestMapping("accommodation/accommodation")
@Tag(name="Accommodation")
public class AccAccommodationController {
    @Autowired
    private AccAccommodationService accAccommodationService;

    @GetMapping("page")
    @Operation(summary = "Pagination")
    @Parameters({
        @Parameter(name = Constant.PAGE, description = "Current page number, starting from 1", in = ParameterIn.QUERY, required = true, ref="int") ,
        @Parameter(name = Constant.LIMIT, description = "Number of records per page", in = ParameterIn.QUERY,required = true, ref="int") ,
        @Parameter(name = Constant.ORDER_FIELD, description = "Sort field", in = ParameterIn.QUERY, ref="String") ,
        @Parameter(name = Constant.ORDER, description = "Sort order, optional values (asc, desc)", in = ParameterIn.QUERY, ref="String")
    })
    @RequiresPermissions("accommodation:accommodation:page")
    public Result<PageData<AccAccommodationDTO>> page(@Parameter(hidden = true) @RequestParam Map<String, Object> params){
        PageData<AccAccommodationDTO> page = accAccommodationService.page(params);

        return new Result<PageData<AccAccommodationDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @Operation(summary = "Information")
    @RequiresPermissions("accommodation:accommodation:info")
    public Result<AccAccommodationDTO> get(@PathVariable("id") Long id){
        AccAccommodationDTO data = accAccommodationService.get(id);

        return new Result<AccAccommodationDTO>().ok(data);
    }

    @PostMapping
    @Operation(summary = "Save")
    @LogOperation("Save")
    @RequiresPermissions("accommodation:accommodation:save")
    public Result save(@RequestBody AccAccommodationDTO dto){
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);
        validateAccommodationTagDTO(dto);
        validateAccommodationImageDTO(dto);
        if (dto.getAccBookingRuleDTO() != null) {
            ValidatorUtils.validateEntity(dto.getAccBookingRuleDTO(), AddGroup.class, DefaultGroup.class);
        }

        accAccommodationService.save(dto);

        return new Result();
    }

    @PutMapping
    @Operation(summary = "Update")
    @LogOperation("Update")
    @RequiresPermissions("accommodation:accommodation:update")
    public Result update(@RequestBody AccAccommodationDTO dto){
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);
        validateAccommodationTagDTO(dto);
        validateAccommodationImageDTO(dto);
        if (dto.getAccBookingRuleDTO() != null) {
            ValidatorUtils.validateEntity(dto.getAccBookingRuleDTO(), AddGroup.class, DefaultGroup.class);
        }
        accAccommodationService.update(dto);

        return new Result();
    }

    @DeleteMapping
    @Operation(summary = "Delete")
    @LogOperation("Delete")
    @RequiresPermissions("accommodation:accommodation:delete")
    public Result delete(@RequestBody Long[] ids){
        AssertUtils.isArrayEmpty(ids, "id");

        accAccommodationService.delete(ids);

        return new Result();
    }

    @GetMapping("export")
    @Operation(summary = "Export")
    @LogOperation("Export")
    @RequiresPermissions("accommodation:accommodation:export")
    public void export(@Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        List<AccAccommodationDTO> list = accAccommodationService.list(params);

        ExcelUtils.exportExcelToTarget(response, null, "Accommodation", list, AccAccommodationExcel.class);
    }

    private void validateAccommodationTagDTO(AccAccommodationDTO dto) {
        dto.getAccTagDTOList().forEach(tagDTO -> {
            ValidatorUtils.validateEntity(
                    tagDTO,
                    InsertGroup.class
            );
        });
    }

    private void validateAccommodationImageDTO(AccAccommodationDTO dto) {
        dto.getAccImageDTOList().forEach(imageDTO -> {
            ValidatorUtils.validateEntity(
                    imageDTO,
                    InsertGroup.class
            );
        });
    }
}
