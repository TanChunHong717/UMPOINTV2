package my.edu.um.umpoint.modules.accommodation.service;

import my.edu.um.umpoint.common.service.CrudService;
import my.edu.um.umpoint.modules.accommodation.dto.AccBookingDTO;
import my.edu.um.umpoint.modules.accommodation.dto.AccClosureDTO;
import my.edu.um.umpoint.modules.accommodation.dto.AccEventDTO;
import my.edu.um.umpoint.modules.accommodation.entity.AccEventEntity;

/**
 * Accommodation Occupied Event
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-09-16
 */
public interface AccEventService extends CrudService<AccEventEntity, AccEventDTO> {
    void addEvent(AccBookingDTO bookingDTO);
    void addEvent(AccClosureDTO closureDTO);
    void deleteByBookingId(Long bookingId);
    void deleteByClosureId(Long closureId);
    void deleteByClosureId(Long[] closureIds);
}