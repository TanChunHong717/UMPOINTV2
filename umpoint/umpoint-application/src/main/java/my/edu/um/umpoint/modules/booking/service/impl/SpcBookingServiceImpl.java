package my.edu.um.umpoint.modules.booking.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import my.edu.um.umpoint.common.service.impl.CrudServiceImpl;
import my.edu.um.umpoint.modules.booking.dao.SpcBookingDao;
import my.edu.um.umpoint.modules.booking.dto.SpcBookingDTO;
import my.edu.um.umpoint.modules.booking.entity.SpcBookingEntity;
import my.edu.um.umpoint.modules.booking.service.SpcBookingService;
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
public class SpcBookingServiceImpl extends CrudServiceImpl<SpcBookingDao, SpcBookingEntity, SpcBookingDTO> implements SpcBookingService {

    @Override
    public QueryWrapper<SpcBookingEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<SpcBookingEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }


}