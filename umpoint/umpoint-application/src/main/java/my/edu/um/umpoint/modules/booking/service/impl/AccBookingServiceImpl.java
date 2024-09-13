package my.edu.um.umpoint.modules.booking.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import my.edu.um.umpoint.common.constant.BookingConstant;
import my.edu.um.umpoint.common.page.PageData;
import my.edu.um.umpoint.common.service.impl.CrudServiceImpl;
import my.edu.um.umpoint.modules.booking.dao.AccBookingDao;
import my.edu.um.umpoint.modules.booking.dto.AccBookingDTO;
import my.edu.um.umpoint.modules.booking.entity.AccBookingEntity;
import my.edu.um.umpoint.modules.booking.service.AccBookingService;
import cn.hutool.core.util.StrUtil;
import my.edu.um.umpoint.modules.security.user.SecurityUser;
import my.edu.um.umpoint.modules.security.user.UserDetail;
import org.springframework.stereotype.Service;

import java.util.List;
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

    @Override
    public PageData<AccBookingDTO> page(Map<String, Object> params) {
        paramsToLike(params, "accommodation");

        IPage<AccBookingEntity> page = getPage(params, "create_date", false);
        List<AccBookingEntity> list = baseDao.getList(params);

        return getPageData(list, page.getTotal(), currentDtoClass());
    }

    @Override
    public void approve(Long id) {
        UserDetail user = SecurityUser.getUser();
        AccBookingEntity entity = new AccBookingEntity();
        entity.setAdminId(user.getId());
        entity.setStatus(BookingConstant.BookingStatus.APPROVED.getValue());

        baseDao.update(entity, new QueryWrapper<AccBookingEntity>().eq("id",id));
    }

    @Override
    public void reject(Long id) {
        UserDetail user = SecurityUser.getUser();
        AccBookingEntity entity = new AccBookingEntity();
        entity.setAdminId(user.getId());
        entity.setStatus(BookingConstant.BookingStatus.REJECT.getValue());

        baseDao.update(entity, new QueryWrapper<AccBookingEntity>().eq("id",id));
    }
}