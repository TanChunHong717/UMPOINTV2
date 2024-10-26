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
import my.edu.um.umpoint.modules.security.user.SecurityUser;
import my.edu.um.umpoint.modules.security.user.UserDetail;
import my.edu.um.umpoint.modules.space.dao.SpcEventDao;
import my.edu.um.umpoint.modules.space.dto.SpcBookingDTO;
import my.edu.um.umpoint.modules.space.dto.SpcBookingRuleDTO;
import my.edu.um.umpoint.modules.space.dto.SpcClientBookingDTO;
import my.edu.um.umpoint.modules.space.dto.SpcSpaceDTO;
import my.edu.um.umpoint.modules.space.entity.SpcEventEntity;
import my.edu.um.umpoint.modules.space.excel.SpcBookingExcel;
import my.edu.um.umpoint.modules.space.service.SpcBookingService;
import my.edu.um.umpoint.modules.space.service.SpcSpaceService;
import my.edu.um.umpoint.modules.utils.SpaceBookingUtils;
import my.edu.um.umpoint.modules.utils.EventEntity;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Space Booking
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-09-08
 */
@RestController
@RequestMapping("space/booking")
@Tag(name = "Space Booking")
public class SpcBookingController{
    @Autowired
    private SpcBookingService spcBookingService;

    @Autowired
    private SpcSpaceService spcSpaceService;

    @Autowired
    private SpcEventDao spcEventDao;

    @GetMapping("page")
    @Operation(summary = "Pagination")
    @Parameters({
        @Parameter(name = Constant.PAGE, description = "Current page number, starting from 1", in = ParameterIn.QUERY, required = true, ref = "int"),
        @Parameter(name = Constant.LIMIT, description = "Number of records per page", in = ParameterIn.QUERY, required = true, ref = "int"),
        @Parameter(name = Constant.ORDER_FIELD, description = "Sort field", in = ParameterIn.QUERY, ref = "String"),
        @Parameter(name = Constant.ORDER, description = "Sort order, optional values (asc, desc)", in = ParameterIn.QUERY, ref = "String"),
        @Parameter(name = Constant.ID, description = "Booking ID", in = ParameterIn.QUERY),
        @Parameter(name = Constant.STATUS, description = "Booking status", in = ParameterIn.QUERY, ref = "int"),
        @Parameter(name = Constant.SPACE, description = "Space name", in = ParameterIn.QUERY, ref = "String"),
        @Parameter(name = Constant.EVENT, description = "Booking purpose description", in = ParameterIn.QUERY, ref = "String")
    })
    @RequiresPermissions("space:booking:page")
    public Result<PageData<SpcBookingDTO>> page(@Parameter(hidden = true) @RequestParam Map<String, Object> params){
        PageData<SpcBookingDTO> page = spcBookingService.page(params);

        return new Result<PageData<SpcBookingDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @Operation(summary = "Information")
    @RequiresPermissions("space:booking:info")
    public Result<SpcBookingDTO> get(@PathVariable("id") Long id){
        SpcBookingDTO data = spcBookingService.get(id);

        return new Result<SpcBookingDTO>().ok(data);
    }

    @PostMapping
    @Operation(summary = "Save")
    @LogOperation("Save")
    @RequiresPermissions("space:booking:save")
    public Result save(@RequestBody SpcClientBookingDTO request){
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
        LocalTime startTime = request.getStartTime().toLocalTime();
        LocalTime endTime = request.getEndTime().toLocalTime();

        try {
            if (endDate.isBefore(startDate)) {
                throw new DateTimeException("Start date must be earlier than end date");
            }
            if (endTime.isBefore(startTime)) {
                throw new DateTimeException("Start time must be earlier than end time");
            }

            validateInAllowedRange(spcBookingRule, startDate, endDate);
            validateReservationLength(spcBookingRule, startDate, endDate, startTime, endTime);

            validateEventOverlapped(request);
        } catch (DateTimeException e) {
            return new Result().error(400, e.getMessage());
        }

        // prepare to save
        SpcBookingDTO bookingDto = makeSpcBookingDTO(request, space);

        spcBookingService.save(bookingDto);

        return new Result();
    }

    private void validateEventOverlapped(SpcClientBookingDTO request){
        DateTimeFormatter sqlDateDormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for (EventEntity dividedEvent: SpaceBookingUtils.dividePeriodToEvents(
            request.getStartDay(), request.getEndDay(), request.getStartTime(), request.getEndTime()
        )) {
            List<SpcEventEntity> overlappedEvents = spcEventDao.getEventsBetweenTimeSpan(
                request.getSpaceId(),
                DateUtils.convertDateToLocalDateTime(dividedEvent.startTime).format(sqlDateDormatter),
                DateUtils.convertDateToLocalDateTime(dividedEvent.endTime).format(sqlDateDormatter)
            );
            if (!overlappedEvents.isEmpty()) {
                throw new DateTimeException("Booking overlapped");
            }
        }
    }

    private static void validateReservationLength(
        SpcBookingRuleDTO spcBookingRule,
        LocalDate startDate,
        LocalDate endDate,
        LocalTime startTime,
        LocalTime endTime
    ) throws DateTimeException{
        long differenceInDays = ChronoUnit.DAYS.between(startDate, endDate);
        if (differenceInDays > spcBookingRule.getMaxReservationDay()) {
            throw new DateTimeException("Selected date range is over the maximum number of reservation days");
        }

        double differenceInHours = ChronoUnit.MINUTES.between(startTime, endTime) / 60.0; // may have half hour limit
        if (differenceInHours < spcBookingRule.getMinBookingHour().doubleValue()){
            throw new DateTimeException("Selected time range does not reach minimum number of hours");
        }
    }

    private static void validateInAllowedRange(
        SpcBookingRuleDTO spcBookingRule, LocalDate startDate, LocalDate endDate
    ) throws DateTimeException{
        // Date time check
        LocalDate allowedRangeStartDate =
            LocalDate.now()
                     .atStartOfDay(ZoneId.systemDefault())
                     .plusDays(spcBookingRule.getMinBookingAdvanceDay())
                     .toLocalDate();
        LocalDate allowedRangeEndDate =
            LocalDate.now()
                     .atTime(LocalTime.MAX)
                     .plusDays(spcBookingRule.getMaxBookingAdvanceDay())
                     .toLocalDate();
        if (
            startDate.isBefore(allowedRangeStartDate) ||
            startDate.isAfter(allowedRangeEndDate)
        ) {
            throw new DateTimeException("Invalid start date");
        } else if (
            endDate.isBefore(allowedRangeStartDate) ||
            endDate.isAfter(allowedRangeEndDate)
        ) {
            throw new DateTimeException("Invalid end date");
        }
    }

    private static SpcBookingDTO makeSpcBookingDTO(SpcClientBookingDTO request, SpcSpaceDTO space){
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
        return bookingDto;
    }

    @PutMapping
    @Operation(summary = "Update")
    @LogOperation("Update")
    @RequiresPermissions("space:booking:update")
    public Result update(@RequestBody SpcBookingDTO dto){
        UserDetail user = SecurityUser.getUser();
        if (!Objects.equals(user.getId(), spcBookingService.getUserId(dto.getId())))
            throw new UnauthorizedException();

        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        spcBookingService.update(dto);

        return new Result();
    }

    @PutMapping("approve/{id}")
    @Operation(summary = "Approve")
    @LogOperation("Approve")
    @RequiresPermissions("space:booking:approve")
    public Result approve(@PathVariable("id") Long id, @RequestBody List<Long> technicianIdList){
        spcBookingService.approve(id, technicianIdList);

        return new Result();
    }

    @PutMapping("reject/{id}")
    @Operation(summary = "Reject")
    @LogOperation("Reject")
    @RequiresPermissions("space:booking:reject")
    public Result reject(@PathVariable("id") Long id){
        spcBookingService.reject(id);

        return new Result();
    }

    @PutMapping("cancel/{id}")
    @Operation(summary = "Cancel")
    @LogOperation("Cancel")
    @RequiresPermissions("space:booking:cancel")
    public Result cancel(@PathVariable("id") Long id){
        UserDetail user = SecurityUser.getUser();
        if (!Objects.equals(user.getId(), spcBookingService.getUserId(id)))
            throw new UnauthorizedException();

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
    ) throws Exception{
        List<SpcBookingDTO> list = spcBookingService.list(params);

        ExcelUtils.exportExcelToTarget(response, null, "Space Booking", list, SpcBookingExcel.class);
    }

}
