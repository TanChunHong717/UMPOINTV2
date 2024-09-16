package my.edu.um.umpoint.modules.space.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import my.edu.um.umpoint.common.constant.BookingConstant;
import my.edu.um.umpoint.common.service.impl.CrudServiceImpl;
import my.edu.um.umpoint.common.utils.DateUtils;
import my.edu.um.umpoint.modules.space.dao.SpcEventDao;
import my.edu.um.umpoint.modules.space.dto.*;
import my.edu.um.umpoint.modules.space.entity.SpcEventEntity;
import my.edu.um.umpoint.modules.space.service.SpcEventService;
import my.edu.um.umpoint.modules.space.service.SpcSpaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Space Occupied Event
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-09-16
 */
@Service
public class SpcEventServiceImpl extends CrudServiceImpl<SpcEventDao, SpcEventEntity, SpcEventDTO> implements SpcEventService {

    @Autowired
    private SpcSpaceService spcSpaceService;

    @Override
    public QueryWrapper<SpcEventEntity> getWrapper(Map<String, Object> params){
        String spaceId = (String)params.get("spaceId");
        Object startTime = params.get("startTime");
        Object endTime = params.get("endTime");

        QueryWrapper<SpcEventEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(spaceId), "space_id", spaceId);
        wrapper.between("start_time", startTime, endTime);
        wrapper.between("end_time", startTime, endTime);

        return wrapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addEvent(SpcBookingDTO bookingDTO) {
        List<SpcEventEntity> eventEntityList = new ArrayList<>();

        LocalDate startDay = DateUtils.convertDateToLocalDate(bookingDTO.getStartDay());
        LocalDate endDay = DateUtils.convertDateToLocalDate(bookingDTO.getEndDay());
        while (startDay.isBefore(endDay) || startDay.isEqual(endDay)) {
            SpcEventEntity eventEntity = new SpcEventEntity();

            eventEntity.setSpaceId(bookingDTO.getSpaceId());
            eventEntity.setBookingId(bookingDTO.getId());
            eventEntity.setType(BookingConstant.EventStatus.BOOKING.getValue());

            eventEntity.setStartTime(DateUtils.convertLocalDateTimeToDate(startDay, bookingDTO.getStartTime()));
            eventEntity.setEndTime(DateUtils.convertLocalDateTimeToDate(endDay, bookingDTO.getEndTime()));

            eventEntityList.add(eventEntity);
            startDay = startDay.plusDays(1);
        }

        SpcSpaceDTO spaceDTO = spcSpaceService.get(bookingDTO.getSpaceId());
        SpcBookingRuleDTO bookingRuleDTO = spaceDTO.getSpcBookingRuleDTO();
        if (bookingRuleDTO.getCloseDaysAfterBooking() > 0) {
            LocalDateTime startTime = LocalDateTime.of(endDay.plusDays(1), LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(
                    endDay.plusDays(1 + bookingRuleDTO.getCloseDaysAfterBooking()),
                    LocalTime.MAX
            );
            
            SpcEventEntity eventEntity = new SpcEventEntity();

            eventEntity.setSpaceId(bookingDTO.getSpaceId());
            eventEntity.setBookingId(bookingDTO.getId());
            eventEntity.setType(BookingConstant.EventStatus.CLOSE_AFTER_BOOKING.getValue());
            
            eventEntity.setStartTime(DateUtils.convertLocalDateTimeToDate(startTime));
            eventEntity.setEndTime(DateUtils.convertLocalDateTimeToDate(endTime));

            eventEntityList.add(eventEntity);
        }

        insertBatch(eventEntityList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addEvent(SpcClosureDTO closureDTO) {
        List<SpcEventEntity> eventEntityList = new ArrayList<>();

        LocalDate startDay = DateUtils.convertDateToLocalDate(closureDTO.getStartDay());
        LocalDate endDay = DateUtils.convertDateToLocalDate(closureDTO.getEndDay());
        while (startDay.isBefore(endDay) || startDay.isEqual(endDay)) {
            if (needToAddEvent(closureDTO, startDay)) {
                SpcEventEntity eventEntity = new SpcEventEntity();

                eventEntity.setSpaceId(closureDTO.getSpaceId());
                eventEntity.setType(BookingConstant.EventStatus.CLOSURE.getValue());

                eventEntity.setStartTime(DateUtils.convertLocalDateTimeToDate(startDay, closureDTO.getStartTime()));
                eventEntity.setEndTime(DateUtils.convertLocalDateTimeToDate(endDay, closureDTO.getEndTime()));

                eventEntityList.add(eventEntity);
            }
            startDay = startDay.plusDays(1);
        }

        insertBatch(eventEntityList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByClosureId(Long closureId) {
        baseDao.delete(new QueryWrapper<SpcEventEntity>().eq("closure_id", closureId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByBookingId(Long bookingId) {
        baseDao.delete(new QueryWrapper<SpcEventEntity>().eq("booking_id", bookingId));
    }

    private static Boolean needToAddEvent(SpcClosureDTO dto, LocalDate date) {
        boolean needToAddEvent = false;
        switch (date.getDayOfWeek()) {
            case MONDAY -> needToAddEvent = dto.getRecurMonday() == 1;
            case TUESDAY -> needToAddEvent = dto.getRecurTuesday() == 1;
            case WEDNESDAY -> needToAddEvent = dto.getRecurWednesday() == 1;
            case THURSDAY -> needToAddEvent = dto.getRecurThursday() == 1;
            case FRIDAY -> needToAddEvent = dto.getRecurFriday() == 1;
            case SATURDAY -> needToAddEvent = dto.getRecurSaturday() == 1;
            case SUNDAY -> needToAddEvent = dto.getRecurSunday() == 1;
        }
        return needToAddEvent;
    }
}