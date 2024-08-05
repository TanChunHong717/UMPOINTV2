package my.edu.um.umpoint.modules.security.service;

import my.edu.um.umpoint.common.service.BaseService;
import my.edu.um.umpoint.common.utils.Result;
import my.edu.um.umpoint.modules.security.entity.SysUserTokenEntity;

public interface SysUserTokenService extends BaseService<SysUserTokenEntity> {

	Result createToken(Long userId);

	void logout(Long userId);

}