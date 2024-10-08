export default {
    defaultLang: "en-us",
    apiUrl: import.meta.env.VITE_BOOKING_API_URL,
    requestTimeout: 30000,
};
export const CacheToken = "cache";
export const facilityTypes = {
    space: "space",
    service: "service",
    accommodation: "accommodation",
}