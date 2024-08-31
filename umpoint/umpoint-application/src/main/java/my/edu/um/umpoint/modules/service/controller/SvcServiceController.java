package my.edu.um.umpoint.modules.service.controller;

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
import my.edu.um.umpoint.modules.service.dto.SvcServiceDTO;
import my.edu.um.umpoint.modules.service.excel.SvcServiceExcel;
import my.edu.um.umpoint.modules.service.service.SvcServiceService;
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
 * Service
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-20
 */
@RestController
@RequestMapping("service/service")
@Tag(name="Service")
public class SvcServiceController {
    @Autowired
    private SvcServiceService svcServiceService;

    @GetMapping("page")
    @Operation(summary = "Pagination")
    @Parameters({
        @Parameter(name = Constant.PAGE, description = "Current page number, starting from 1", in = ParameterIn.QUERY, required = true, ref="int") ,
        @Parameter(name = Constant.LIMIT, description = "Number of records per page", in = ParameterIn.QUERY,required = true, ref="int") ,
        @Parameter(name = Constant.ORDER_FIELD, description = "Sort field", in = ParameterIn.QUERY, ref="String") ,
        @Parameter(name = Constant.ORDER, description = "Sort order, optional values (asc, desc)", in = ParameterIn.QUERY, ref="String")
    })
    @RequiresPermissions("service:service:page")
    public Result<PageData<SvcServiceDTO>> page(@Parameter(hidden = true) @RequestParam Map<String, Object> params){
        PageData<SvcServiceDTO> page = svcServiceService.page(params);

        return new Result<PageData<SvcServiceDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @Operation(summary = "Information")
    @RequiresPermissions("service:service:info")
    public Result<SvcServiceDTO> get(@PathVariable("id") Long id){
        SvcServiceDTO data = svcServiceService.get(id);

        return new Result<SvcServiceDTO>().ok(data);
    }

    @PostMapping
    @Operation(summary = "Save")
    @LogOperation("Save")
    @RequiresPermissions("service:service:save")
    public Result save(@RequestBody SvcServiceDTO dto){
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);
        validateServiceTagDTO(dto);
        validateServiceImageDTO(dto);
        if (dto.getSvcBookingRuleDTO() != null) {
            ValidatorUtils.validateEntity(dto.getSvcBookingRuleDTO(), AddGroup.class, DefaultGroup.class);
        }

        svcServiceService.save(dto);

        return new Result();
    }

    @PutMapping
    @Operation(summary = "Update")
    @LogOperation("Update")
    @RequiresPermissions("service:service:update")
    public Result update(@RequestBody SvcServiceDTO dto){
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);
        validateServiceTagDTO(dto);
        validateServiceImageDTO(dto);
        if (dto.getSvcBookingRuleDTO() != null) {
            ValidatorUtils.validateEntity(dto.getSvcBookingRuleDTO(), UpdateGroup.class, DefaultGroup.class);
        }

        svcServiceService.update(dto);

        return new Result();
    }

    @DeleteMapping
    @Operation(summary = "Delete")
    @LogOperation("Delete")
    @RequiresPermissions("service:service:delete")
    public Result delete(@RequestBody Long[] ids){
        AssertUtils.isArrayEmpty(ids, "id");

        svcServiceService.delete(ids);

        return new Result();
    }

    @GetMapping("export")
    @Operation(summary = "Export")
    @LogOperation("Export")
    @RequiresPermissions("service:service:export")
    public void export(@Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        List<SvcServiceDTO> list = svcServiceService.list(params);

        ExcelUtils.exportExcelToTarget(response, null, "Service", list, SvcServiceExcel.class);
    }

    private void validateServiceTagDTO(SvcServiceDTO dto) {
        if (dto.getSvcTagDTOList() != null && !dto.getSvcTagDTOList().isEmpty()) {
            dto.getSvcTagDTOList().forEach(tagDTO -> {
                ValidatorUtils.validateEntity(
                        tagDTO,
                        InsertGroup.class
                );
            });
        }
    }

    private void validateServiceImageDTO(SvcServiceDTO dto) {
        if (dto.getSvcImageDTOList() != null && !dto.getSvcImageDTOList().isEmpty()) {
            dto.getSvcImageDTOList().forEach(imageDTO -> {
                ValidatorUtils.validateEntity(
                        imageDTO,
                        InsertGroup.class
                );
            });
        }
    }
}
