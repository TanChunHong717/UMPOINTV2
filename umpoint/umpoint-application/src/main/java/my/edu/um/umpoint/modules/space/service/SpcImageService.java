package my.edu.um.umpoint.modules.space.service;

import my.edu.um.umpoint.common.service.CrudService;
import my.edu.um.umpoint.modules.space.dto.SpcImageDTO;
import my.edu.um.umpoint.modules.space.entity.SpcImageEntity;

/**
 * Space Image
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-03
 */
public interface SpcImageService extends CrudService<SpcImageEntity, SpcImageDTO> {
    void deleteBySpaceId(Long spaceId);
}