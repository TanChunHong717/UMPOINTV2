package my.edu.um.umpoint.modules.client.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import my.edu.um.umpoint.common.service.impl.CrudServiceImpl;
import my.edu.um.umpoint.common.utils.ConvertUtils;
import my.edu.um.umpoint.modules.client.dao.CliUserDao;
import my.edu.um.umpoint.modules.client.dto.CliUserDTO;
import my.edu.um.umpoint.modules.client.entity.CliUserEntity;
import my.edu.um.umpoint.modules.client.service.CliUserService;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * User
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-10-01
 */
@Service
public class CliUserServiceImpl extends CrudServiceImpl<CliUserDao, CliUserEntity, CliUserDTO> implements CliUserService {

    @Override
    public QueryWrapper<CliUserEntity> getWrapper(Map<String, Object> params){
        String username = (String)params.get("username");
        String type = (String)params.get("type");

        QueryWrapper<CliUserEntity> wrapper = new QueryWrapper<>();
        wrapper.like(StrUtil.isNotBlank(username), "username", username);
        wrapper.eq(StrUtil.isNotBlank(type), "type", type);

        return wrapper;
    }


    @Override
    public CliUserDTO getByUsername(String username) {
        QueryWrapper<CliUserEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        return ConvertUtils.sourceToTarget(baseDao.selectOne(queryWrapper), CliUserDTO.class);
    }

}