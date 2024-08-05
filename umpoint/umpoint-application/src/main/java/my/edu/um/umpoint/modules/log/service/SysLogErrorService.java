package my.edu.um.umpoint.modules.log.service;


import my.edu.um.umpoint.common.page.PageData;
import my.edu.um.umpoint.common.service.BaseService;
import my.edu.um.umpoint.modules.log.dto.SysLogErrorDTO;
import my.edu.um.umpoint.modules.log.entity.SysLogErrorEntity;

import java.util.List;
import java.util.Map;

public interface SysLogErrorService extends BaseService<SysLogErrorEntity> {

    PageData<SysLogErrorDTO> page(Map<String, Object> params);

    List<SysLogErrorDTO> list(Map<String, Object> params);

    void save(SysLogErrorEntity entity);

}