package my.edu.um.umpoint.modules.booking.service;

import my.edu.um.umpoint.common.service.CrudService;
import my.edu.um.umpoint.modules.booking.dto.AccBookingDTO;
import my.edu.um.umpoint.modules.booking.entity.AccBookingEntity;

/**
 * Accommodation Booking
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-09-08
 */
public interface AccBookingService extends CrudService<AccBookingEntity, AccBookingDTO> {
    void approve(Long id);
    void reject(Long id);
}