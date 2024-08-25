package my.edu.um.umpoint.modules.accommodation.service;

import my.edu.um.umpoint.common.service.CrudService;
import my.edu.um.umpoint.modules.accommodation.dto.AccImageDTO;
import my.edu.um.umpoint.modules.accommodation.entity.AccImageEntity;

/**
 * Accommodation Image
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-23
 */
public interface AccImageService extends CrudService<AccImageEntity, AccImageDTO> {
    void deleteByAccommodationId(Long accommodationId);
}