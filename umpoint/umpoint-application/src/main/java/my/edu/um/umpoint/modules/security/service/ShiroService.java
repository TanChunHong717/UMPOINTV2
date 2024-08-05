package my.edu.um.umpoint.modules.security.service;

import my.edu.um.umpoint.modules.security.user.UserDetail;
import my.edu.um.umpoint.modules.sys.entity.SysUserEntity;
import my.edu.um.umpoint.modules.security.entity.SysUserTokenEntity;

import java.util.List;
import java.util.Set;

public interface ShiroService {

    Set<String> getUserPermissions(UserDetail user);

    SysUserTokenEntity getByToken(String token);

    SysUserEntity getUser(Long userId);

    List<Long> getDataScopeList(Long userId);
}
