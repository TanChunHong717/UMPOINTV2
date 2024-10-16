package my.edu.um.umpoint.modules.space.service;

import my.edu.um.umpoint.common.page.PageData;
import my.edu.um.umpoint.common.service.CrudService;
import my.edu.um.umpoint.modules.space.dto.SpcSpaceDTO;
import my.edu.um.umpoint.modules.space.entity.SpcSpaceEntity;

import java.util.Map;

/**
 * Space
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-03
 */
public interface SpcSpaceService extends CrudService<SpcSpaceEntity, SpcSpaceDTO> {
    PageData<SpcSpaceDTO> spacePage(Map<String, Object> params);
    PageData<SpcSpaceDTO> bookingRulePage(Map<String, Object> params);
}