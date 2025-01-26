package my.edu.um.umpoint.modules.space.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import my.edu.um.umpoint.common.constant.BookingConstant;
import my.edu.um.umpoint.common.constant.Constant;
import my.edu.um.umpoint.common.service.impl.CrudServiceImpl;
import my.edu.um.umpoint.common.utils.DateUtils;
import my.edu.um.umpoint.modules.space.dao.SpcEventDao;
import my.edu.um.umpoint.modules.space.dto.SpcBookingDTO;
import my.edu.um.umpoint.modules.space.dto.SpcClosureDTO;
import my.edu.um.umpoint.modules.space.dto.SpcEventDTO;
import my.edu.um.umpoint.modules.space.entity.SpcEventEntity;
import my.edu.um.umpoint.modules.space.service.SpcClosedSpaceService;
import my.edu.um.umpoint.modules.space.service.SpcEventService;
import my.edu.um.umpoint.modules.utils.SpaceBookingUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

/**
 * Space Occupied Event
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-09-16
 */
@Service
public class SpcEventServiceImpl extends CrudServiceImpl<SpcEventDao, SpcEventEntity, SpcEventDTO> implements SpcEventService{

    @Autowired
    private SpcClosedSpaceService spcClosedSpaceService;

    @Override
    public QueryWrapper<SpcEventEntity> getWrapper(Map<String, Object> params){
        QueryWrapper<SpcEventEntity> wrapper = new QueryWrapper<>();

        if (params.containsKey(Constant.SPACE_ID_LIST)) {
            List<Long> ids = Arrays.stream((Object[]) params.get(Constant.SPACE_ID_LIST))
                                   .map((Object id) -> Long.parseLong(id.toString()))
                                   .toList();

            wrapper.in("space_id", ids);
        } else if (params.get(Constant.SPACE_ID) != null) {
            Long spaceId = Long.parseLong((String) params.get(Constant.SPACE_ID));
            wrapper.eq("space_id", spaceId);
        }

        if (params.get("startTime") != null) {
            Date startTime = DateUtils.parse((String) params.get("startTime"), DateUtils.DATE_TIME_PATTERN);
            wrapper.ge("start_time", startTime);
            wrapper.ge("end_time", startTime);
        }

        if (params.get("endTime") != null) {
            Date endTime = DateUtils.parse((String) params.get("endTime"), DateUtils.DATE_TIME_PATTERN);
            wrapper.le("start_time", endTime);
            wrapper.le("end_time", endTime);
        }

        return wrapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addEvent(SpcBookingDTO bookingDTO, boolean holidayAvailable){
        List<SpcEventEntity> spcEventEntities = SpaceBookingUtils.divideBookingToEvents(bookingDTO, holidayAvailable);
        for (SpcEventEntity eventEntity: spcEventEntities)
            spcClosedSpaceService.addSpace(eventEntity.getStartTime().getTime(), eventEntity.getEndTime().getTime(), bookingDTO.getSpaceId());
        insertBatch(spcEventEntities);
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

                spcClosedSpaceService.addSpace(eventEntity.getStartTime().getTime(), eventEntity.getEndTime().getTime(), closureDTO.getSpaceId());
            }
            currentDay = currentDay.plusDays(1);
        }

        insertBatch(eventEntityList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByClosureId(Long closureId){
        QueryWrapper<SpcEventEntity> wrapper = new QueryWrapper<SpcEventEntity>().eq("closure_id", closureId);
        for (SpcEventEntity eventEntity : baseDao.selectList(wrapper))
            spcClosedSpaceService.removeSpace(eventEntity.getStartTime().getTime(), eventEntity.getEndTime().getTime(), eventEntity.getSpaceId());

        baseDao.delete(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByClosureId(List<Long> closureIdList){
        QueryWrapper<SpcEventEntity> wrapper = new QueryWrapper<SpcEventEntity>().in("closure_id", closureIdList);
        for (SpcEventEntity eventEntity : baseDao.selectList(wrapper))
            spcClosedSpaceService.removeSpace(eventEntity.getStartTime().getTime(), eventEntity.getEndTime().getTime(), eventEntity.getSpaceId());

        baseDao.delete(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByBookingId(Long bookingId){
        QueryWrapper<SpcEventEntity> wrapper = new QueryWrapper<SpcEventEntity>().eq("booking_id", bookingId);
        for (SpcEventEntity eventEntity : baseDao.selectList(wrapper))
            spcClosedSpaceService.removeSpace(eventEntity.getStartTime().getTime(), eventEntity.getEndTime().getTime(), eventEntity.getSpaceId());

        baseDao.delete(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByBookingId(List<Long> bookingIdList){
        QueryWrapper<SpcEventEntity> wrapper = new QueryWrapper<SpcEventEntity>().in("booking_id", bookingIdList);
        for (SpcEventEntity eventEntity : baseDao.selectList(wrapper))
            spcClosedSpaceService.removeSpace(eventEntity.getStartTime().getTime(), eventEntity.getEndTime().getTime(), eventEntity.getSpaceId());

        baseDao.delete(wrapper);
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