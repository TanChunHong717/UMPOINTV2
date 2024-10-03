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
            "description": `Before completing your booking on UMPoint, please contact
the staff in charge regarding your event: 

WAN NUR ANI BINTI WAN HAR (Assistant Registrar) 
Email : wannurani_@um.edu.my
Tel: +603-79673056 or 

Mr. Zaid bin Idris (Assistant Engineer) 
Email: yed@um.edu.my 
Tel: +603-79673056 

Staf teknikal membantu dalam urusan berikut: Membuka/menutup
pintu ruang/suis elektrik (lampu/aircond) Peralatan
ICT/elektrik/mekanikal/perabot tersedia untuk digunakan
Menyelesaikan aduan berkaitan kerja teknikal dalam tempoh
ruang digunakan Technical staff assist in the following:
Opening / closing the door / electric switch door (lamp /
aircond) ICT / electrical / mechanical / furniture equipment
is available for use Solve technical work complaints within
the space used`,
            "facilities": "rewerwe",
            "capacity": 30,
            "manager": null,
            "hourPrice": 200,
            "fourHoursPrice": 700,
            "dayPrice": 1000,
            "bookingRuleId": null,
            "creator": "1067246875800000001",
            "createDate": "2024-10-03 13:04:16",
            "updater": "1067246875800000001",
            "updateDate": "2024-10-03 13:04:16",
            "category": "Auditorium",
            "deptName": "Faculty of Engineering",
            "managerName": "admin",
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
