package my.edu.um.umpoint.modules.space.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import my.edu.um.umpoint.common.constant.BookingConstant;
import my.edu.um.umpoint.common.service.impl.CrudServiceImpl;
import my.edu.um.umpoint.common.utils.DateUtils;
import my.edu.um.umpoint.modules.space.dao.SpcEventDao;
import my.edu.um.umpoint.modules.space.dto.*;
import my.edu.um.umpoint.modules.space.entity.SpcEventEntity;
import my.edu.um.umpoint.modules.space.service.SpcEventService;
import my.edu.um.umpoint.modules.space.service.SpcSpaceService;
import my.edu.um.umpoint.modules.utils.SpaceBookingUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Space Occupied Event
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-09-16
 */
@Service
public class SpcEventServiceImpl extends CrudServiceImpl<SpcEventDao, SpcEventEntity, SpcEventDTO> implements SpcEventService{

    @Override
    public QueryWrapper<SpcEventEntity> getWrapper(Map<String, Object> params){
        Long spaceId = Long.parseLong((String) params.get("spaceId"));
        Date startTime = null,
            endTime = null;
        if (params.get("startTime") != null) {
            startTime = DateUtils.parse((String) params.get("startTime"), DateUtils.DATE_TIME_PATTERN);
        }
        if (params.get("endTime") != null) {
            endTime = DateUtils.parse((String) params.get("endTime"), DateUtils.DATE_TIME_PATTERN);
        }

        QueryWrapper<SpcEventEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("space_id", spaceId);
        if (startTime != null) {
            wrapper.ge("start_time", startTime);
            wrapper.ge("end_time", startTime);
        }
        if (endTime != null) {
            wrapper.le("start_time", endTime);
            wrapper.le("end_time", endTime);
        }

        return wrapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addEvent(SpcBookingDTO bookingDTO){
        insertBatch(SpaceBookingUtils.divideBookingToEvents(bookingDTO));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addEvent(SpcClosureDTO closureDTO){
        List<SpcEventEntity> eventEntityList = new ArrayList<>();

        LocalDate currentDay = DateUtils.convertDateToLocalDate(closureDTO.getStartDay());
        LocalDate endDay = DateUtils.convertDateToLocalDate(closureDTO.getEndDay());
        while (currentDay.isBefore(endDay) || currentDay.isEqual(endDay)) {
            if (needToAddEvent(closureDTO, currentDay)) {
                SpcEventEntity eventEntity = new SpcEventEntity();

                eventEntity.setSpaceId(closureDTO.getSpaceId());
                eventEntity.setClosureId(closureDTO.getId());
                eventEntity.setType(BookingConstant.EventStatus.CLOSURE.getValue());

                eventEntity.setStartTime(DateUtils.convertLocalDateTimeToDate(currentDay, closureDTO.getStartTime()));
                eventEntity.setEndTime(DateUtils.convertLocalDateTimeToDate(currentDay, closureDTO.getEndTime()));

                eventEntityList.add(eventEntity);
            }
            currentDay = currentDay.plusDays(1);
        }

        insertBatch(eventEntityList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByClosureId(Long closureId){
        baseDao.delete(new QueryWrapper<SpcEventEntity>().eq("closure_id", closureId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByBookingId(Long bookingId){
        baseDao.delete(new QueryWrapper<SpcEventEntity>().eq("booking_id", bookingId));
    }

    private static Boolean needToAddEvent(SpcClosureDTO dto, LocalDate date){
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