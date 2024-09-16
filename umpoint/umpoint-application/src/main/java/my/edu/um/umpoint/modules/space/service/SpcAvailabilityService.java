package my.edu.um.umpoint.modules.space.service;

import my.edu.um.umpoint.common.service.CrudService;
import my.edu.um.umpoint.modules.space.availability.SpaceAvailability;
import my.edu.um.umpoint.modules.space.dto.SpcAvailabilityDTO;
import my.edu.um.umpoint.modules.space.entity.SpcAvailabilityEntity;

import java.util.List;

/**
 * Space Availability
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-03
 */
public interface SpcAvailabilityService extends CrudService<SpcAvailabilityEntity, SpcAvailabilityDTO> {
    List<SpcAvailabilityEntity> getBySpaceId(Long spaceId);
    void update(Long spaceId, SpaceAvailability spaceAvailability);
}