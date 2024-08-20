package my.edu.um.umpoint.modules.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import my.edu.um.umpoint.common.service.impl.CrudServiceImpl;
import my.edu.um.umpoint.modules.service.dao.SvcBookingRuleDao;
import my.edu.um.umpoint.modules.service.dto.SvcBookingRuleDTO;
import my.edu.um.umpoint.modules.service.entity.SvcBookingRuleEntity;
import my.edu.um.umpoint.modules.service.service.SvcBookingRuleService;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Service Booking Rule
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-20
 */
@Service
public class SvcBookingRuleServiceImpl extends CrudServiceImpl<SvcBookingRuleDao, SvcBookingRuleEntity, SvcBookingRuleDTO> implements SvcBookingRuleService {

    @Override
    public QueryWrapper<SvcBookingRuleEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<SvcBookingRuleEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }


}