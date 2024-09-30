import api from "@/utils/api";

function getFacilityInformation(facilityID) {
    return {
        id: facilityID,
        name: "Chancellor Room",
        faculty: "Faculty of Engineering",
        spcBookingRuleDTO: {
            startTime: "08:00",
            endTime: "18:00",
            holidayAvailable: 1,
            open_days_prior_booking: 60,
            close_days_after_booking: 5,
            max_reservation_days: 30,
            min_booking_hours: 1,
        },
    };

    // return api.get(`/facility/${facilityID}`);
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
    getFacilityInformation,
    getFacilityAvailability,
    validateBooking,
    getCurrentUserBookings,
};
