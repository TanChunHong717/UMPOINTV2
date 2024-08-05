package my.edu.um.umpoint.modules.sys.service;

import my.edu.um.umpoint.common.service.BaseService;
import my.edu.um.umpoint.modules.sys.entity.SysRoleMenuEntity;

import java.util.List;

public interface SysRoleMenuService extends BaseService<SysRoleMenuEntity> {

	List<Long> getMenuIdList(Long roleId);

	void saveOrUpdate(Long roleId, List<Long> menuIdList);

	void deleteByRoleIds(Long[] roleIds);

	void deleteByMenuId(Long menuId);
}