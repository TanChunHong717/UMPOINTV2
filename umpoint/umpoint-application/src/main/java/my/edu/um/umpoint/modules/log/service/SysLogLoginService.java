package my.edu.um.umpoint.modules.log.service;

import my.edu.um.umpoint.common.page.PageData;
import my.edu.um.umpoint.common.service.BaseService;
import my.edu.um.umpoint.modules.log.dto.SysLogLoginDTO;
import my.edu.um.umpoint.modules.log.entity.SysLogLoginEntity;

import java.util.List;
import java.util.Map;

public interface SysLogLoginService extends BaseService<SysLogLoginEntity> {

    PageData<SysLogLoginDTO> page(Map<String, Object> params);

    List<SysLogLoginDTO> list(Map<String, Object> params);

    void save(SysLogLoginEntity entity);
}