package my.edu.um.umpoint.modules.accommodation.service;

import my.edu.um.umpoint.common.service.CrudService;
import my.edu.um.umpoint.modules.accommodation.dto.AccBookingDTO;
import my.edu.um.umpoint.modules.accommodation.entity.AccBookingEntity;

import java.util.List;

/**
 * Accommodation Booking
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-09-08
 */
public interface AccBookingService extends CrudService<AccBookingEntity, AccBookingDTO> {
    void approve(Long id, List<Long> technicianIdList);
    void reject(Long id);
    void cancel(Long id);
    Long getUserId(Long id);
}