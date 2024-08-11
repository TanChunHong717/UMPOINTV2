package my.edu.um.umpoint.modules.space.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import my.edu.um.umpoint.common.service.impl.CrudServiceImpl;
import my.edu.um.umpoint.modules.space.dao.BookingRuleDao;
import my.edu.um.umpoint.modules.space.dto.BookingRuleDTO;
import my.edu.um.umpoint.modules.space.entity.BookingRuleEntity;
import my.edu.um.umpoint.modules.space.service.BookingRuleService;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Space Availability
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-11
 */
@Service
public class BookingRuleServiceImpl extends CrudServiceImpl<BookingRuleDao, BookingRuleEntity, BookingRuleDTO> implements BookingRuleService {

    @Override
    public QueryWrapper<BookingRuleEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<BookingRuleEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }


}