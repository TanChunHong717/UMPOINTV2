package my.edu.um.umpoint.modules.accommodation.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import my.edu.um.umpoint.common.service.impl.CrudServiceImpl;
import my.edu.um.umpoint.common.utils.ConvertUtils;
import my.edu.um.umpoint.modules.accommodation.dao.AccBookingRuleDao;
import my.edu.um.umpoint.modules.accommodation.dto.AccBookingRuleDTO;
import my.edu.um.umpoint.modules.accommodation.entity.AccBookingRuleEntity;
import my.edu.um.umpoint.modules.accommodation.service.AccBookingRuleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Accommodation Booking Rule
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-23
 */
@Service
public class AccBookingRuleServiceImpl extends CrudServiceImpl<AccBookingRuleDao, AccBookingRuleEntity, AccBookingRuleDTO> implements AccBookingRuleService {

    @Override
    public QueryWrapper<AccBookingRuleEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<AccBookingRuleEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }


    @Override
    public void updateBatch(List<Long> idList, AccBookingRuleDTO accBookingRuleDTO) {
        QueryWrapper<AccBookingRuleEntity> wrapper = new QueryWrapper<>();
        wrapper.in("id", idList);

        baseDao.update(ConvertUtils.sourceToTarget(accBookingRuleDTO, currentModelClass()), wrapper);
    }
}