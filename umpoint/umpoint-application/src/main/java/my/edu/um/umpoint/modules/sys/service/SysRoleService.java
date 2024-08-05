package my.edu.um.umpoint.modules.sys.service;

import my.edu.um.umpoint.common.page.PageData;
import my.edu.um.umpoint.common.service.BaseService;
import my.edu.um.umpoint.modules.sys.dto.SysRoleDTO;
import my.edu.um.umpoint.modules.sys.entity.SysRoleEntity;

import java.util.List;
import java.util.Map;

public interface SysRoleService extends BaseService<SysRoleEntity> {

	PageData<SysRoleDTO> page(Map<String, Object> params);

	List<SysRoleDTO> list(Map<String, Object> params);

	SysRoleDTO get(Long id);

	void save(SysRoleDTO dto);

	void update(SysRoleDTO dto);

	void delete(Long[] ids);

}
