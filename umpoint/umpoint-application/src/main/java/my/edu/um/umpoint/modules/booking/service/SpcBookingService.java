package my.edu.um.umpoint.modules.booking.service;

import my.edu.um.umpoint.common.service.CrudService;
import my.edu.um.umpoint.modules.booking.dto.SpcBookingDTO;
import my.edu.um.umpoint.modules.booking.entity.SpcBookingEntity;

/**
 * Space Booking
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-09-08
 */
public interface SpcBookingService extends CrudService<SpcBookingEntity, SpcBookingDTO> {
    void approve(Long id);
    void reject(Long id);
}