package my.edu.um.umpoint.modules.space.controller;

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
import my.edu.um.umpoint.modules.space.dto.SpcClosureDTO;
import my.edu.um.umpoint.modules.space.service.SpcClosureService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * Space Closure Period
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-09-16
 */
@RestController
@RequestMapping("space/closure")
@Tag(name="Space Closure Period")
public class SpcClosureController {
    @Autowired
    private SpcClosureService spcClosureService;

    @GetMapping("{id}")
    @Operation(summary = "Information")
    @RequiresPermissions("space:closure:info")
    public Result<SpcClosureDTO> get(@PathVariable("id") Long id){
        SpcClosureDTO data = spcClosureService.get(id);

        return new Result<SpcClosureDTO>().ok(data);
    }

    @PostMapping
    @Operation(summary = "Save")
    @LogOperation("Save")
    @RequiresPermissions("space:closure:save")
    public Result save(@RequestBody SpcClosureDTO dto){
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);
        validateEventTime(dto);

        spcClosureService.save(dto);

        return new Result();
    }

    @PutMapping
    @Operation(summary = "Update")
    @LogOperation("Update")
    @RequiresPermissions("space:closure:update")
    public Result update(@RequestBody SpcClosureDTO dto){
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);
        validateEventTime(dto);

        spcClosureService.update(dto);

        return new Result();
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Delete")
    @LogOperation("Delete")
    @RequiresPermissions("space:closure:delete")
    public Result delete(@PathVariable("id")  Long id){
        AssertUtils.isNull(id);

        spcClosureService.delete(id);

        return new Result();
    }

    private static void validateEventTime(SpcClosureDTO dto) {
        LocalDateTime startTime = DateUtils.convertDateTimeToLocalDateTime(dto.getStartDay(), dto.getStartTime());
        LocalDateTime endTime = DateUtils.convertDateTimeToLocalDateTime(dto.getStartDay(), dto.getEndTime());

        if (!startTime.isBefore(endTime) || startTime.isBefore(LocalDateTime.now())){
            throw new RenException("Invalid start time or end time");
        }
    }
}
