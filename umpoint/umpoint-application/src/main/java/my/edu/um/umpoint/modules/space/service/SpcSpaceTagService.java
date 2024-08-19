package my.edu.um.umpoint.modules.space.service;

import my.edu.um.umpoint.common.service.CrudService;
import my.edu.um.umpoint.modules.space.dto.SpcSpaceTagDTO;
import my.edu.um.umpoint.modules.space.entity.SpcSpaceTagEntity;

/**
 * Space tag relationship
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-03
 */
public interface SpcSpaceTagService extends CrudService<SpcSpaceTagEntity, SpcSpaceTagDTO> {
    void deleteBySpaceId(Long spaceId);
}