package my.edu.um.umpoint.modules.space.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import my.edu.um.umpoint.common.annotation.LogOperation;
import my.edu.um.umpoint.common.config.KafkaConfig;
import my.edu.um.umpoint.common.constant.Constant;
import my.edu.um.umpoint.common.exception.RenException;
import my.edu.um.umpoint.common.page.PageData;
import my.edu.um.umpoint.common.utils.DateUtils;
import my.edu.um.umpoint.common.utils.JsonUtils;
import my.edu.um.umpoint.common.utils.Result;
import my.edu.um.umpoint.common.validator.AssertUtils;
import my.edu.um.umpoint.common.validator.ValidatorUtils;
import my.edu.um.umpoint.common.validator.group.AddGroup;
import my.edu.um.umpoint.common.validator.group.BatchUpdateGroup;
import my.edu.um.umpoint.common.validator.group.DefaultGroup;
import my.edu.um.umpoint.common.validator.group.UpdateGroup;
import my.edu.um.umpoint.modules.security.user.SecurityUser;
import my.edu.um.umpoint.modules.space.dto.SpcClosureDTO;
import my.edu.um.umpoint.modules.space.dto.SpcClosureKafkaMessageDTO;
import my.edu.um.umpoint.modules.space.service.SpcClosureService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

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

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @GetMapping("page")
    @Operation(summary = "Pagination")
    @Parameters({
            @Parameter(name = Constant.PAGE, description = "Current page number, starting from 1", in = ParameterIn.QUERY, required = true, schema = @Schema(type = "integer")) ,
            @Parameter(name = Constant.LIMIT, description = "Number of records per page", in = ParameterIn.QUERY,required = true, schema = @Schema(type = "integer")) ,
            @Parameter(name = Constant.ORDER_FIELD, description = "Sort field", in = ParameterIn.QUERY, schema = @Schema(type = "string")) ,
            @Parameter(name = Constant.ORDER, description = "Sort order, optional values (asc, desc)", in = ParameterIn.QUERY, schema = @Schema(type = "string")) ,
            @Parameter(name = Constant.NAME, description = "Name", in = ParameterIn.QUERY, schema = @Schema(type = "string")) ,
            @Parameter(name = "showPast", description = "Show past closure event, optional", in = ParameterIn.QUERY, schema = @Schema(type = "integer"))
    })
    @RequiresPermissions("space:closure:page")
    public Result<PageData<SpcClosureDTO>> page(@Parameter(hidden = true) @RequestParam Map<String, Object> params){
        PageData<SpcClosureDTO> page = spcClosureService.page(params);

        return new Result<PageData<SpcClosureDTO>>().ok(page);
    }

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

    @PostMapping("batch")
    @Operation(summary = "Batch Save")
    @LogOperation("Save")
    @RequiresPermissions("space:closure:save")
    public Result batchSave(@RequestParam Long[] spaceIdList, @RequestBody SpcClosureDTO dto){
        AssertUtils.isArrayEmpty(spaceIdList);
        ValidatorUtils.validateEntity(dto, BatchUpdateGroup.class, DefaultGroup.class);
        validateEventTime(dto);

        String batchKey = UUID.randomUUID().toString();
        Set<Long> set = new HashSet<>();
        for (int i = 0; i < spaceIdList.length; i++) {
            if (set.contains(spaceIdList[i]))
                continue;
            set.add(spaceIdList[i]);
            dto.setSpaceId(spaceIdList[i]);

            SpcClosureKafkaMessageDTO messageDTO = new SpcClosureKafkaMessageDTO();
            messageDTO.setSpcClosureDTO(dto);
            messageDTO.setUserDetail(SecurityUser.getUser());
            messageDTO.setIsLast(i == spaceIdList.length - 1);

            kafkaTemplate.send(KafkaConfig.BATCH_ADD_CLOSURE_TOPIC, batchKey, JsonUtils.toJsonString(messageDTO));
        }

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

    @DeleteMapping
    @Operation(summary = "Delete")
    @LogOperation("Delete")
    @RequiresPermissions("space:closure:delete")
    public Result delete(@RequestBody Long[] ids){
        AssertUtils.isArrayEmpty(ids);

        spcClosureService.delete(ids);

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
