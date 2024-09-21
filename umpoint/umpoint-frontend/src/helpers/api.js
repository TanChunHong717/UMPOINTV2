import axios from 'axios';

const api = axios.create({
    baseURL: import.meta.env.BOOKING_API_URL,
    timeout: 1000,
    headers: {'X-Custom-Header': 'foobar'}
})

function getFacilityInformation(facilityID) {
    return {id:facilityID, name:'Chancellor Room', faculty:'Faculty of Engineering'};

    // return api.get(`/facility/${facilityID}`);
}

function getFacilityAvailability(facilityID, date) {
    return api.get(`/facility/${facilityID}/availability`, {
        data: {
            date: date
        }
    });
}

function validateBooking(data) {
    return api.post(`/booking`, {
        data: data,
    });
}

export {
    getFacilityInformation,
    getFacilityAvailability,
    validateBooking,
    
};