package my.edu.um.umpoint.modules.space.service;

import my.edu.um.umpoint.common.service.CrudService;
import my.edu.um.umpoint.modules.space.dto.SpcBookingDTO;
import my.edu.um.umpoint.modules.space.dto.SpcClosureDTO;
import my.edu.um.umpoint.modules.space.dto.SpcEventDTO;
import my.edu.um.umpoint.modules.space.entity.SpcEventEntity;

import java.util.List;

/**
 * Space Occupied Event
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-09-16
 */
public interface SpcEventService extends CrudService<SpcEventEntity, SpcEventDTO> {
    void addEvent(SpcBookingDTO bookingDTO);
    void addEvent(SpcClosureDTO closureDTO);
    void deleteByBookingId(Long bookingId);
    void deleteByBookingId(List<Long> bookingIdList);
    void deleteByClosureId(Long closureId);
    void deleteByClosureId(List<Long> closureIdList);
}