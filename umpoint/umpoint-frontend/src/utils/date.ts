export const formatDateToTimezoneDateStr = (date: Date, timezone?: string): string => {
    return date.toLocaleDateString('sv', {timeZone: timezone});
}

export const formatDateToTimezoneTimeStr = (date: Date, timezone?: string): string => {
    return date.toLocaleTimeString('sv', {timeZone: timezone});
}

export const formatDateToTimezoneDateTimeStr = (date: Date, timezone?: string): string => {
    return date.toLocaleString('sv', {timeZone: timezone});
}

export const addDays = (date: Date, days: number): Date => {
    const result = new Date(date);
    result.setDate(result.getDate() + days);
    return result;
}
