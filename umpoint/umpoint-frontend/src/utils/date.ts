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

export const diffDays = (date1: Date, date2: Date): number => {
    const diffTime = Math.abs(date2.getTime() - date1.getTime());
    return Math.ceil(diffTime / (1000 * 60 * 60 * 24));
}

export const sameDay = (date1: Date, date2: Date): boolean => {
    return (
        date1.getFullYear() === date2.getFullYear() &&
        date1.getMonth() === date2.getMonth() &&
        date1.getDate() === date2.getDate()
    );
}