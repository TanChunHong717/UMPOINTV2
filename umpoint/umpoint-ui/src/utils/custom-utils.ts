export const generateDisabledWeekends = (startDate: Date, endDate: Date, convertTimeZone: boolean) => {
  const disabledDays = [];
  const currentDate = new Date(startDate);

  while (currentDate <= endDate) {
    const day = currentDate.getDay();
    if (day === 0 || day === 6) {
      const temp = new Date(currentDate)
      if (convertTimeZone)
        temp.setDate(currentDate.getDate() + 1)
      disabledDays.push(temp.toISOString().split('T')[0]); // Push formatted date string
    }
    currentDate.setDate(currentDate.getDate() + 1);
  }
  return disabledDays;
};

export const formatDescription = (description: string) => {
  if (description.startsWith('"'))
    description = description.substring(1);
  if (description.endsWith('"'))
    description = description.substring(0, description.length-1);
  description = description.replace("\\n", "");
  return description;
}
