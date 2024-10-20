import {
    formatDateToTimezoneDateStr,
    formatDateToTimezoneTimeStr,
    addDays,
} from "@/utils/date";
import { VueCalEvent } from "@/types/interface";

function breakBookingByDays(
    startDate: Date,
    endDate: Date,
    startTime: string,
    endTime: string,
    isHolidayAvailable: boolean = true
): VueCalEvent[] {
    let returnList = [];
    for (
        let d = new Date(startDate);
        d <= endDate;
        d.setDate(d.getDate() + 1)
    ) {
        if (!isHolidayAvailable && (d.getDay() == 0 || d.getDay() == 6)) {
            continue;
        }
        let currStartDateTime = new Date(d);
        currStartDateTime.setHours(
            Number(startTime.split(":")[0]),
            Number(startTime.split(":")[1])
        );
        let currEndDateTime = new Date(d);
        currEndDateTime.setHours(
            Number(endTime.split(":")[0]),
            Number(endTime.split(":")[1])
        );

        returnList.push({
            start: currStartDateTime,
            end: currEndDateTime,
        });
    }

    return returnList;
}

function itemiseDailyEventPrices(facilityInfo: any, startDate: Date, endDate: Date, startTime: string, endTime: string, isHolidayAvailable: boolean = true) {
    let resultArr = [];

    let eventDays = breakBookingByDays(startDate, endDate, startTime, endTime, isHolidayAvailable);
    let dayCount = eventDays.length;

    for (let {
        start: eventStartDate,
        end: eventEndDate,
    } of eventDays) {
        // price logic
        if (
            facilityInfo.dayPrice &&
            formatDateToTimezoneTimeStr(eventStartDate) ===
            facilityInfo.bookingRule.startTime.substr(0, 5) &&
            formatDateToTimezoneTimeStr(eventEndDate) ===
            facilityInfo.bookingRule.endTime.substr(0, 5)
        ) {
            dayCount++;
            continue;
        }

        let hourLength =
            Math.abs(eventEndDate.getTime() - eventStartDate.getTime()) / (60 * 60 * 1000);
        if (facilityInfo.fourHoursPrice) {
            let fourHoursCount = Math.floor(hourLength / 4);
            let oneHourCount = Math.ceil(hourLength % 4);
            if (fourHoursCount) {
                resultArr.push({
                    item: `${fourHoursCount} * 4 hours (${formatDateToTimezoneDateStr(
                        eventStartDate
                    )})`,
                    amount: fourHoursCount,
                    price: facilityInfo.fourHoursPrice,
                    total: facilityInfo.fourHoursPrice * fourHoursCount,
                });
            }
            if (oneHourCount) {
                resultArr.push({
                    item: `${oneHourCount} hours (${formatDateToTimezoneDateStr(
                        eventStartDate
                    )})`,
                    amount: oneHourCount,
                    price: facilityInfo.hourPrice,
                    total: facilityInfo.hourPrice * oneHourCount,
                });
            }
        } else {
            let oneHourCount = Math.ceil(hourLength);
            resultArr.push({
                item: `${oneHourCount} hours (${formatDateToTimezoneDateStr(
                    eventStartDate
                )})`,
                amount: oneHourCount,
                price: facilityInfo.hourPrice,
                total: facilityInfo.hourPrice * oneHourCount,
            });
        }
    }

    if (dayCount) {
        resultArr.unshift({
            item: `Full day`,
            amount: dayCount,
            price: facilityInfo.dayPrice,
            total: dayCount * facilityInfo.dayPrice,
        });
    }

    return resultArr;
}

export { itemiseDailyEventPrices, breakBookingByDays };
