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
