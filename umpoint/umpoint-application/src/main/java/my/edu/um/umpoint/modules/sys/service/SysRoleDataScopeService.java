package my.edu.um.umpoint.modules.sys.service;

import my.edu.um.umpoint.common.service.BaseService;
import my.edu.um.umpoint.modules.sys.entity.SysRoleDataScopeEntity;

import java.util.List;

public interface SysRoleDataScopeService extends BaseService<SysRoleDataScopeEntity> {

    List<Long> getDeptIdList(Long roleId);

    void saveOrUpdate(Long roleId, List<Long> deptIdList);

    void deleteByRoleIds(Long[] roleId);
}