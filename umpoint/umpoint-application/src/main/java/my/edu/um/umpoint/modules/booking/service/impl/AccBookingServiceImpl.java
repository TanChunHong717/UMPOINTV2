package my.edu.um.umpoint.modules.booking.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import my.edu.um.umpoint.common.service.impl.CrudServiceImpl;
import my.edu.um.umpoint.modules.booking.dao.AccBookingDao;
import my.edu.um.umpoint.modules.booking.dto.AccBookingDTO;
import my.edu.um.umpoint.modules.booking.entity.AccBookingEntity;
import my.edu.um.umpoint.modules.booking.service.AccBookingService;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Accommodation Booking
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-09-08
 */
@Service
public class AccBookingServiceImpl extends CrudServiceImpl<AccBookingDao, AccBookingEntity, AccBookingDTO> implements AccBookingService {

    @Override
    public QueryWrapper<AccBookingEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<AccBookingEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }


}