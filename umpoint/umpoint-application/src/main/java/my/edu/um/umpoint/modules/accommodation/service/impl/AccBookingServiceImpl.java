package my.edu.um.umpoint.modules.accommodation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import my.edu.um.umpoint.common.constant.BookingConstant;
import my.edu.um.umpoint.common.page.PageData;
import my.edu.um.umpoint.common.service.impl.CrudServiceImpl;
import my.edu.um.umpoint.modules.accommodation.dao.AccBookingDao;
import my.edu.um.umpoint.modules.accommodation.dto.AccBookingDTO;
import my.edu.um.umpoint.modules.accommodation.entity.AccBookingEntity;
import my.edu.um.umpoint.modules.accommodation.service.AccBookingService;
import cn.hutool.core.util.StrUtil;
import my.edu.um.umpoint.modules.accommodation.service.AccEventService;
import my.edu.um.umpoint.modules.security.user.SecurityUser;
import my.edu.um.umpoint.modules.security.user.UserDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private AccEventService accEventService;

    @Override
    public QueryWrapper<AccBookingEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<AccBookingEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }

    @Override
    public PageData<AccBookingDTO> page(Map<String, Object> params) {
        paramsToLike(params, "event");

        IPage<AccBookingEntity> page = getPage(params, "create_date", false);
        List<AccBookingEntity> list = baseDao.getList(params);

        return getPageData(list, page.getTotal(), currentDtoClass());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(AccBookingDTO accBookingDTO) {
        super.save(accBookingDTO);
        accEventService.addEvent(accBookingDTO);
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
    @Transactional(rollbackFor = Exception.class)
    public void reject(Long id) {
        UserDetail user = SecurityUser.getUser();
        AccBookingEntity entity = new AccBookingEntity();
        entity.setAdminId(user.getId());
        entity.setStatus(BookingConstant.BookingStatus.REJECT.getValue());

        baseDao.update(entity, new QueryWrapper<AccBookingEntity>().eq("id",id));
        accEventService.deleteByBookingId(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancel(Long id) {
        AccBookingEntity entity = new AccBookingEntity();
        entity.setStatus(BookingConstant.BookingStatus.CANCELLED.getValue());

        baseDao.update(entity, new QueryWrapper<AccBookingEntity>().eq("id",id));
        accEventService.deleteByBookingId(id);
    }
}