package my.edu.um.umpoint.modules.space.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import my.edu.um.umpoint.common.annotation.LogOperation;
import my.edu.um.umpoint.common.constant.Constant;
import my.edu.um.umpoint.common.page.PageData;
import my.edu.um.umpoint.common.utils.DateUtils;
import my.edu.um.umpoint.common.utils.ExcelUtils;
import my.edu.um.umpoint.common.utils.Result;
import my.edu.um.umpoint.common.validator.ValidatorUtils;
import my.edu.um.umpoint.common.validator.group.AddGroup;
import my.edu.um.umpoint.common.validator.group.DefaultGroup;
import my.edu.um.umpoint.common.validator.group.UpdateGroup;
import my.edu.um.umpoint.modules.space.dao.SpcEventDao;
import my.edu.um.umpoint.modules.space.dao.SpcSpaceDao;
import my.edu.um.umpoint.modules.space.dto.SpcBookingDTO;
import my.edu.um.umpoint.modules.space.dto.SpcBookingRuleDTO;
import my.edu.um.umpoint.modules.space.dto.SpcClientBookingDTO;
import my.edu.um.umpoint.modules.space.dto.SpcSpaceDTO;
import my.edu.um.umpoint.modules.space.entity.SpcBookingRuleEntity;
import my.edu.um.umpoint.modules.space.entity.SpcEventEntity;
import my.edu.um.umpoint.modules.space.entity.SpcSpaceEntity;
import my.edu.um.umpoint.modules.space.excel.SpcBookingExcel;
import my.edu.um.umpoint.modules.space.service.SpcBookingService;
import my.edu.um.umpoint.modules.space.service.SpcSpaceService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Space Booking
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-09-08
 */
@RestController
@RequestMapping("space/booking")
@Tag(name = "Space Booking")
public class SpcBookingController {
    @Autowired
    private SpcBookingService spcBookingService;

    @Autowired
    private SpcSpaceService spcSpaceService;

    @Autowired
    private SpcEventDao spcEventDao;

    @GetMapping("page")
    @Operation(summary = "Pagination")
    @Parameters(
        {
            @Parameter(
                name = Constant.PAGE, description = "Current page number, starting from 1", in = ParameterIn.QUERY,
                required = true, ref = "int"
            ),
            @Parameter(
                name = Constant.LIMIT, description = "Number of records per page", in = ParameterIn.QUERY,
                required = true, ref = "int"
            ),
            @Parameter(name = Constant.ORDER_FIELD, description = "Sort field", in = ParameterIn.QUERY, ref = "String"),
            @Parameter(
                name = Constant.ORDER, description = "Sort order, optional values (asc, desc)", in = ParameterIn.QUERY,
                ref = "String"
            ),
            @Parameter(name = Constant.ID, description = "Booking ID", in = ParameterIn.QUERY),
            @Parameter(name = Constant.STATUS, description = "Booking status", in = ParameterIn.QUERY, ref = "int"),
            @Parameter(
                name = Constant.EVENT, description = "Booking purpose description", in = ParameterIn.QUERY,
                ref = "String"
            )
        }
    )
    @RequiresPermissions("space:booking:page")
    public Result<PageData<SpcBookingDTO>> page(@Parameter(hidden = true) @RequestParam Map<String, Object> params) {
        PageData<SpcBookingDTO> page = spcBookingService.page(params);

        return new Result<PageData<SpcBookingDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @Operation(summary = "Information")
    @RequiresPermissions("space:booking:info")
    public Result<SpcBookingDTO> get(@PathVariable("id") Long id) {
        SpcBookingDTO data = spcBookingService.get(id);

        return new Result<SpcBookingDTO>().ok(data);
    }

    @PostMapping
    @Operation(summary = "Save")
    @LogOperation("Save")
    @RequiresPermissions("space:booking:save")
    public Result save(@RequestBody SpcClientBookingDTO request) {
        ValidatorUtils.validateEntity(request, AddGroup.class, DefaultGroup.class);

        // Check space exist
        SpcSpaceDTO space = spcSpaceService.get(request.getSpaceId());
        if (space == null) {
            return new Result().error(400, "Space ID does not exist");
        }
        // Validate booking rule
        SpcBookingRuleDTO spcBookingRule = space.getSpcBookingRuleDTO();
        // TODO: Check if account is student, staff or public

        // combine date time to new Java object
        LocalDate startDate = DateUtils.convertDateToLocalDate(request.getStartDay());
        LocalDate endDate = DateUtils.convertDateToLocalDate(request.getEndDay());
        LocalDateTime startDateTime =
            DateUtils.convertDateTimeToLocalDateTime(request.getStartDay(), request.getStartTime());
        LocalDateTime endDateTime = DateUtils.convertDateTimeToLocalDateTime(request.getEndDay(), request.getEndTime());

        // Date time check
        LocalDate allowedRangeStartDate =
            LocalDate.now()
                     .atStartOfDay(ZoneId.systemDefault())
                     .plusDays(spcBookingRule.getMaxBookingAdvanceDay())
                     .toLocalDate();
        LocalDate allowedRangeEndDate =
            LocalDate.now()
                     .atStartOfDay(ZoneId.systemDefault())
                     .plusDays(spcBookingRule.getMinBookingAdvanceDay())
                     .toLocalDate();
        if (endDateTime.isBefore(startDateTime)) {
            return new Result().error(400, "Start date must be earlier than end date");
        }

        if (
            startDate.isBefore(allowedRangeStartDate) ||
            startDate.isAfter(allowedRangeEndDate)
        ) {
            return new Result().error(400, "Invalid start date");
        } else if (
            endDate.isBefore(allowedRangeStartDate) ||
            endDate.isAfter(allowedRangeEndDate)
        ) {
            return new Result().error(400, "Invalid end date");
        }

        long differenceInDays = ChronoUnit.DAYS.between(startDate, endDate);
        if (
            differenceInDays > spcBookingRule.getMaxReservationDays()
        ) {
            return new Result().error(400, "Selected date range is over the maximum number of reservation days");
        }

        // Validate event overlap
        DateTimeFormatter sqlDateDormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        List<SpcEventEntity> overlappedEvents = spcEventDao.getEventsBetweenTimeSpan(
            startDateTime.format(sqlDateDormatter),
            endDateTime.format(sqlDateDormatter)
        );
        if (!overlappedEvents.isEmpty()) {
            return new Result().error(400, "Booking overlapped");
        }

        // prepare to save
        SpcBookingDTO bookingDto = new SpcBookingDTO();
        bookingDto.setSpaceId(space.getId());
        bookingDto.setEvent(request.getEvent());
        bookingDto.setStartDay(request.getStartDay());
        bookingDto.setEndDay(request.getEndDay());
        bookingDto.setStartTime(request.getStartTime());
        bookingDto.setEndTime(request.getEndTime());
        if (request.getTechnicianNumber() != null && request.getTechnicianNumber().isPresent()) {
            bookingDto.setTechnicianNumber(request.getTechnicianNumber().get());
        } else {
            bookingDto.setTechnicianNumber(0);
        }

        spcBookingService.save(bookingDto);

        return new Result<SpcBookingDTO>().ok(bookingDto);
    }

    @PutMapping
    @Operation(summary = "Update")
    @LogOperation("Update")
    @RequiresPermissions("space:booking:update")
    public Result update(@RequestBody SpcBookingDTO dto) {
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        spcBookingService.update(dto);

        return new Result();
    }

    @PutMapping("approve/{id}")
    @Operation(summary = "Approve")
    @LogOperation("Approve")
    @RequiresPermissions("space:booking:approve")
    public Result approve(@PathVariable("id") Long id, @RequestBody List<Long> technicianIdList) {
        spcBookingService.approve(id, technicianIdList);

        return new Result();
    }

    @PutMapping("reject/{id}")
    @Operation(summary = "Reject")
    @LogOperation("Reject")
    @RequiresPermissions("space:booking:reject")
    public Result reject(@PathVariable("id") Long id) {
        spcBookingService.reject(id);

        return new Result();
    }

    @PutMapping("cancel/{id}")
    @Operation(summary = "Cancel")
    @LogOperation("Cancel")
    @RequiresPermissions("space:booking:cancel")
    public Result cancel(@PathVariable("id") Long id) {
        spcBookingService.cancel(id);

        return new Result();
    }

    @GetMapping("export")
    @Operation(summary = "Export")
    @LogOperation("Export")
    @RequiresPermissions("space:booking:export")
    public void export(
        @Parameter(hidden = true) @RequestParam
        Map<String, Object> params, HttpServletResponse response
    ) throws Exception {
        List<SpcBookingDTO> list = spcBookingService.list(params);

        ExcelUtils.exportExcelToTarget(response, null, "Space Booking", list, SpcBookingExcel.class);
    }

}
