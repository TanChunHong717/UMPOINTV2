package my.edu.um.umpoint.modules.space.service;

import my.edu.um.umpoint.common.service.CrudService;
import my.edu.um.umpoint.modules.space.dto.SpcBookingDTO;
import my.edu.um.umpoint.modules.space.dto.SpcClientBookingDTO;
import my.edu.um.umpoint.modules.space.entity.SpcBookingEntity;

import java.util.List;

/**
 * Space Booking
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-09-08
 */
public interface SpcBookingService extends CrudService<SpcBookingEntity, SpcBookingDTO> {
    void approve(Long id, List<Long> technicianIdList);
    void reject(Long id);
    void reject(List<Long> idList);
    void cancel(Long id);
    void pay(Long id);
    Long getUserId(Long id);
    void validateBookingHasOverlap(SpcClientBookingDTO request);
}