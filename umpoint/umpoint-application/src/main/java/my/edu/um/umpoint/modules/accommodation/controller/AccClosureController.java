package my.edu.um.umpoint.modules.accommodation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import my.edu.um.umpoint.common.annotation.LogOperation;
import my.edu.um.umpoint.common.exception.RenException;
import my.edu.um.umpoint.common.utils.DateUtils;
import my.edu.um.umpoint.common.utils.Result;
import my.edu.um.umpoint.common.validator.AssertUtils;
import my.edu.um.umpoint.common.validator.ValidatorUtils;
import my.edu.um.umpoint.common.validator.group.AddGroup;
import my.edu.um.umpoint.common.validator.group.DefaultGroup;
import my.edu.um.umpoint.common.validator.group.UpdateGroup;
import my.edu.um.umpoint.modules.accommodation.dto.AccClosureDTO;
import my.edu.um.umpoint.modules.accommodation.service.AccClosureService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Accommodation Closure Period
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-09-16
 */
@RestController
@RequestMapping("accommodation/closure")
@Tag(name="Accommodation Closure Period")
public class AccClosureController {
    @Autowired
    private AccClosureService accClosureService;

    @GetMapping("{id}")
    @Operation(summary = "Information")
    @RequiresPermissions("accommodation:closure:info")
    public Result<AccClosureDTO> get(@PathVariable("id") Long id){
        AccClosureDTO data = accClosureService.get(id);

        return new Result<AccClosureDTO>().ok(data);
    }

    @PostMapping
    @Operation(summary = "Save")
    @LogOperation("Save")
    @RequiresPermissions("accommodation:closure:save")
    public Result save(@RequestBody AccClosureDTO dto){
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);
        validateEventTime(dto);

        accClosureService.save(dto);

        return new Result();
    }

    @PutMapping
    @Operation(summary = "Update")
    @LogOperation("Update")
    @RequiresPermissions("accommodation:closure:update")
    public Result update(@RequestBody AccClosureDTO dto){
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);
        validateEventTime(dto);

        accClosureService.update(dto);

        return new Result();
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Delete")
    @LogOperation("Delete")
    @RequiresPermissions("accommodation:closure:delete")
    public Result delete(@PathVariable("id")  Long id){
        AssertUtils.isNull(id);

        accClosureService.delete(id);

        return new Result();
    }

    private static void validateEventTime(AccClosureDTO dto) {
        LocalDate startDate = DateUtils.convertDateToLocalDate(dto.getStartDay());
        LocalDate endDate = DateUtils.convertDateToLocalDate(dto.getEndDay());

        if (!startDate.isBefore(endDate) || startDate.isBefore(LocalDate.now())){
            throw new RenException("Invalid start time or end time");
        }
    }
}
