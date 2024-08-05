package my.edu.um.umpoint.modules.log.service;

import my.edu.um.umpoint.common.page.PageData;
import my.edu.um.umpoint.common.service.BaseService;
import my.edu.um.umpoint.modules.log.dto.SysLogOperationDTO;
import my.edu.um.umpoint.modules.log.entity.SysLogOperationEntity;

import java.util.List;
import java.util.Map;

public interface SysLogOperationService extends BaseService<SysLogOperationEntity> {

    PageData<SysLogOperationDTO> page(Map<String, Object> params);

    List<SysLogOperationDTO> list(Map<String, Object> params);

    void save(SysLogOperationEntity entity);
}