package my.edu.um.umpoint.modules.space.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import my.edu.um.umpoint.common.annotation.DataFilter;
import my.edu.um.umpoint.common.constant.BookingConstant;
import my.edu.um.umpoint.common.constant.Constant;
import my.edu.um.umpoint.common.page.PageData;
import my.edu.um.umpoint.common.service.impl.CrudServiceImpl;
import my.edu.um.umpoint.common.utils.ConvertUtils;
import my.edu.um.umpoint.common.utils.DateUtils;
import my.edu.um.umpoint.modules.client.entity.CliUserEntity;
import my.edu.um.umpoint.modules.payment.dto.SpcPaymentDTO;
import my.edu.um.umpoint.modules.payment.dto.SpcPaymentItemDTO;
import my.edu.um.umpoint.modules.payment.entity.SpcPaymentEntity;
import my.edu.um.umpoint.modules.payment.service.SpcPaymentItemService;
import my.edu.um.umpoint.modules.payment.service.SpcPaymentService;
import my.edu.um.umpoint.modules.security.service.ShiroService;
import my.edu.um.umpoint.modules.security.user.SecurityUser;
import my.edu.um.umpoint.modules.security.user.UserDetail;
import my.edu.um.umpoint.modules.space.dao.SpcBookingDao;
import my.edu.um.umpoint.modules.space.dao.SpcEventDao;
import my.edu.um.umpoint.modules.space.dto.SpcBookingAttachmentDTO;
import my.edu.um.umpoint.modules.space.dto.SpcBookingDTO;
import my.edu.um.umpoint.modules.space.dto.SpcClientBookingDTO;
import my.edu.um.umpoint.modules.space.dto.SpcSpaceDTO;
import my.edu.um.umpoint.modules.space.entity.SpcBookingEntity;
import my.edu.um.umpoint.modules.space.entity.SpcBookingTechnicianEntity;
import my.edu.um.umpoint.modules.space.entity.SpcEventEntity;
import my.edu.um.umpoint.modules.space.entity.SpcSpaceEntity;
import my.edu.um.umpoint.modules.space.service.*;
import my.edu.um.umpoint.modules.thirdparty.entity.EmailDetails;
import my.edu.um.umpoint.modules.thirdparty.service.EmailService;
import my.edu.um.umpoint.modules.utils.EventEntity;
import my.edu.um.umpoint.modules.utils.SpaceBookingUtils;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * Space Booking
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-09-08
 */
@Service
public class SpcBookingServiceImpl extends CrudServiceImpl<SpcBookingDao, SpcBookingEntity, SpcBookingDTO> implements SpcBookingService{

    @Autowired
    private SpcBookingTechnicianService spcBookingTechnicianService;

    @Autowired
    private SpcEventService spcEventService;

    @Autowired
    private SpcSpaceService spcSpaceService;

    @Autowired
    private SpcBookingAttachmentService spcBookingAttachmentService;

    @Autowired
    private SpcPaymentService spcPaymentService;

    @Autowired
    private SpcPaymentItemService spcPaymentItemService;

    @Autowired
    private SpcEventDao spcEventDao;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ShiroService userInformationService;

    @Override
    public QueryWrapper<SpcBookingEntity> getWrapper(Map<String, Object> params){
        String id = (String) params.get("id");

        QueryWrapper<SpcBookingEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }

    @Override
    @DataFilter(tableAlias = "s")
    public PageData<SpcBookingDTO> page(Map<String, Object> params){
        UserDetail user = SecurityUser.getUser();
        if (user.getSuperAdmin() == null)
            params.put("userId", user.getId());
        paramsToLike(params, "space");
        paramsToLike(params, "event");

        IPage<SpcBookingEntity> page = getPage(params, "create_date", false);
        List<SpcBookingEntity> list = baseDao.getList(params);

        return getPageData(list, page.getTotal(), currentDtoClass());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(SpcBookingDTO spcBookingDTO){
        UserDetail user = SecurityUser.getUser();
        spcBookingDTO.setUserId(user.getId());

        // additional business logic
        // default booking status
        spcBookingDTO.setStatus(BookingConstant.BookingStatus.PENDING.getValue());
        // payment calculation
        SpcSpaceDTO space = spcSpaceService.get(spcBookingDTO.getSpaceId());
        List<SpcPaymentItemDTO> itemisedPrices = SpaceBookingUtils.itemisePrice(spcBookingDTO, space);
        BigDecimal total = itemisedPrices.stream()
                                         .map(i -> BigDecimal.valueOf(i.getItemAmount()).multiply(i.getItemPrice()))
                                         .reduce(BigDecimal.ZERO, BigDecimal::add);
        spcBookingDTO.setPaymentAmount(total);

        // check if can automatically approve
        boolean automaticApprove = space.getSpcBookingRuleDTO().getApprovalRequired() == 0;
        if (automaticApprove) {
            if (total.compareTo(BigDecimal.ZERO) > 0) {
                spcBookingDTO.setStatus(BookingConstant.BookingStatus.APPROVED.getValue());
            } else {
                spcBookingDTO.setStatus(BookingConstant.BookingStatus.COMPLETED.getValue());
            }
        }

        // save to db and refresh ID
        super.save(spcBookingDTO);

        // insert object that need id from booking dto
        // daily event breakdown
        spcEventService.addEvent(spcBookingDTO, space.getSpcBookingRuleDTO().getHolidayAvailable() ==
                                                BookingConstant.Holiday.AVAILABLE.getValue());

        // add attachments if required
        if (spcBookingDTO.getSpcBookingAttachmentDTOList() != null &&
            !spcBookingDTO.getSpcBookingAttachmentDTOList().isEmpty()) {
            for (SpcBookingAttachmentDTO item : spcBookingDTO.getSpcBookingAttachmentDTOList()) {
                item.setBookingId(spcBookingDTO.getId());
                spcBookingAttachmentService.save(item);
            }
        }

        // add payment if required
        if (total.compareTo(BigDecimal.ZERO) > 0) {
            SpcPaymentDTO payment = new SpcPaymentDTO();
            payment.setBookingId(spcBookingDTO.getId());
            payment.setStatus(BookingConstant.PaymentStatus.PENDING.getValue());
            payment.setAmount(total);
            payment.setCreatedAt(new Date());
            spcPaymentService.save(payment);

            // payment item breakdown
            for (SpcPaymentItemDTO item : itemisedPrices) {
                item.setPaymentId(payment.getId());
                spcPaymentItemService.save(item);
            }
        }

        // send mail notification
        FutureTask<Void> futureTask = new FutureTask<Void>(() -> {
            emailService.sendSimpleMail(
                new EmailDetails(
                    space.getManagerEmail(),
                    String.format(
                        "A new booking has been created for %s at %s. Please visit the panel to take further action. \n\n" +
                        spcBookingDTO.toString(),
                        space.getName(),
                        (new SimpleDateFormat("dd MMM yyyy, HH:mm:ss z")).format(spcBookingDTO.getCreateDate())
                    ),
                    "New Booking For " + space.getName()
                )
            );
            return null;
        });
        // Start the task in a new thread
        new Thread(futureTask).start();

        this.sendEmailToCustomer(
            ConvertUtils.sourceToTarget(user, CliUserEntity.class),
            "New booking for ${spaceName}",
            "Your booking for ${spaceName} has been created at ${currentTime}. Please see details below.",
            spcBookingDTO,
            space
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void approve(Long id, List<Long> technicianIdList){
        UserDetail user = SecurityUser.getUser();
        SpcBookingEntity entity = new SpcBookingEntity();
        entity.setAdminId(user.getId());
        entity.setStatus(BookingConstant.BookingStatus.APPROVED.getValue());

        baseDao.update(entity, new QueryWrapper<SpcBookingEntity>().eq("id", id));

        if (technicianIdList != null) {
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

        this.sendEmailToCustomer(
            "Booking ${eventName} is approved",
            "Your following booking for ${spaceName} has been approved at ${currentTime}.",
            id
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reject(Long id){
        UserDetail user = SecurityUser.getUser();
        SpcBookingEntity entity = new SpcBookingEntity();
        entity.setAdminId(user.getId());
        entity.setStatus(BookingConstant.BookingStatus.REJECT.getValue());

        baseDao.update(entity, new QueryWrapper<SpcBookingEntity>().eq("id", id));
        spcEventService.deleteByBookingId(id);

        this.sendEmailToCustomer(
            "Booking ${eventName} is rejected",
            "Your following booking for ${spaceName} has been rejected at ${currentTime}. Payment will be refunded " +
            "shortly.",
            id
        );
    }

    @Override
    public void reject(List<Long> idList){
        UserDetail user = SecurityUser.getUser();
        SpcBookingEntity entity = new SpcBookingEntity();
        entity.setAdminId(user.getId());
        entity.setStatus(BookingConstant.BookingStatus.REJECT.getValue());

        baseDao.update(entity, new QueryWrapper<SpcBookingEntity>().in("id", idList));
        spcEventService.deleteByBookingId(idList);

        for (Long id : idList) {
            this.sendEmailToCustomer(
                "Booking ${eventName} is rejected",
                "Your following booking for ${spaceName} has been rejected at ${currentTime}. Payment will be " +
                "refunded shortly.",
                id
            );
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancel(Long id){
        SpcBookingEntity entity = new SpcBookingEntity();
        entity.setStatus(BookingConstant.BookingStatus.CANCELLED.getValue());

        baseDao.update(entity, new QueryWrapper<SpcBookingEntity>().eq("id", id));
        spcEventService.deleteByBookingId(id);
        spcBookingTechnicianService.deleteByBookingId(id);

        if (entity.getPaymentAmount() != null && entity.getPaymentAmount().compareTo(BigDecimal.ZERO) > 0) {
            spcPaymentService.refund(
                spcPaymentService.getLatestPayment(entity.getId())
                                 .getId()
            );
        }

        this.sendEmailToCustomer(
            "Booking ${eventName} is cancelled",
            "Your following booking for ${spaceName} has been cancelled at ${currentTime}. Payment will be refunded " +
            "shortly.",
            id
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void pay(Long id){
        // update main booking
        SpcBookingEntity entity = new SpcBookingEntity();
        entity.setStatus(BookingConstant.BookingStatus.COMPLETED.getValue());
        baseDao.update(entity, new QueryWrapper<SpcBookingEntity>().eq("id", id));

        SpcPaymentEntity payment = spcPaymentService.getLatestPayment(id);
        // update payment done
        spcPaymentService.pay(payment.getId());


        this.sendEmailToCustomer(
            "Payment for booking ${eventName}",
            "Your following booking for ${spaceName} has been paid at ${currentTime}.",
            id
        );
    }

    @Override
    public Long getUserId(Long id){
        QueryWrapper<SpcBookingEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("id", id);
        wrapper.select("user_id");
        return baseDao.selectOne(wrapper).getUserId();
    }

    @Override
    public void validateBookingHasOverlap(SpcClientBookingDTO request) throws DateTimeException{
        DateTimeFormatter sqlDateDormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        List<SpcEventEntity> sameDateRangeEvents = spcEventDao.getEventsBetweenTimeSpan(
            request.getSpaceId(),
            DateUtils.convertDateTimeToLocalDateTime(request.getStartDay(), request.getStartTime())
                     .format(sqlDateDormatter),
            DateUtils.convertDateTimeToLocalDateTime(request.getEndDay(), request.getEndTime())
                     .format(sqlDateDormatter)
        );

        for (EventEntity currentEvent : SpaceBookingUtils.dividePeriodToEvents(
            request.getStartDay(), request.getEndDay(), request.getStartTime(), request.getEndTime()
        )) {
            List<SpcEventEntity> overlappedEvents = sameDateRangeEvents
                .stream()
                .filter((overlapEvent) -> {
                    LocalDateTime overlapStart = DateUtils.convertDateToLocalDateTime(overlapEvent.getStartTime());
                    LocalDateTime overlapEnd = DateUtils.convertDateToLocalDateTime(overlapEvent.getEndTime());
                    return !(
                        // if overlapStart - overlapEnd - currentStart (- currentEnd) then doesnt overlap
                        (currentEvent.startTime.isAfter(overlapStart) && currentEvent.startTime.isAfter(overlapEnd)) ||
                        // if (currentStart -) currentEnd - overlapStart - overlapEnd then doesnt overlap
                        (currentEvent.endTime.isBefore(overlapStart) && currentEvent.endTime.isBefore(overlapEnd))
                    ); // otherwise event is overlapping
                })
                .toList();
            if (!overlappedEvents.isEmpty()) {
                throw new DateTimeException("Booking overlapped");
            }
        }
    }

    public void sendEmailToCustomer(
        String title, String messageFormat, Long bookingId
    ){
        SpcBookingEntity bookingEntity = baseDao.selectById(bookingId);
        SpcSpaceEntity spaceEntity = spcSpaceService.selectById(bookingEntity.getSpaceId());

        CliUserEntity user = userInformationService.getCliUser(bookingEntity.getUserId());
        sendEmailToCustomer(
            user,
            title,
            messageFormat,
            ConvertUtils.sourceToTarget(bookingEntity, SpcBookingDTO.class),
            ConvertUtils.sourceToTarget(spaceEntity, SpcSpaceDTO.class)
        );
    }

    public void sendEmailToCustomer(
        CliUserEntity user, String title, String messageFormat,
        SpcBookingDTO booking, SpcSpaceDTO space
    ){
        if (
            user.getReceiveEmail() == Constant.EmailNotification.RECEIVE.getValue()
        ) {
            // prebuild message info
            Map<String, Object> replacementMap = Map.of(
                "spaceName", space.getName(),
                "bookingId", booking.getId(),
                "eventName", booking.getEvent(),
                "date", DateUtils.format(booking.getCreateDate(), DateUtils.TEXT_DATE_TIME_PATTERN),
                "amount", booking.getPaymentAmount(),
                "currentTime", DateUtils.format(new Date(), DateUtils.TEXT_DATE_TIME_PATTERN)
            );

            FutureTask<Void> futureTask = new FutureTask<Void>(() -> {
                emailService.sendSimpleMail(
                    new EmailDetails(
                        user.getEmail(),
                        StrSubstitutor.replace(messageFormat, replacementMap) + "\n\n" + booking.toString(),
                        StrSubstitutor.replace(title, replacementMap)
                    )
                );
                return null;
            });

            // Start the task in a new thread
            new Thread(futureTask).start();
        }
    }
}