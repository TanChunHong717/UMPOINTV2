package my.edu.um.umpoint.modules.utils;

import my.edu.um.umpoint.common.constant.BookingConstant;
import my.edu.um.umpoint.common.utils.DateUtils;
import my.edu.um.umpoint.modules.payment.dto.SpcPaymentItemDTO;
import my.edu.um.umpoint.modules.space.dto.SpcBookingDTO;
import my.edu.um.umpoint.modules.space.dto.SpcBookingRuleDTO;
import my.edu.um.umpoint.modules.space.dto.SpcSpaceDTO;
import my.edu.um.umpoint.modules.space.entity.SpcEventEntity;

import java.sql.Time;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class SpaceBookingUtils{

    public static List<EventEntity> dividePeriodToEvents(
        Date startDate, Date endDate, Time startTime, Time endTime
    ){
        List<EventEntity> eventEntityList = new ArrayList<>();

        LocalDate currentDay = DateUtils.convertDateToLocalDate(startDate);
        LocalDate endDay = DateUtils.convertDateToLocalDate(endDate);
        while (currentDay.isBefore(endDay) || currentDay.isEqual(endDay)) {
            EventEntity eventEntity = new EventEntity();

            eventEntity.startTime = currentDay.atTime(startTime.toLocalTime());
            eventEntity.endTime = currentDay.atTime(endTime.toLocalTime());

            eventEntityList.add(eventEntity);
            currentDay = currentDay.plusDays(1);
        }

        return eventEntityList;
    }

    public static List<SpcEventEntity> divideBookingToEvents(SpcBookingDTO bookingDTO, boolean holidayAvailable){
        List<EventEntity> eventEntities = dividePeriodToEvents(
            bookingDTO.getStartDay(),
            bookingDTO.getEndDay(),
            bookingDTO.getStartTime(),
            bookingDTO.getEndTime()
        );
        List<SpcEventEntity> spcEventEntities = new ArrayList<>();
        for (EventEntity event : eventEntities) {
            if (!holidayAvailable && (
                event.startTime.getDayOfWeek().equals(DayOfWeek.SATURDAY) ||
                event.startTime.getDayOfWeek().equals(DayOfWeek.SUNDAY)
            )) {
                // skip saturday and sunday if not available
                continue;
            }
            SpcEventEntity spcEventEntity = new SpcEventEntity();
            spcEventEntity.setSpaceId(bookingDTO.getSpaceId());
            spcEventEntity.setStartTime(DateUtils.convertLocalDateTimeToDate(event.startTime));
            spcEventEntity.setEndTime(DateUtils.convertLocalDateTimeToDate(event.endTime));
            spcEventEntity.setBookingId(bookingDTO.getId());
            spcEventEntity.setType(BookingConstant.EventStatus.BOOKING.getValue());
            spcEventEntities.add(spcEventEntity);
        }
        return spcEventEntities;
    }

    public static List<SpcPaymentItemDTO> itemisePrice(SpcBookingDTO bookingInfo, SpcSpaceDTO spaceInfo){
        SpcBookingRuleDTO spaceBookingRule = spaceInfo.getSpcBookingRuleDTO();

        // break booking into daily events and sum price
        List<SpcEventEntity> eventEntities =
            divideBookingToEvents(bookingInfo, spaceBookingRule.getHolidayAvailable() == BookingConstant.Holiday.AVAILABLE.getValue());
        List<SpcPaymentItemDTO> eventPrices = itemiseDailyEventPrices(eventEntities, spaceInfo);

        List<SpcPaymentItemDTO> detailedItems = new ArrayList<>(eventPrices);
        // calculate technician price if available
        if (bookingInfo.getTechnicianNumber() != 0) {
            // technician price
            String technicianDisplay = "Technician price";

            SpcPaymentItemDTO fullDayPaymentItem = new SpcPaymentItemDTO();
            fullDayPaymentItem.setItemName(technicianDisplay);
            fullDayPaymentItem.setItemPrice(spaceBookingRule.getTechnicianPrice());
            fullDayPaymentItem.setItemAmount(bookingInfo.getTechnicianNumber());
            detailedItems.add(fullDayPaymentItem);
        }

        return detailedItems;
    }

    public static List<SpcPaymentItemDTO> itemiseDailyEventPrices(
        List<SpcEventEntity> eventEntities,
        SpcSpaceDTO spaceInfo
    ){
        List<SpcPaymentItemDTO> detailedItems = new LinkedList<>();

        SpcBookingRuleDTO spaceBookingRule = spaceInfo.getSpcBookingRuleDTO();
        for (SpcEventEntity eventEntity : eventEntities) {
            // full day price
            if (
                // only if set by admin
                spaceInfo.getDayPrice() != null &&
                DateUtils.convertDateToLocalTime(eventEntity.getStartTime())
                         .equals(spaceBookingRule.getStartTime().toLocalTime()) &&
                DateUtils.convertDateToLocalTime(eventEntity.getEndTime())
                         .equals(spaceBookingRule.getEndTime().toLocalTime())
            ) {
                String displayDate =
                    "Full day price for " + DateUtils.convertDateToLocalDate(eventEntity.getStartTime())
                                                     .format(DateTimeFormatter.ISO_DATE);

                SpcPaymentItemDTO fullDayPaymentItem = new SpcPaymentItemDTO();
                fullDayPaymentItem.setItemName(displayDate);
                fullDayPaymentItem.setItemPrice(spaceInfo.getDayPrice());
                fullDayPaymentItem.setItemAmount(1);
                detailedItems.add(fullDayPaymentItem);

                // no need to calculate per hour price
                continue;
            }

            // not full day price
            Duration length =
                Duration.between(eventEntity.getStartTime().toInstant(), eventEntity.getEndTime().toInstant());
            // check if location have 4 hours price
            if (spaceInfo.getFourHoursPrice() != null) {
                // only if set by admin
                // raw division is rounded down
                int fourHoursCount = (int) (length.toHours() / 4);
                int oneHourCount = (int) (length.toHours() - (4 * fourHoursCount));
                if (length.toMinutes() % 60 != 0) {
                    // there is additional hours not counted in, round up
                    oneHourCount += 1;
                }
                // calculate price
                String fourHoursDisplayDate =
                    "4 hours price for " + DateUtils.convertDateToLocalDate(eventEntity.getStartTime())
                                                    .format(DateTimeFormatter.ISO_DATE);
                SpcPaymentItemDTO fourHoursPaymentItem = new SpcPaymentItemDTO();
                fourHoursPaymentItem.setItemName(fourHoursDisplayDate);
                fourHoursPaymentItem.setItemPrice(spaceInfo.getFourHoursPrice());
                fourHoursPaymentItem.setItemAmount(fourHoursCount);
                detailedItems.add(fourHoursPaymentItem);

                String oneHourDisplayDate =
                    "1 hour price for " + DateUtils.convertDateToLocalDate(eventEntity.getStartTime())
                                                   .format(DateTimeFormatter.ISO_DATE);
                SpcPaymentItemDTO oneHourPaymentItem = new SpcPaymentItemDTO();
                oneHourPaymentItem.setItemName(oneHourDisplayDate);
                oneHourPaymentItem.setItemPrice(spaceInfo.getHourPrice());
                oneHourPaymentItem.setItemAmount(oneHourCount);
                detailedItems.add(oneHourPaymentItem);
            } else {
                // does not have 4 hours count, use only 1 hour
                int oneHourCount = (int) Math.ceil(length.toMinutes() / 60.0);
                String oneHourDisplayDate =
                    "1 hour price for " + DateUtils.convertDateToLocalDate(eventEntity.getStartTime())
                                                   .format(DateTimeFormatter.ISO_DATE);

                SpcPaymentItemDTO oneHourPaymentItem = new SpcPaymentItemDTO();
                oneHourPaymentItem.setItemName(oneHourDisplayDate);
                oneHourPaymentItem.setItemPrice(spaceInfo.getHourPrice());
                oneHourPaymentItem.setItemAmount(oneHourCount);
                detailedItems.add(oneHourPaymentItem);
            }
        }
        return detailedItems;
    }
}

