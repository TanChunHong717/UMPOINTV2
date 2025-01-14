export const formatDescription = (description: string) => {
  if (description.startsWith('"'))
    description = description.substring(1);
  if (description.startsWith('\\"'))
    description = description.substring(2);
  if (description.endsWith('"'))
    description = description.substring(0, description.length-1);
  if (description.endsWith('\\"'))
    description = description.substring(0, description.length-2);
  description = description.replaceAll("\\n", "");
  description = description.replaceAll("null", "");
  return description;
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
