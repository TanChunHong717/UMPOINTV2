package my.edu.um.umpoint.modules.space.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import my.edu.um.umpoint.common.constant.BookingConstant;
import my.edu.um.umpoint.common.exception.RenException;
import my.edu.um.umpoint.common.page.PageData;
import my.edu.um.umpoint.common.service.impl.CrudServiceImpl;
import my.edu.um.umpoint.modules.space.service.SpcEventService;
import my.edu.um.umpoint.modules.space.dao.SpcBookingDao;
import my.edu.um.umpoint.modules.space.dto.SpcBookingDTO;
import my.edu.um.umpoint.modules.space.entity.SpcBookingEntity;
import my.edu.um.umpoint.modules.space.service.SpcBookingService;
import cn.hutool.core.util.StrUtil;
import my.edu.um.umpoint.modules.security.user.SecurityUser;
import my.edu.um.umpoint.modules.security.user.UserDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Space Booking
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-09-08
 */
@Service
public class SpcBookingServiceImpl extends CrudServiceImpl<SpcBookingDao, SpcBookingEntity, SpcBookingDTO> implements SpcBookingService {

    @Autowired
    private SpcEventService spcEventService;

    @Override
    public QueryWrapper<SpcBookingEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<SpcBookingEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }

    @Override
    public PageData<SpcBookingDTO> page(Map<String, Object> params) {
        paramsToLike(params, "space");

        IPage<SpcBookingEntity> page = getPage(params, "create_date", false);
        List<SpcBookingEntity> list = baseDao.getList(params);

        return getPageData(list, page.getTotal(), currentDtoClass());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(SpcBookingDTO spcBookingDTO) {
        super.save(spcBookingDTO);
        spcEventService.addEvent(spcBookingDTO);
    }

    @Override
    public void approve(Long id) {
        UserDetail user = SecurityUser.getUser();
        SpcBookingEntity entity = new SpcBookingEntity();
        entity.setAdminId(user.getId());
        entity.setStatus(BookingConstant.BookingStatus.APPROVED.getValue());

        baseDao.updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reject(Long id) {
        UserDetail user = SecurityUser.getUser();
        SpcBookingEntity entity = new SpcBookingEntity();
        entity.setAdminId(user.getId());
        entity.setStatus(BookingConstant.BookingStatus.REJECT.getValue());

        baseDao.update(entity, new QueryWrapper<SpcBookingEntity>().eq("id", id));
        spcEventService.deleteByBookingId(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancel(Long id) {
        SpcBookingEntity entity = new SpcBookingEntity();
        entity.setStatus(BookingConstant.BookingStatus.CANCELLED.getValue());

        baseDao.update(entity, new QueryWrapper<SpcBookingEntity>().eq("id", id));
        spcEventService.deleteByBookingId(id);
    }
}