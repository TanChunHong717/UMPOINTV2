package my.edu.um.umpoint.modules.sys.dao;

import my.edu.um.umpoint.common.dao.BaseDao;
import my.edu.um.umpoint.modules.sys.entity.SysRoleDataScopeEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysRoleDataScopeDao extends BaseDao<SysRoleDataScopeEntity> {

    List<Long> getDeptIdList(Long roleId);

    List<Long> getDataScopeList(Long userId);

    void deleteByRoleIds(Long[] roleIds);
}