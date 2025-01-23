import api from "@/utils/api";
import { facilityTypes } from "@/constants/app";
import { JavaId } from "@/types/interface";

export function getFacilities(type: keyof typeof facilityTypes, params: any) {
    return api.get(`/${type}/public/page`, {
        params
    });
}

export function getFacilityInformation(facilityType: keyof typeof facilityTypes, facilityID: JavaId) {
    // TODO: PLACEHOLDER
    if (Number(facilityID) == 2) {
        return {
            data: {
                code: 0,
                data: {
                    id: facilityID,
                    status: 1,
                    name: "Chancellor Room",
                    catId: "1841705487032270849",
                    deptId: "1841705636508876802",
                    address: "1234",
                    description: `Before completing your booking on UMPoint, please contact
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
                    facilities: "rewerwe",
                    capacity: 30,
                    manager: null,
                    hourPrice: 200,
                    fourHoursPrice: 700,
                    dayPrice: 1000,
                    bookingRuleId: null,
                    creator: "1067246875800000001",
                    createDate: "2024-10-03 13:04:16",
                    updater: "1067246875800000001",
                    updateDate: "2024-10-03 13:04:16",
                    category: "Auditorium",
                    deptName: "Faculty of Engineering",
                    managerName: "admin",
                    creatorName: "admin",
                    updaterName: "admin",
                    spcImageDTOList: [],
                    spcTagDTOList: [
                        {
                            id: "1841764876103708673",
                            tagName: "temp",
                            spaceCount: null,
                        },
                    ],
                    spcBookingRuleDTO: {
                        approvalRequired: 1,
                        openForStaff: 1,
                        openForStudent: 0,
                        openForPublic: 0,
                        holidayAvailable: 1,
                        startTime: "08:00",
                        endTime: "17:00",
                        maxBookingAdvanceDay: 60,
                        minBookingAdvanceDay: 5,
                        maxReservationDays: 3,
                        minReservationDays: 1,
                        maxBookingHours: 24,
                        minBookingHours: 1,
                        bookingMode: 1,
                        bookingUnit: 60,
                    },
                },
            },
        };
    }

    return api.get(`/${facilityType}/public/${facilityID}`);
}

export function getFacilityCategories(type: keyof typeof facilityTypes) {
    return api.get(`/${type}/category/list/filter`)
}

export function getDepartments() {
    return api.get(`/sys/dept/public/list`);
}

// Bookings

export function getFacilityBookings(facilityType: keyof typeof facilityTypes, facilityID: JavaId | JavaId[], startTime: string, endTime: string) {
    // TODO: PLACEHOLDER
    if (Number(facilityID) == 2) {
        return {
            data: {
                code: 0,
                data: [
                    {
                        id: "1841705487032270849",
                        spaceId: "1841705487032270849",
                        spaceName: "Chancellor Room",
                        spaceCode: "1841705487032270849",
                        spaceAddress: "1234",
                        spaceDescription: `Before completing your booking on UMPoint, please contact`,
                    },
                ],
            },
        };
    }

    if (facilityType === facilityTypes.service) {
        return { data: { code: 0, data: [] } };
    }

    let params = {}
    if (Array.isArray(facilityID)) {
        params = { [`${facilityType}Id`]: facilityID.join(",") }
    } else {
        params = { [`${facilityType}Id`]: facilityID }
    }
    if (startTime) params = { ...params, startTime }
    if (endTime) params = { ...params, endTime }

    return api.get(`/${facilityType}/event`, {
        params,
    });
}

export function getCurrentUserBookings(facilityType: keyof typeof facilityTypes, params: any) {
    return api.get(`/${facilityType}/booking/page`, {
        params
    })
}

export function createBooking(facilityType: keyof typeof facilityTypes, formData: any) {
    return api.post(`/${facilityType}/booking`, formData);
}

export function cancelBooking(facilityType: keyof typeof facilityTypes, bookingID: JavaId) {
    return api.put(`/${facilityType}/booking/cancel/${bookingID}`);
}

export function payBooking(facilityType: keyof typeof facilityTypes, bookingID: JavaId) {
    return api.put(`/${facilityType}/booking/pay/${bookingID}`);
}

// helpers

export function transformGallery(facilityType: keyof typeof facilityTypes, data: any) {
    // transform data based on facility type
    switch (facilityType) {
        case facilityTypes.space:
            data.gallery = data.spcImageDTOList ?? {};
            break;
        case facilityTypes.service:
            data.gallery = data.svcImageDTOList ?? {};
            break;
        case facilityTypes.accommodation:
            data.gallery = data.accImageDTOList ?? {};
            break;
        default:
            throw new Error("Invalid facility type");
    }
    return data;
}

export function transformBookingRule(facilityType: keyof typeof facilityTypes, data: any) {
    // transform data based on facility type
    switch (facilityType) {
        case facilityTypes.space:
            data.bookingRule = data.spcBookingRuleDTO ?? {};
            break;
        case facilityTypes.service:
            data.bookingRule = data.svcBookingRuleDTO ?? {};
            break;
        case facilityTypes.accommodation:
            data.bookingRule = data.accBookingRuleDTO ?? {};
            break;
        default:
            throw new Error("Invalid facility type");
    }
    return data;
}