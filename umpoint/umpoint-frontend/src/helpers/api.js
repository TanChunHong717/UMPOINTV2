import api from "@/utils/api";
import { facilityTypes } from "@/constants/app";

function getFacilities(type) {
    if (facilityTypes[type] === undefined) {
        throw new Error("Invalid facility type");
    }
    return api.get('/space/space/page');
}

function getFacilityInformation(facilityID) {
    // TODO: PLACEHOLDER
    if (typeof facilityID == "number" && facilityID < 100) {
        return {
            "id": facilityID,
            "status": 1,
            "name": "Chancellor Room",
            "catId": "1841705487032270849",
            "deptId": "1841705636508876802",
            "address": "1234",
            "description": "<p>sadadasadsdasdas</p>",
            "facilities": "rewerwe",
            "capacity": 30,
            "manager": null,
            "hourPrice": null,
            "fourHoursPrice": null,
            "dayPrice": null,
            "bookingRuleId": null,
            "creator": "1067246875800000001",
            "createDate": "2024-10-03 13:04:16",
            "updater": "1067246875800000001",
            "updateDate": "2024-10-03 13:04:16",
            "category": "football",
            "deptName": "Faculty of Engineering",
            "managerName": null,
            "creatorName": "admin",
            "updaterName": "admin",
            "spcImageDTOList": [],
            "spcTagDTOList": [
                {
                    "id": "1841764876103708673",
                    "tagName": "temp",
                    "spaceCount": null
                }
            ],
            "spcBookingRuleDTO": {
                startTime: "08:00",
                endTime: "18:00",
                holidayAvailable: 1,
                open_days_prior_booking: 60,
                close_days_after_booking: 5,
                max_reservation_days: 30,
                min_booking_hours: 1,
            },
        }
    }

    return api.get(`/space/space/${facilityID}`);
}

function getFacilityAvailability(facilityID, date) {
    return api.get(`/facility/${facilityID}/availability`, {
        data: {
            date: date,
        },
    });
}

function validateBooking(data) {
    return api.post(`/booking`, {
        data: data,
    });
}

function getCurrentUserBookings() {
    return [
        {}
    ];
    return api.get(`/user/bookings`, {
        data: {
            userID: id,
        },
    });
}

export {
    getFacilities,
    getFacilityInformation,
    getFacilityAvailability,
    validateBooking,
    getCurrentUserBookings,
};
