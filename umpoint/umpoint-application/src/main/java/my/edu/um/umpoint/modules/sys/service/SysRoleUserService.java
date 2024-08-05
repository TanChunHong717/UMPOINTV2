package my.edu.um.umpoint.modules.sys.service;

import my.edu.um.umpoint.common.service.BaseService;
import my.edu.um.umpoint.modules.sys.entity.SysRoleUserEntity;

import java.util.List;

public interface SysRoleUserService extends BaseService<SysRoleUserEntity> {

    void saveOrUpdate(Long userId, List<Long> roleIdList);

    void deleteByRoleIds(Long[] roleIds);

    void deleteByUserIds(Long[] userIds);

    List<Long> getRoleIdList(Long userId);
}