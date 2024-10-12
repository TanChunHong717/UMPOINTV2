package my.edu.um.umpoint.modules.space.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import my.edu.um.umpoint.common.service.impl.CrudServiceImpl;
import my.edu.um.umpoint.modules.space.dao.SpcBookingTechnicianDao;
import my.edu.um.umpoint.modules.space.dto.SpcBookingTechnicianDTO;
import my.edu.um.umpoint.modules.space.entity.SpcBookingTechnicianEntity;
import my.edu.um.umpoint.modules.space.service.SpcBookingTechnicianService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SpcBookingTechnicianServiceImpl  extends CrudServiceImpl<SpcBookingTechnicianDao, SpcBookingTechnicianEntity, SpcBookingTechnicianDTO> implements SpcBookingTechnicianService {

    @Override
    public QueryWrapper<SpcBookingTechnicianEntity> getWrapper(Map<String, Object> params) {
        String id = (String)params.get("id");

        QueryWrapper<SpcBookingTechnicianEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }

    @Override
    public void deleteByBookingId(Long bookingId) {
        QueryWrapper<SpcBookingTechnicianEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("booking_id", bookingId);

        baseDao.delete(wrapper);
    }
}
