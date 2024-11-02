package my.edu.um.umpoint.modules.accommodation.controller;

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
import my.edu.um.umpoint.common.validator.AssertUtils;
import my.edu.um.umpoint.common.validator.ValidatorUtils;
import my.edu.um.umpoint.common.validator.group.AddGroup;
import my.edu.um.umpoint.common.validator.group.DefaultGroup;
import my.edu.um.umpoint.common.validator.group.UpdateGroup;
import my.edu.um.umpoint.modules.accommodation.dao.AccEventDao;
import my.edu.um.umpoint.modules.accommodation.dto.AccAccommodationDTO;
import my.edu.um.umpoint.modules.accommodation.dto.AccBookingDTO;
import my.edu.um.umpoint.modules.accommodation.dto.AccBookingRuleDTO;
import my.edu.um.umpoint.modules.accommodation.dto.AccClientBookingDTO;
import my.edu.um.umpoint.modules.accommodation.entity.AccEventEntity;
import my.edu.um.umpoint.modules.accommodation.excel.AccBookingExcel;
import my.edu.um.umpoint.modules.accommodation.service.AccAccommodationService;
import my.edu.um.umpoint.modules.accommodation.service.AccBookingService;
import my.edu.um.umpoint.modules.security.user.SecurityUser;
import my.edu.um.umpoint.modules.security.user.UserDetail;
import my.edu.um.umpoint.modules.utils.EventEntity;
import my.edu.um.umpoint.modules.utils.SpaceBookingUtils;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Time;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Accommodation Booking
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-09-08
 */
@RestController
@RequestMapping("accommodation/booking")
@Tag(name = "Accommodation Booking")
public class AccBookingController{
    @Autowired
    private AccBookingService accBookingService;

    @Autowired
    private AccAccommodationService accAccommodationService;

    @Autowired
    private AccEventDao accEventDao;

    @GetMapping("page")
    @Operation(summary = "Pagination")
    @Parameters({
        @Parameter(name = Constant.PAGE, description = "Current page number, starting from 1", in = ParameterIn.QUERY, required = true, ref = "int"),
        @Parameter(name = Constant.LIMIT, description = "Number of records per page", in = ParameterIn.QUERY, required = true, ref = "int"),
        @Parameter(name = Constant.ORDER_FIELD, description = "Sort field", in = ParameterIn.QUERY, ref = "String"),
        @Parameter(name = Constant.ORDER, description = "Sort order, optional values (asc, desc)", in = ParameterIn.QUERY, ref = "String"),
        @Parameter(name = Constant.ID, description = "Booking ID", in = ParameterIn.QUERY),
        @Parameter(name = Constant.STATUS, description = "Booking status", in = ParameterIn.QUERY, ref = "int"),
        @Parameter(name = Constant.ACCOMMODATION, description = "Accommodation name", in = ParameterIn.QUERY, ref = "String"),
        @Parameter(name = Constant.EVENT, description = "Booking purpose description", in = ParameterIn.QUERY, ref = "String")
    })
    @RequiresPermissions("accommodation:booking:page")
    public Result<PageData<AccBookingDTO>> page(@Parameter(hidden = true) @RequestParam Map<String, Object> params){
        PageData<AccBookingDTO> page = accBookingService.page(params);

        return new Result<PageData<AccBookingDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @Operation(summary = "Information")
    @RequiresPermissions("accommodation:booking:info")
    public Result<AccBookingDTO> get(@PathVariable("id") Long id){
        AccBookingDTO data = accBookingService.get(id);

        return new Result<AccBookingDTO>().ok(data);
    }

    @PostMapping
    @Operation(summary = "Save")
    @LogOperation("Save")
    @RequiresPermissions("accommodation:booking:save")
    public Result save(@RequestBody AccClientBookingDTO request){
        ValidatorUtils.validateEntity(request, AddGroup.class, DefaultGroup.class);

        // Check space exist
        AccAccommodationDTO accommodation = accAccommodationService.get(request.getAccommodationId());
        if (accommodation == null) {
            return new Result().error(400, "Space ID does not exist");
        }
        // Validate booking rule
        AccBookingRuleDTO accBookingRule = accommodation.getAccBookingRuleDTO();
        // TODO: Check if account is student, staff or public

        // combine date time to new Java object
        LocalDate startDate = DateUtils.convertDateToLocalDate(request.getStartDay());
        LocalDate endDate = DateUtils.convertDateToLocalDate(request.getEndDay());

        try {
            if (endDate.isBefore(startDate)) {
                throw new DateTimeException("Start date must be earlier than end date");
            }

            validateInAllowedRange(accBookingRule, startDate, endDate);
            validateReservationLength(accBookingRule, startDate, endDate);

            validateEventOverlapped(request);
        } catch (DateTimeException e) {
            return new Result().error(400, e.getMessage());
        }


        AccBookingDTO dto = new AccBookingDTO();
        dto.setAccommodationId(request.getAccommodationId());
        dto.setEvent(request.getEvent());
        dto.setStartDay(request.getStartDay());
        dto.setEndDay(request.getEndDay());
        accBookingService.save(dto);

        return new Result();
    }

    @PutMapping
    @Operation(summary = "Update")
    @LogOperation("Update")
    @RequiresPermissions("accommodation:booking:update")
    public Result update(@RequestBody AccBookingDTO dto){
        UserDetail user = SecurityUser.getUser();
        if (!Objects.equals(user.getId(), accBookingService.getUserId(dto.getId())))
            throw new UnauthorizedException();
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        accBookingService.update(dto);

        return new Result();
    }

    @PutMapping("approve/{id}")
    @Operation(summary = "Approve")
    @LogOperation("Approve")
    @RequiresPermissions("accommodation:booking:approve")
    public Result approve(@PathVariable("id") Long id, @RequestBody List<Long> technicianIdList){
        accBookingService.approve(id, technicianIdList);

        return new Result();
    }

    @PutMapping("reject/{id}")
    @Operation(summary = "Reject")
    @LogOperation("Reject")
    @RequiresPermissions("accommodation:booking:reject")
    public Result reject(@PathVariable("id") Long id){
        accBookingService.reject(id);

        return new Result();
    }

    @PutMapping("cancel/{id}")
    @Operation(summary = "Cancel")
    @LogOperation("Cancel")
    @RequiresPermissions("accommodation:booking:cancel")
    public Result cancel(@PathVariable("id") Long id){
        UserDetail user = SecurityUser.getUser();
        if (!Objects.equals(user.getId(), accBookingService.getUserId(id)))
            throw new UnauthorizedException();


        accBookingService.cancel(id);

        return new Result();
    }

    @DeleteMapping
    @Operation(summary = "Delete")
    @LogOperation("Delete")
    @RequiresPermissions("accommodation:booking:delete")
    public Result delete(@RequestBody Long[] ids){
        AssertUtils.isArrayEmpty(ids, "id");

        accBookingService.delete(ids);

        return new Result();
    }

    @GetMapping("export")
    @Operation(summary = "Export")
    @LogOperation("Export")
    @RequiresPermissions("accommodation:booking:export")
    public void export(
        @Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletResponse response
    ) throws Exception{
        List<AccBookingDTO> list = accBookingService.list(params);

        ExcelUtils.exportExcelToTarget(response, null, "Accommodation Booking", list, AccBookingExcel.class);
    }

    private void validateEventOverlapped(AccClientBookingDTO request){
        DateTimeFormatter sqlDateDormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        //Todo: No need to validate by event, just query all overlap event and check is exist
        //Example: SELECT * FROM acc_event WHERE (startTime >= startDay AND startTime <= endDay) OR (endTime >= startDay AND endTime <= endDay)
        for (EventEntity dividedEvent : SpaceBookingUtils.dividePeriodToEvents(
                request.getStartDay(), request.getEndDay(), Time.valueOf(LocalTime.MIN), Time.valueOf(LocalTime.MAX)
        )) {
            List<AccEventEntity> overlappedEvents = accEventDao.getEventsBetweenTimeSpan(
                    request.getAccommodationId(),
                    DateUtils.convertDateToLocalDateTime(dividedEvent.startTime).format(sqlDateDormatter),
                    DateUtils.convertDateToLocalDateTime(dividedEvent.endTime).format(sqlDateDormatter)
            );
            if (!overlappedEvents.isEmpty()) {
                throw new DateTimeException("Booking overlapped");
            }
        }
    }

    private static void validateReservationLength(
            AccBookingRuleDTO spcBookingRule,
            LocalDate startDate,
            LocalDate endDate
    ) throws DateTimeException{
        long differenceInDays = ChronoUnit.DAYS.between(startDate, endDate);
        if (differenceInDays > spcBookingRule.getMaxReservationDay()) {
            throw new DateTimeException("Selected date range is over the maximum number of reservation days");
        }

        if (differenceInDays < spcBookingRule.getMinReservationDay()) {
            throw new DateTimeException("Selected time range does not reach minimum number of hours");
        }
    }

    private static void validateInAllowedRange(
            AccBookingRuleDTO accBookingRule, LocalDate startDate, LocalDate endDate
    ) throws DateTimeException{
        // Date time check
        LocalDate allowedRangeStartDate =
                LocalDate.now()
                        .atStartOfDay(ZoneId.systemDefault())
                        .plusDays(accBookingRule.getMinBookingAdvanceDay())
                        .toLocalDate();
        LocalDate allowedRangeEndDate =
                LocalDate.now()
                        .atStartOfDay(ZoneId.systemDefault())
                        .plusDays(accBookingRule.getMaxBookingAdvanceDay())
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
}
