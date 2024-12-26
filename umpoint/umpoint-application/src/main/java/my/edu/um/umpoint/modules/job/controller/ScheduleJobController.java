package my.edu.um.umpoint.modules.job.controller;

import io.swagger.v3.oas.annotations.media.Schema;
import my.edu.um.umpoint.common.annotation.LogOperation;
import my.edu.um.umpoint.common.constant.Constant;
import my.edu.um.umpoint.common.page.PageData;
import my.edu.um.umpoint.common.utils.Result;
import my.edu.um.umpoint.common.validator.ValidatorUtils;
import my.edu.um.umpoint.common.validator.group.AddGroup;
import my.edu.um.umpoint.common.validator.group.DefaultGroup;
import my.edu.um.umpoint.common.validator.group.UpdateGroup;
import my.edu.um.umpoint.modules.job.dto.ScheduleJobDTO;
import my.edu.um.umpoint.modules.job.service.ScheduleJobService;
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
@RequestMapping("/sys/schedule")
@Tag(name = "Schedule Job")
@AllArgsConstructor
public class ScheduleJobController {
    private final ScheduleJobService scheduleJobService;

    @GetMapping("page")
    @Operation(summary = "page")
    @Parameters({
            @Parameter(name = Constant.PAGE, description = "Current page number, starting from 1", in = ParameterIn.QUERY, required = true, schema = @Schema(type = "integer")) ,
            @Parameter(name = Constant.LIMIT, description = "Number of records per page", in = ParameterIn.QUERY,required = true, schema = @Schema(type = "integer")) ,
            @Parameter(name = Constant.ORDER_FIELD, description = "Sort field", in = ParameterIn.QUERY, schema = @Schema(type = "string")) ,
            @Parameter(name = Constant.ORDER, description = "Sort order, optional values (asc, desc)", in = ParameterIn.QUERY, schema = @Schema(type = "string")) ,
            @Parameter(name = "beanName", description = "beanName", in = ParameterIn.QUERY, schema = @Schema(type = "string"))
    })
    @RequiresPermissions("sys:schedule:page")
    public Result<PageData<ScheduleJobDTO>> page(@Parameter(hidden = true) @RequestParam Map<String, Object> params) {
        PageData<ScheduleJobDTO> page = scheduleJobService.page(params);

        return new Result<PageData<ScheduleJobDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @Operation(summary = "info")
    @RequiresPermissions("sys:schedule:info")
    public Result<ScheduleJobDTO> info(@PathVariable("id") Long id) {
        ScheduleJobDTO schedule = scheduleJobService.get(id);

        return new Result<ScheduleJobDTO>().ok(schedule);
    }

    @PostMapping
    @Operation(summary = "save")
    @LogOperation("save")
    @RequiresPermissions("sys:schedule:save")
    public Result save(@RequestBody ScheduleJobDTO dto) {
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        scheduleJobService.save(dto);

        return new Result();
    }

    @PutMapping
    @Operation(summary = "update")
    @LogOperation("update")
    @RequiresPermissions("sys:schedule:update")
    public Result update(@RequestBody ScheduleJobDTO dto) {
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        scheduleJobService.update(dto);

        return new Result();
    }

    @DeleteMapping
    @Operation(summary = "delete")
    @LogOperation("delete")
    @RequiresPermissions("sys:schedule:delete")
    public Result delete(@RequestBody Long[] ids) {
        scheduleJobService.deleteBatch(ids);

        return new Result();
    }

    @PutMapping("/run")
    @Operation(summary = "run")
    @LogOperation("run")
    @RequiresPermissions("sys:schedule:run")
    public Result run(@RequestBody Long[] ids) {
        scheduleJobService.run(ids);

        return new Result();
    }

    @PutMapping("/pause")
    @Operation(summary = "pause")
    @LogOperation("pause")
    @RequiresPermissions("sys:schedule:pause")
    public Result pause(@RequestBody Long[] ids) {
        scheduleJobService.pause(ids);

        return new Result();
    }

    @PutMapping("/resume")
    @Operation(summary = "resume")
    @LogOperation("resume")
    @RequiresPermissions("sys:schedule:resume")
    public Result resume(@RequestBody Long[] ids) {
        scheduleJobService.resume(ids);

        return new Result();
    }

}
