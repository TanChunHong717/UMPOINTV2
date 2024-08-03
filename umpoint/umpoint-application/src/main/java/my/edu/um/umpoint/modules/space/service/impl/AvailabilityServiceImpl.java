package my.edu.um.umpoint.modules.space.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import my.edu.um.umpoint.common.service.impl.CrudServiceImpl;
import my.edu.um.umpoint.modules.space.dao.AvailabilityDao;
import my.edu.um.umpoint.modules.space.dto.AvailabilityDTO;
import my.edu.um.umpoint.modules.space.entity.AvailabilityEntity;
import my.edu.um.umpoint.modules.space.service.AvailabilityService;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Space Image
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-03
 */
@Service
public class AvailabilityServiceImpl extends CrudServiceImpl<AvailabilityDao, AvailabilityEntity, AvailabilityDTO> implements AvailabilityService {

    @Override
    public QueryWrapper<AvailabilityEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<AvailabilityEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }


}