package my.edu.um.umpoint.modules.service.service;

import my.edu.um.umpoint.common.service.CrudService;
import my.edu.um.umpoint.modules.service.dto.SvcBookingDTO;
import my.edu.um.umpoint.modules.service.entity.SvcBookingEntity;

/**
 * Space Booking
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-09-08
 */
public interface SvcBookingService extends CrudService<SvcBookingEntity, SvcBookingDTO> {
    void approve(Long id);
    void reject(Long id);
}