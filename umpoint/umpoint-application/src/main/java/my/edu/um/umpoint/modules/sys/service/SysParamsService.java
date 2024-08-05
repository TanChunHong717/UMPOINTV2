package my.edu.um.umpoint.modules.sys.service;

import my.edu.um.umpoint.common.page.PageData;
import my.edu.um.umpoint.common.service.BaseService;
import my.edu.um.umpoint.modules.sys.dto.SysParamsDTO;
import my.edu.um.umpoint.modules.sys.entity.SysParamsEntity;

import java.util.List;
import java.util.Map;

public interface SysParamsService extends BaseService<SysParamsEntity> {

    PageData<SysParamsDTO> page(Map<String, Object> params);

    List<SysParamsDTO> list(Map<String, Object> params);

    SysParamsDTO get(Long id);

    void save(SysParamsDTO dto);

    void update(SysParamsDTO dto);

    void delete(Long[] ids);

    String getValue(String paramCode);

    <T> T getValueObject(String paramCode, Class<T> clazz);

    int updateValueByCode(String paramCode, String paramValue);
}
