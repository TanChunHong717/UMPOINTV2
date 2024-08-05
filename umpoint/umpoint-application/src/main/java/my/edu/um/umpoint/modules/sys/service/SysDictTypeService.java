package my.edu.um.umpoint.modules.sys.service;

import my.edu.um.umpoint.common.page.PageData;
import my.edu.um.umpoint.common.service.BaseService;
import my.edu.um.umpoint.modules.sys.dto.SysDictTypeDTO;
import my.edu.um.umpoint.modules.sys.entity.DictType;
import my.edu.um.umpoint.modules.sys.entity.SysDictTypeEntity;

import java.util.List;
import java.util.Map;

public interface SysDictTypeService extends BaseService<SysDictTypeEntity> {

    PageData<SysDictTypeDTO> page(Map<String, Object> params);

    SysDictTypeDTO get(Long id);

    void save(SysDictTypeDTO dto);

    void update(SysDictTypeDTO dto);

    void delete(Long[] ids);

    List<DictType> getAllList();

}