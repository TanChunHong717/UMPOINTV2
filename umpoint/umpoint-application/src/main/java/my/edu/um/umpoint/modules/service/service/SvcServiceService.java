package my.edu.um.umpoint.modules.service.service;

import my.edu.um.umpoint.common.page.PageData;
import my.edu.um.umpoint.common.service.CrudService;
import my.edu.um.umpoint.modules.service.dto.SvcServiceDTO;
import my.edu.um.umpoint.modules.service.entity.SvcServiceEntity;

import java.util.Map;

/**
 * Service
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-20
 */
public interface SvcServiceService extends CrudService<SvcServiceEntity, SvcServiceDTO> {
    PageData<SvcServiceDTO> servicePage(Map<String, Object> params);
    PageData<SvcServiceDTO> bookingRulePage(Map<String, Object> params);
}