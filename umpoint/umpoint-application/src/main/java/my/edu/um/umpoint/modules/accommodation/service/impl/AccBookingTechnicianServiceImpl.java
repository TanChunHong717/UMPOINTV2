package my.edu.um.umpoint.modules.accommodation.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import my.edu.um.umpoint.common.service.impl.CrudServiceImpl;
import my.edu.um.umpoint.modules.accommodation.dao.AccBookingTechnicianDao;
import my.edu.um.umpoint.modules.accommodation.dto.AccBookingTechnicianDTO;
import my.edu.um.umpoint.modules.accommodation.entity.AccBookingTechnicianEntity;
import my.edu.um.umpoint.modules.accommodation.service.AccBookingTechnicianService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AccBookingTechnicianServiceImpl extends CrudServiceImpl<AccBookingTechnicianDao, AccBookingTechnicianEntity, AccBookingTechnicianDTO> implements AccBookingTechnicianService {

    @Override
    public QueryWrapper<AccBookingTechnicianEntity> getWrapper(Map<String, Object> params) {
        String id = (String)params.get("id");

        QueryWrapper<AccBookingTechnicianEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }

    @Override
    public void deleteByBookingId(Long bookingId) {
        QueryWrapper<AccBookingTechnicianEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("booking_id", bookingId);

        baseDao.delete(wrapper);
    }
}
