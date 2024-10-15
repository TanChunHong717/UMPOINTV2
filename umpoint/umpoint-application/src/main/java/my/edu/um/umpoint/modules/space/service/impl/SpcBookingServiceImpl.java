package my.edu.um.umpoint.modules.space.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import my.edu.um.umpoint.common.constant.BookingConstant;
import my.edu.um.umpoint.common.page.PageData;
import my.edu.um.umpoint.common.service.impl.CrudServiceImpl;
import my.edu.um.umpoint.modules.payment.dto.SpcPaymentDTO;
import my.edu.um.umpoint.modules.payment.dto.SpcPaymentItemDTO;
import my.edu.um.umpoint.modules.payment.service.SpcPaymentItemService;
import my.edu.um.umpoint.modules.payment.service.SpcPaymentService;
import my.edu.um.umpoint.modules.space.dto.SpcSpaceDTO;
import my.edu.um.umpoint.modules.space.entity.SpcBookingTechnicianEntity;
import my.edu.um.umpoint.modules.space.entity.SpcEventEntity;
import my.edu.um.umpoint.modules.space.service.SpcBookingTechnicianService;
import my.edu.um.umpoint.modules.space.service.SpcEventService;
import my.edu.um.umpoint.modules.space.dao.SpcBookingDao;
import my.edu.um.umpoint.modules.space.dto.SpcBookingDTO;
import my.edu.um.umpoint.modules.space.entity.SpcBookingEntity;
import my.edu.um.umpoint.modules.space.service.SpcBookingService;
import cn.hutool.core.util.StrUtil;
import my.edu.um.umpoint.modules.security.user.SecurityUser;
import my.edu.um.umpoint.modules.security.user.UserDetail;
import my.edu.um.umpoint.modules.space.service.SpcSpaceService;
import my.edu.um.umpoint.modules.utils.BookingUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
    private SpcBookingTechnicianService spcBookingTechnicianService;

    @Autowired
    private SpcEventService spcEventService;

    @Autowired
    private SpcSpaceService spcSpaceService;

    @Autowired
    private SpcPaymentService spcPaymentService;

    @Autowired
    private SpcPaymentItemService spcPaymentItemService;

    @Override
    public QueryWrapper<SpcBookingEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");

        QueryWrapper<SpcBookingEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }

    @Override
    public PageData<SpcBookingDTO> page(Map<String, Object> params) {
        paramsToLike(params, "event");

        IPage<SpcBookingEntity> page = getPage(params, "create_date", false);
        List<SpcBookingEntity> list = baseDao.getList(params);

        return getPageData(list, page.getTotal(), currentDtoClass());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(SpcBookingDTO spcBookingDTO) {
        UserDetail user = SecurityUser.getUser();
        spcBookingDTO.setUserId(user.getId());

        // additional business logic
        // default booking status
        spcBookingDTO.setStatus(BookingConstant.BookingStatus.PENDING.getValue());
        // payment calculation
        SpcSpaceDTO space = spcSpaceService.get(spcBookingDTO.getSpaceId());
        List<SpcPaymentItemDTO> itemisedPrices = BookingUtils.itemisePrice(spcBookingDTO, space);
        BigDecimal total = itemisedPrices.stream()
                                         .map(i -> BigDecimal.valueOf(i.getItemAmount()).multiply(i.getItemPrice()))
                                         .reduce(BigDecimal.ZERO, BigDecimal::add);
        spcBookingDTO.setPaymentAmount(total);

        super.save(spcBookingDTO);

        // object that need id from booking dto
        // daily event breakdown
        List<SpcEventEntity> events = BookingUtils.divideBookingToEvents(spcBookingDTO, space.getSpcBookingRuleDTO());
        for (SpcEventEntity event : events) {
            event.setBookingId(spcBookingDTO.getId());
            event.setType(BookingConstant.EventStatus.BOOKING.getValue());
        }
        spcEventService.insertBatch(events);

        // add payment if required
        if (total.compareTo(BigDecimal.ZERO) != 0) {
            SpcPaymentDTO payment = new SpcPaymentDTO();
            payment.setBookingId(spcBookingDTO.getId());
            payment.setStatus(BookingConstant.PaymentStatus.PENDING.getValue());
            payment.setAmount(total);
            spcPaymentService.save(payment);

            // payment item breakdown
            for (SpcPaymentItemDTO item : itemisedPrices) {
                item.setPaymentId(payment.getId());
                spcPaymentItemService.save(item);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void approve(Long id, List<Long> technicianIdList) {
        UserDetail user = SecurityUser.getUser();
        SpcBookingEntity entity = new SpcBookingEntity();
        entity.setAdminId(user.getId());
        entity.setStatus(BookingConstant.BookingStatus.APPROVED.getValue());

        baseDao.update(entity, new QueryWrapper<SpcBookingEntity>().eq("id", id));

        List<SpcBookingTechnicianEntity> technicianEntityList = technicianIdList
            .stream()
            .map(technicianId -> {
                SpcBookingTechnicianEntity technicianEntity = new SpcBookingTechnicianEntity();
                technicianEntity.setBookingId(id);
                technicianEntity.setTechnicianId(technicianId);
                return technicianEntity;
            }).toList();
        spcBookingTechnicianService.insertBatch(technicianEntityList);
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
        spcBookingTechnicianService.deleteByBookingId(id);
    }
}