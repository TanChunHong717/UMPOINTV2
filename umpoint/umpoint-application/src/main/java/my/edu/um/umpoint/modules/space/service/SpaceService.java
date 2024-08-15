package my.edu.um.umpoint.modules.space.service;

import my.edu.um.umpoint.common.service.CrudService;
import my.edu.um.umpoint.modules.space.dto.SpaceDTO;
import my.edu.um.umpoint.modules.space.entity.SpaceEntity;

/**
 * Space
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-03
 */
public interface SpaceService extends CrudService<SpaceEntity, SpaceDTO> {
    Long saveWithID(SpaceDTO dto);
}