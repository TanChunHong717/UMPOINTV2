export default {
    defaultLang: "en-us",
    apiUrl: import.meta.env.VITE_BOOKING_API_URL,
    requestTimeout: 30000,
};
export const CacheToken = "cache";
export const CacheRememberMeToken = "rememberMe";
export const facilityTypes = {
    space: "space",
    service: "service",
    accommodation: "accommodation",
};

// TODO: Sync with backend
export const bookingStatus = {
    0: "Pending",
    1: "Rejected",
    2: "Approved",
    3: "Completed",
    4: "Cancelled",
    PENDING: 0,
    REJECTED: 1,
    APPROVED: 2,
    COMPLETED: 3,
    CANCELLED: 4,
};
export const paymentStatus = {
    0: "Pending",
    1: "Success",
    2: "Failed",
    3: "Refunded",
    PENDING: 0,
    SUCCESS: 1,
    FAILED: 2,
    REFUNDED: 3,
};
export const eventStatus = {
    0: "Booking",
    1: "Closure",
};
