package my.edu.um.umpoint.modules.accommodation.service;

import my.edu.um.umpoint.common.page.PageData;
import my.edu.um.umpoint.common.service.CrudService;
import my.edu.um.umpoint.modules.accommodation.dto.AccAccommodationDTO;
import my.edu.um.umpoint.modules.accommodation.entity.AccAccommodationEntity;

import java.util.Map;

/**
 * Accommodation
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-23
 */
public interface AccAccommodationService extends CrudService<AccAccommodationEntity, AccAccommodationDTO> {
    PageData<AccAccommodationDTO> accommodationPage(Map<String, Object> params);
    PageData<AccAccommodationDTO> bookingRulePage(Map<String, Object> params);
}