package my.edu.um.umpoint.modules.accommodation.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import my.edu.um.umpoint.common.constant.BookingConstant;
import my.edu.um.umpoint.common.service.impl.CrudServiceImpl;
import my.edu.um.umpoint.common.utils.DateUtils;
import my.edu.um.umpoint.modules.accommodation.dao.AccEventDao;
import my.edu.um.umpoint.modules.accommodation.dto.*;
import my.edu.um.umpoint.modules.accommodation.entity.AccEventEntity;
import my.edu.um.umpoint.modules.accommodation.service.AccAccommodationService;
import my.edu.um.umpoint.modules.accommodation.service.AccEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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

    @Autowired
    private AccAccommodationService accAccommodationService;

    @Override
    public QueryWrapper<AccEventEntity> getWrapper(Map<String, Object> params){
        Long accommodationId = Long.parseLong((String)params.get("accommodationId"));
        Date startTime =  DateUtils.parse((String) params.get("startTime"), DateUtils.DATE_PATTERN);
        Date endTime = DateUtils.parse((String) params.get("endTime"), DateUtils.DATE_PATTERN);

        QueryWrapper<AccEventEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("accommodation_id", accommodationId);
        wrapper.between("start_time", startTime, endTime);
        wrapper.between("end_time", startTime, endTime);

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

        AccAccommodationDTO accommodationDTO = accAccommodationService.get(bookingDTO.getAccommodationId());
        AccBookingRuleDTO bookingRuleDTO = accommodationDTO.getAccBookingRuleDTO();
        if (bookingRuleDTO.getCloseDaysAfterBooking() > 0) {
            LocalDate startDate = DateUtils.convertDateToLocalDate(bookingDTO.getStartDay()).plusDays(1);
            LocalDate endDate = DateUtils.convertDateToLocalDate(bookingDTO.getEndDay());
            endDate = endDate.plusDays(1 + bookingRuleDTO.getCloseDaysAfterBooking());

            AccEventEntity closeEventEntity = new AccEventEntity();

            closeEventEntity.setAccommodationId(bookingDTO.getAccommodationId());
            closeEventEntity.setBookingId(bookingDTO.getId());
            closeEventEntity.setType(BookingConstant.EventStatus.CLOSE_AFTER_BOOKING.getValue());

            closeEventEntity.setStartTime(DateUtils.convertLocalDateToDate(startDate));
            closeEventEntity.setEndTime(DateUtils.convertLocalDateToDate(endDate));

            insert(closeEventEntity);
        }
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
    public void deleteByClosureId(Long closureId) {
        baseDao.delete(new QueryWrapper<AccEventEntity>().eq("closure_id", closureId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByBookingId(Long bookingId) {
        baseDao.delete(new QueryWrapper<AccEventEntity>().eq("booking_id", bookingId));
    }
}