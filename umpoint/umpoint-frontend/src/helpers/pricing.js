import {
    formatDateToTimezoneDateStr,
    formatDateToTimezoneTimeStr,
    addDays,
} from "@/utils/date";

function itemiseDailyEventPrices(facilityInfo, startDate, endDate) {
    let resultArr = [];

    let currDate = new Date(startDate);
    let dayCount = 0;
    let eventDays = [];
    while (currDate <= endDate) {
        let currEndDate = new Date(currDate);
        currEndDate.setHours(
            Number(formatDateToTimezoneTimeStr(endDate).split(":")[0]),
            Number(formatDateToTimezoneTimeStr(endDate).split(":")[1]),
            0,
            0
        );
        eventDays.push({
            startDate: new Date(currDate),
            endDate: new Date(currEndDate),
        });
        currDate = addDays(currDate, 1);
    }

    for (let {
        startDate: eventStartDate,
        endDate: eventEndDate,
    } of eventDays) {
        // price logic
        if (
            facilityInfo.dayPrice &&
            formatDateToTimezoneTimeStr(eventStartDate) ===
                facilityInfo.spcBookingRuleDTO.startTime.substr(0, 5) &&
            formatDateToTimezoneTimeStr(eventEndDate) ===
                facilityInfo.spcBookingRuleDTO.endTime.substr(0, 5)
        ) {
            dayCount++;
            continue;
        }

        let hourLength =
            Math.abs(eventEndDate - eventStartDate) / (60 * 60 * 1000);
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

export { itemiseDailyEventPrices };
