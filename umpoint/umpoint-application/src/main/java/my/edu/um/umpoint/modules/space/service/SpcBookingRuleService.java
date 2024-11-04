package my.edu.um.umpoint.modules.space.service;

import my.edu.um.umpoint.common.service.CrudService;
import my.edu.um.umpoint.modules.space.dto.SpcBookingRuleDTO;
import my.edu.um.umpoint.modules.space.entity.SpcBookingRuleEntity;

import java.util.List;

/**
 * Space Booking Rule
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-11
 */
public interface SpcBookingRuleService extends CrudService<SpcBookingRuleEntity, SpcBookingRuleDTO> {
    void updateBatch(List<Long> idList, SpcBookingRuleDTO spcBookingRuleDTO);
}