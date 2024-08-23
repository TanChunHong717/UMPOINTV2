package my.edu.um.umpoint.modules.accommodation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import my.edu.um.umpoint.common.service.impl.CrudServiceImpl;
import my.edu.um.umpoint.modules.accommodation.dao.AccAvailabilityDao;
import my.edu.um.umpoint.modules.accommodation.dto.AccAvailabilityDTO;
import my.edu.um.umpoint.modules.accommodation.entity.AccAvailabilityEntity;
import my.edu.um.umpoint.modules.accommodation.service.AccAvailabilityService;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Accommodation Availability
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-23
 */
@Service
public class AccAvailabilityServiceImpl extends CrudServiceImpl<AccAvailabilityDao, AccAvailabilityEntity, AccAvailabilityDTO> implements AccAvailabilityService {

    @Override
    public QueryWrapper<AccAvailabilityEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<AccAvailabilityEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }


}