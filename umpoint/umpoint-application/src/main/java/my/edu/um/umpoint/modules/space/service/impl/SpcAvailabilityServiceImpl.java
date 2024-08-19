package my.edu.um.umpoint.modules.space.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import my.edu.um.umpoint.common.service.impl.CrudServiceImpl;
import my.edu.um.umpoint.modules.space.dao.SpcAvailabilityDao;
import my.edu.um.umpoint.modules.space.dto.SpcAvailabilityDTO;
import my.edu.um.umpoint.modules.space.entity.SpcAvailabilityEntity;
import my.edu.um.umpoint.modules.space.service.SpcAvailabilityService;
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
public class SpcAvailabilityServiceImpl extends CrudServiceImpl<SpcAvailabilityDao, SpcAvailabilityEntity, SpcAvailabilityDTO> implements SpcAvailabilityService {

    @Override
    public QueryWrapper<SpcAvailabilityEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<SpcAvailabilityEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }


}