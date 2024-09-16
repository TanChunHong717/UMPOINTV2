export const formatDateTime = (date, time) => {
  const [hours, minutes] = time.split(':').map(Number);
  return `${date.toISOString().split('T')[0]} ${String(hours).padStart(2, '0')}:${String(minutes).padStart(2, '0')}`;
};

export const formatDescription = (description: string) => {
  if (description.startsWith('"'))
    description = description.substring(1);
  if (description.endsWith('"'))
    description = description.substring(0, description.length-1);
  description = description.replace("\\n", "");
  return description;
}

export const formatBookingStatus = (status: number): string => {
  switch (status) {
    case 0:
      return 'Pending';
    case 1:
      return 'Reject';
    case 2:
      return 'Approve';
    case 3:
      return 'Complete';
    case 4:
      return 'Cancel';
  }
}

export const getMondayAndSunday = (currentDate: Date) => {
  const currentDay = currentDate.getDay();
  const dayDifferenceFromMonday = currentDay === 0 ? 6 : currentDay - 1;

  const monday = new Date(currentDate);
  monday.setDate(currentDate.getDate() - dayDifferenceFromMonday);

  const sunday = new Date(monday);
  sunday.setDate(monday.getDate() + 6);

  return { startDate: monday, endDate: sunday };
}
