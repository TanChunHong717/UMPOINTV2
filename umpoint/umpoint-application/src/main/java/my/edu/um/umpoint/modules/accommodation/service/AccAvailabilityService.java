package my.edu.um.umpoint.modules.accommodation.service;

import my.edu.um.umpoint.common.service.CrudService;
import my.edu.um.umpoint.modules.accommodation.availability.AccommodationAvailability;
import my.edu.um.umpoint.modules.accommodation.dto.AccAvailabilityDTO;
import my.edu.um.umpoint.modules.accommodation.entity.AccAvailabilityEntity;

import java.util.List;

/**
 * Accommodation Availability
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-23
 */
public interface AccAvailabilityService extends CrudService<AccAvailabilityEntity, AccAvailabilityDTO> {
    List<AccAvailabilityEntity> getByAccommodationId(Long accommodationId);
    void update(Long accommodationId, AccommodationAvailability accommodationAvailability);
}