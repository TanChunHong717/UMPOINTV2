package my.edu.um.umpoint.modules.sys.service;

import my.edu.um.umpoint.common.service.BaseService;
import my.edu.um.umpoint.modules.security.user.UserDetail;
import my.edu.um.umpoint.modules.sys.dto.SysMenuDTO;
import my.edu.um.umpoint.modules.sys.entity.SysMenuEntity;

import java.util.List;

public interface SysMenuService extends BaseService<SysMenuEntity> {

	SysMenuDTO get(Long id);

	void save(SysMenuDTO dto);

	void update(SysMenuDTO dto);

	void delete(Long id);

	List<SysMenuDTO> getAllMenuList(Integer menuType);

	List<SysMenuDTO> getUserMenuList(UserDetail user, Integer menuType);

	List<SysMenuDTO> getListPid(Long pid);
}
