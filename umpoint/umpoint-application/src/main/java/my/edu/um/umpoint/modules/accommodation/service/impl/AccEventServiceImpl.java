package my.edu.um.umpoint.modules.accommodation.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import my.edu.um.umpoint.common.constant.BookingConstant;
import my.edu.um.umpoint.common.service.impl.CrudServiceImpl;
import my.edu.um.umpoint.common.utils.DateUtils;
import my.edu.um.umpoint.modules.accommodation.availability.AccommodationAvailability;
import my.edu.um.umpoint.modules.accommodation.dao.AccEventDao;
import my.edu.um.umpoint.modules.accommodation.dto.*;
import my.edu.um.umpoint.modules.accommodation.entity.AccAvailabilityEntity;
import my.edu.um.umpoint.modules.accommodation.entity.AccEventEntity;
import my.edu.um.umpoint.modules.accommodation.service.AccAccommodationService;
import my.edu.um.umpoint.modules.accommodation.service.AccAvailabilityService;
import my.edu.um.umpoint.modules.accommodation.service.AccEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
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

    @Autowired
    private AccAvailabilityService accAvailabilityService;

    @Override
    public QueryWrapper<AccEventEntity> getWrapper(Map<String, Object> params){
        String accommodationId = (String)params.get("accommodationId");
        Object startTime = params.get("startTime");
        Object endTime = params.get("endTime");

        QueryWrapper<AccEventEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(accommodationId), "space_id", accommodationId);
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

        List<AccAvailabilityEntity> availabilityEntityList = accAvailabilityService.getByAccommodationId(bookingDTO.getAccommodationId());
        AccommodationAvailability accommodationAvailability = new AccommodationAvailability(availabilityEntityList);
        accommodationAvailability.markUnavailable(bookingDTO.getStartDay(), bookingDTO.getEndDay());

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

            accommodationAvailability.markUnavailable(startDate, endDate);

            insert(closeEventEntity);
        }

        accAvailabilityService.update(bookingDTO.getAccommodationId(), accommodationAvailability);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addEvent(AccClosureDTO closureDTO) {
        AccEventEntity eventEntity = new AccEventEntity();

        eventEntity.setAccommodationId(closureDTO.getAccommodationId());
        eventEntity.setType(BookingConstant.EventStatus.CLOSURE.getValue());

        eventEntity.setStartTime(closureDTO.getStartDay());
        eventEntity.setEndTime(closureDTO.getEndDay());

        insert(eventEntity);

        List<AccAvailabilityEntity> availabilityEntityList = accAvailabilityService.getByAccommodationId(closureDTO.getAccommodationId());
        AccommodationAvailability accommodationAvailability = new AccommodationAvailability(availabilityEntityList);
        accommodationAvailability.markUnavailable(closureDTO.getStartDay(), closureDTO.getEndDay());
        accAvailabilityService.update(closureDTO.getAccommodationId(), accommodationAvailability);
    }

    @Override
    public void deleteByClosureId(Long closureId) {
        List<AccEventEntity> eventEntityList = baseDao.selectList(new QueryWrapper<AccEventEntity>().eq("closure_id", closureId));
        if (eventEntityList != null && !eventEntityList.isEmpty()) {
            Long accommodationId = eventEntityList.get(0).getAccommodationId();
            List<AccAvailabilityEntity> availabilityEntityList = accAvailabilityService.getByAccommodationId(accommodationId);
            AccommodationAvailability accommodationAvailability = new AccommodationAvailability(availabilityEntityList);

            eventEntityList.forEach(accEventEntity ->
                accommodationAvailability.markAvailable(accEventEntity.getStartTime(), accEventEntity.getEndTime())
            );
            accAvailabilityService.update(accommodationId, accommodationAvailability);
        }

        baseDao.delete(new QueryWrapper<AccEventEntity>().eq("closure_id", closureId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByBookingId(Long bookingId) {
        List<AccEventEntity> eventEntityList = baseDao.selectList(new QueryWrapper<AccEventEntity>().eq("booking_id", bookingId));
        if (eventEntityList != null && !eventEntityList.isEmpty()) {
            Long accommodationId = eventEntityList.get(0).getAccommodationId();
            List<AccAvailabilityEntity> availabilityEntityList = accAvailabilityService.getByAccommodationId(accommodationId);
            AccommodationAvailability accommodationAvailability = new AccommodationAvailability(availabilityEntityList);

            eventEntityList.forEach(accEventEntity ->
                    accommodationAvailability.markAvailable(accEventEntity.getStartTime(), accEventEntity.getEndTime())
            );
            accAvailabilityService.update(accommodationId, accommodationAvailability);
        }

        baseDao.delete(new QueryWrapper<AccEventEntity>().eq("booking_id", bookingId));
    }
}