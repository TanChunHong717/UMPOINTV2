package my.edu.um.umpoint.modules.service.service;

import my.edu.um.umpoint.common.service.CrudService;
import my.edu.um.umpoint.modules.service.dto.SvcServiceDTO;
import my.edu.um.umpoint.modules.service.entity.SvcServiceEntity;

/**
 * Service
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-20
 */
public interface SvcServiceService extends CrudService<SvcServiceEntity, SvcServiceDTO> {
    void applyDefaultBookingRule(Long[] ids);
}