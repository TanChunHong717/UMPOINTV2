package my.edu.um.umpoint.modules.security.service.impl;

import my.edu.um.umpoint.common.constant.Constant;
import my.edu.um.umpoint.common.service.impl.BaseServiceImpl;
import my.edu.um.umpoint.modules.security.oauth2.TokenGenerator;
import my.edu.um.umpoint.common.utils.Result;
import my.edu.um.umpoint.modules.security.dao.SysUserTokenDao;
import my.edu.um.umpoint.modules.security.entity.SysUserTokenEntity;
import my.edu.um.umpoint.modules.security.service.SysUserTokenService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class SysUserTokenServiceImpl extends BaseServiceImpl<SysUserTokenDao, SysUserTokenEntity> implements SysUserTokenService {
	private final static int EXPIRE = 3600 * 12;

	@Override
	public Result createToken(Long userId) {
		String token;
		Date now = new Date();
		Date expireTime = new Date(now.getTime() + EXPIRE * 1000);

		SysUserTokenEntity tokenEntity = baseDao.getByUserId(userId);
		if(tokenEntity == null){
			token = TokenGenerator.generateValue();

			tokenEntity = new SysUserTokenEntity();
			tokenEntity.setUserId(userId);
			tokenEntity.setToken(token);
			tokenEntity.setUpdateDate(now);
			tokenEntity.setExpireDate(expireTime);

			this.insert(tokenEntity);
		}else{
			if(tokenEntity.getExpireDate().getTime() < System.currentTimeMillis()){
				token = TokenGenerator.generateValue();
			}else {
				token = tokenEntity.getToken();
			}

			tokenEntity.setToken(token);
			tokenEntity.setUpdateDate(now);
			tokenEntity.setExpireDate(expireTime);

			this.updateById(tokenEntity);
		}

		Map<String, Object> map = new HashMap<>(2);
		map.put(Constant.TOKEN_HEADER, token);
		map.put("expire", EXPIRE);
		return new Result().ok(map);
	}

	@Override
	public void logout(Long userId) {
		String token = TokenGenerator.generateValue();
		baseDao.updateToken(userId, token);
	}
}