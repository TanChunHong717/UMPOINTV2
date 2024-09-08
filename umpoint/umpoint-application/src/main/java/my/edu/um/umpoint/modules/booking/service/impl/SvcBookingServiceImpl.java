package my.edu.um.umpoint.modules.booking.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import my.edu.um.umpoint.common.service.impl.CrudServiceImpl;
import my.edu.um.umpoint.modules.booking.dao.SvcBookingDao;
import my.edu.um.umpoint.modules.booking.dto.SvcBookingDTO;
import my.edu.um.umpoint.modules.booking.entity.SvcBookingEntity;
import my.edu.um.umpoint.modules.booking.service.SvcBookingService;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Space Booking
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-09-08
 */
@Service
public class SvcBookingServiceImpl extends CrudServiceImpl<SvcBookingDao, SvcBookingEntity, SvcBookingDTO> implements SvcBookingService {

    @Override
    public QueryWrapper<SvcBookingEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<SvcBookingEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }


}