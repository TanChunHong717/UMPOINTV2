package my.edu.um.umpoint.modules.accommodation.service;

import my.edu.um.umpoint.common.service.CrudService;
import my.edu.um.umpoint.modules.accommodation.dto.AccAccommodationTagDTO;
import my.edu.um.umpoint.modules.accommodation.entity.AccAccommodationTagEntity;

/**
 * Accommodation tag relationship
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-23
 */
public interface AccAccommodationTagService extends CrudService<AccAccommodationTagEntity, AccAccommodationTagDTO> {
    void deleteByAccommodationId(Long accommodationId);
}