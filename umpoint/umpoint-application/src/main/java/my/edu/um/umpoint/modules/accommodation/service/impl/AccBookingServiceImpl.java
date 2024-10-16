package my.edu.um.umpoint.modules.accommodation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import my.edu.um.umpoint.common.constant.BookingConstant;
import my.edu.um.umpoint.common.page.PageData;
import my.edu.um.umpoint.common.service.impl.CrudServiceImpl;
import my.edu.um.umpoint.common.utils.DateUtils;
import my.edu.um.umpoint.modules.accommodation.dao.AccBookingDao;
import my.edu.um.umpoint.modules.accommodation.dto.AccAccommodationDTO;
import my.edu.um.umpoint.modules.accommodation.dto.AccBookingDTO;
import my.edu.um.umpoint.modules.accommodation.dto.AccBookingRuleDTO;
import my.edu.um.umpoint.modules.accommodation.entity.AccBookingEntity;
import my.edu.um.umpoint.modules.accommodation.entity.AccBookingTechnicianEntity;
import my.edu.um.umpoint.modules.accommodation.service.AccAccommodationService;
import my.edu.um.umpoint.modules.accommodation.service.AccBookingService;
import cn.hutool.core.util.StrUtil;
import my.edu.um.umpoint.modules.accommodation.service.AccBookingTechnicianService;
import my.edu.um.umpoint.modules.accommodation.service.AccEventService;
import my.edu.um.umpoint.modules.payment.dto.AccPaymentItemDTO;
import my.edu.um.umpoint.modules.payment.dto.SpcPaymentDTO;
import my.edu.um.umpoint.modules.payment.dto.SpcPaymentItemDTO;
import my.edu.um.umpoint.modules.security.user.SecurityUser;
import my.edu.um.umpoint.modules.security.user.UserDetail;
import my.edu.um.umpoint.modules.space.dto.SpcSpaceDTO;
import my.edu.um.umpoint.modules.utils.EventEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Accommodation Booking
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-09-08
 */
@Service
public class AccBookingServiceImpl extends CrudServiceImpl<AccBookingDao, AccBookingEntity, AccBookingDTO> implements AccBookingService {

    @Autowired
    private AccBookingTechnicianService accBookingTechnicianService;

    @Autowired
    private AccEventService accEventService;

    @Autowired
    private AccAccommodationService accAccommodationService;

    @Override
    public QueryWrapper<AccBookingEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<AccBookingEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }

    @Override
    public PageData<AccBookingDTO> page(Map<String, Object> params) {
        UserDetail user = SecurityUser.getUser();
        if (user.getSuperAdmin() == null)
            params.put("userId", user.getId());
        paramsToLike(params, "accommodation");
        paramsToLike(params, "event");

        IPage<AccBookingEntity> page = getPage(params, "create_date", false);
        List<AccBookingEntity> list = baseDao.getList(params);

        return getPageData(list, page.getTotal(), currentDtoClass());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(AccBookingDTO accBookingDTO) {
        UserDetail user = SecurityUser.getUser();
        accBookingDTO.setUserId(user.getId());

        // additional business logic
        // default booking status
        accBookingDTO.setStatus(BookingConstant.BookingStatus.PENDING.getValue());
        // payment calculation
        AccAccommodationDTO accommodation = accAccommodationService.get(accBookingDTO.getAccommodationId());
        AccBookingRuleDTO accommodationBookingRule = accommodation.getAccBookingRuleDTO();

        LocalDate startDay = DateUtils.convertDateToLocalDate(accBookingDTO.getStartDay());
        LocalDate endDay = DateUtils.convertDateToLocalDate(accBookingDTO.getEndDay());
        long diffDays = ChronoUnit.DAYS.between(startDay, endDay);
        List<AccPaymentItemDTO> itemisedPrices = itemiseBookingPrice(diffDays, accommodation);

        BigDecimal total = itemisedPrices.stream()
                                         .map(i -> BigDecimal.valueOf(i.getItemAmount()).multiply(i.getItemPrice()))
                                         .reduce(BigDecimal.ZERO, BigDecimal::add);
        accBookingDTO.setPaymentAmount(total);

        super.save(accBookingDTO);

        // object that need id from booking dto
        // daily event breakdown
        accEventService.addEvent(accBookingDTO);

        // add payment if required
        // TODO: Waiting Payment Class
//        if (total.compareTo(BigDecimal.ZERO) != 0) {
//            AccPaymentDTO payment = new AccPaymentDTO();
//            payment.setBookingId(accBookingDTO.getId());
//            payment.setStatus(BookingConstant.PaymentStatus.PENDING.getValue());
//            payment.setAmount(total);
//            spcPaymentService.save(payment);
//
//            // payment item breakdown
//            for (AccPaymentItemDTO item : itemisedPrices) {
//                item.setPaymentId(payment.getId());
//                spcPaymentItemService.save(item);
//            }
//        }


    }

    private static List<AccPaymentItemDTO> itemiseBookingPrice(long diffDays, AccAccommodationDTO accommodation){
        long weekCount = Math.floorDiv(diffDays, 7);
        int remainingDayCount = Math.floorMod(diffDays, 7);
        List<AccPaymentItemDTO> itemisedPrices = new ArrayList<>();
        if (accommodation.getWeekPrice() != null && weekCount > 0){
            AccPaymentItemDTO weekItem = new AccPaymentItemDTO();
            weekItem.setItemName("Weekly price");
            weekItem.setItemAmount((int) weekCount);
            weekItem.setItemPrice(accommodation.getWeekPrice());
            itemisedPrices.add(weekItem);

            AccPaymentItemDTO dayItem = new AccPaymentItemDTO();
            dayItem.setItemName("Daily price");
            dayItem.setItemAmount(remainingDayCount);
            dayItem.setItemPrice(accommodation.getDayPrice());
            itemisedPrices.add(dayItem);
        } else {
            AccPaymentItemDTO dayItem = new AccPaymentItemDTO();
            dayItem.setItemName("Daily price");
            dayItem.setItemAmount((int) diffDays);
            dayItem.setItemPrice(accommodation.getDayPrice());
            itemisedPrices.add(dayItem);
        }
        return itemisedPrices;
    }

    @Override
    public void approve(Long id, List<Long> technicianIdList) {
        UserDetail user = SecurityUser.getUser();
        AccBookingEntity entity = new AccBookingEntity();
        entity.setAdminId(user.getId());
        entity.setStatus(BookingConstant.BookingStatus.APPROVED.getValue());

        baseDao.update(entity, new QueryWrapper<AccBookingEntity>().eq("id",id));

        List<AccBookingTechnicianEntity> technicianEntityList = technicianIdList
                .stream()
                .map(technicianId -> {
                    AccBookingTechnicianEntity technicianEntity = new AccBookingTechnicianEntity();
                    technicianEntity.setBookingId(id);
                    technicianEntity.setTechnicianId(technicianId);
                    return technicianEntity;
                }).toList();
        accBookingTechnicianService.insertBatch(technicianEntityList);
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
        accBookingTechnicianService.deleteByBookingId(id);
    }
}