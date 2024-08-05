package my.edu.um.umpoint.modules.sys.dao;

import my.edu.um.umpoint.common.dao.BaseDao;
import my.edu.um.umpoint.modules.sys.entity.SysMenuEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysMenuDao extends BaseDao<SysMenuEntity> {

	SysMenuEntity getById(@Param("id") Long id);

	List<SysMenuEntity> getMenuList(@Param("menuType") Integer menuType);

	List<SysMenuEntity> getUserMenuList(@Param("userId") Long userId, @Param("menuType") Integer menuType);

	List<String> getUserPermissionsList(Long userId);

	List<String> getPermissionsList();

	List<SysMenuEntity> getListPid(Long pid);

}
