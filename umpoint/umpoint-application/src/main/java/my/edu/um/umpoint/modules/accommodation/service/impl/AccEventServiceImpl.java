package my.edu.um.umpoint.modules.accommodation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import my.edu.um.umpoint.common.constant.BookingConstant;
import my.edu.um.umpoint.common.constant.Constant;
import my.edu.um.umpoint.common.service.impl.CrudServiceImpl;
import my.edu.um.umpoint.common.utils.DateUtils;
import my.edu.um.umpoint.modules.accommodation.dao.AccEventDao;
import my.edu.um.umpoint.modules.accommodation.dto.*;
import my.edu.um.umpoint.modules.accommodation.entity.AccEventEntity;
import my.edu.um.umpoint.modules.accommodation.service.AccAccommodationService;
import my.edu.um.umpoint.modules.accommodation.service.AccEventService;
import my.edu.um.umpoint.modules.space.entity.SpcEventEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

/**
 * Accommodation Occupied Event
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-09-16
 */
@Service
public class AccEventServiceImpl extends CrudServiceImpl<AccEventDao, AccEventEntity, AccEventDTO> implements AccEventService {

    @Override
    public QueryWrapper<AccEventEntity> getWrapper(Map<String, Object> params){
        QueryWrapper<AccEventEntity> wrapper = new QueryWrapper<>();

        if (params.containsKey(Constant.ACCOMMODATION_ID_LIST)) {
            Long[] ids = (Long[]) params.get(Constant.ACCOMMODATION_ID_LIST);
            wrapper.in("accommodation_id", Arrays.asList(ids));
        } else if (params.get(Constant.ACCOMMODATION_ID) != null) {
            Long spaceId = Long.parseLong((String) params.get(Constant.ACCOMMODATION_ID));
            wrapper.eq("accommodation_id", spaceId);
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
    public void addEvent(AccBookingDTO bookingDTO) {
        AccEventEntity eventEntity = new AccEventEntity();

        eventEntity.setAccommodationId(bookingDTO.getAccommodationId());
        eventEntity.setBookingId(bookingDTO.getId());
        eventEntity.setType(BookingConstant.EventStatus.BOOKING.getValue());

        eventEntity.setStartTime(bookingDTO.getStartDay());
        eventEntity.setEndTime(bookingDTO.getEndDay());

        insert(eventEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addEvent(AccClosureDTO closureDTO) {
        AccEventEntity eventEntity = new AccEventEntity();

        eventEntity.setAccommodationId(closureDTO.getAccommodationId());
        eventEntity.setClosureId(closureDTO.getId());
        eventEntity.setType(BookingConstant.EventStatus.CLOSURE.getValue());

        eventEntity.setStartTime(closureDTO.getStartDay());
        eventEntity.setEndTime(closureDTO.getEndDay());

        insert(eventEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByBookingId(Long bookingId) {
        baseDao.delete(new QueryWrapper<AccEventEntity>().eq("booking_id", bookingId));
    }

    @Override
    public void deleteByClosureId(Long closureId) {
        baseDao.delete(new QueryWrapper<AccEventEntity>().eq("closure_id", closureId));
    }

    @Override
    public void deleteByClosureId(Long[] closureIds) {
        baseDao.delete(new QueryWrapper<AccEventEntity>().in("closure_id", Arrays.asList(closureIds)));
    }
}