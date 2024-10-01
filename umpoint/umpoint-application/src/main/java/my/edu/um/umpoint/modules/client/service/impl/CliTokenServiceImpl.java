package my.edu.um.umpoint.modules.client.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import my.edu.um.umpoint.common.constant.Constant;
import my.edu.um.umpoint.common.service.impl.CrudServiceImpl;
import my.edu.um.umpoint.common.utils.Result;
import my.edu.um.umpoint.modules.client.dao.CliTokenDao;
import my.edu.um.umpoint.modules.client.dto.CliTokenDTO;
import my.edu.um.umpoint.modules.client.entity.CliTokenEntity;
import my.edu.um.umpoint.modules.client.service.CliTokenService;
import cn.hutool.core.util.StrUtil;
import my.edu.um.umpoint.modules.security.entity.SysUserTokenEntity;
import my.edu.um.umpoint.modules.security.oauth2.TokenGenerator;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * UserToken
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-10-01
 */
@Service
public class CliTokenServiceImpl extends CrudServiceImpl<CliTokenDao, CliTokenEntity, CliTokenDTO> implements CliTokenService {

    private final static int EXPIRE = 3600 * 12;

    @Override
    public QueryWrapper<CliTokenEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<CliTokenEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }


    @Override
    public Result createToken(Long userId) {
        String token;
        Date now = new Date();
        Date expireTime = new Date(now.getTime() + EXPIRE * 1000);

        QueryWrapper<CliTokenEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);

        CliTokenEntity tokenEntity = baseDao.selectOne(queryWrapper);
        if(tokenEntity == null){
            token = TokenGenerator.generateValue();

            tokenEntity = new CliTokenEntity();
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
        CliTokenEntity tokenEntity = new CliTokenEntity();
        tokenEntity.setToken(token);

        QueryWrapper<CliTokenEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);

        baseDao.update(tokenEntity, queryWrapper);
    }
}