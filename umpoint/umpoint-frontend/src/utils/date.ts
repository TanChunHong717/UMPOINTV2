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

export const diffHours = (time1: string, time2: String): number => {
    let date1 = new Date(`2000-01-01T${time1}`);
    let date2 = new Date(`2000-01-01T${time2}`);
    const diffTime = Math.abs(date2.getTime() - date1.getTime());
    return diffTime / (1000 * 60 * 60);
}

export const sameTime = (time1: string, time2: string): boolean => {
    let date1 = new Date(`2000-01-01T${time1}`);
    let date2 = new Date(`2000-01-01T${time2}`);
    return date1 === date2;
}

export const listTimesBetween = (startTime: string, endTime: string, gapMinutes: number): string[] => {
    const times = [];
    let currentTime = new Date(`2000-01-01T${startTime}`);
    const endTimeDate = new Date(`2000-01-01T${endTime}`);
    const gapMilliseconds = gapMinutes * 60 * 1000;

    while (currentTime <= endTimeDate) {
        times.push(currentTime.toTimeString().slice(0, 5));
        currentTime = new Date(currentTime.getTime() + gapMilliseconds);
    }

    return times;
}