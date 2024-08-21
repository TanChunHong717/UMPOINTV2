package my.edu.um.umpoint.modules.service.service;

import my.edu.um.umpoint.common.service.CrudService;
import my.edu.um.umpoint.modules.service.dto.SvcServiceTagDTO;
import my.edu.um.umpoint.modules.service.entity.SvcServiceTagEntity;

/**
 * Service tag relationship
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-20
 */
public interface SvcServiceTagService extends CrudService<SvcServiceTagEntity, SvcServiceTagDTO> {
    void deleteByServiceId(Long serviceId);
}