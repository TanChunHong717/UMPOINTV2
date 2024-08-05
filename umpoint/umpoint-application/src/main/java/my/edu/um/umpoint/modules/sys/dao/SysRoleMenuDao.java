package my.edu.um.umpoint.modules.sys.dao;

import my.edu.um.umpoint.common.dao.BaseDao;
import my.edu.um.umpoint.modules.sys.entity.SysRoleMenuEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysRoleMenuDao extends BaseDao<SysRoleMenuEntity> {

	List<Long> getMenuIdList(Long roleId);

	void deleteByRoleIds(Long[] roleIds);

	void deleteByMenuId(Long menuId);
}
