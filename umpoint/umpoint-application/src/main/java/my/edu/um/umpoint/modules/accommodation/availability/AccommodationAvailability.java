package my.edu.um.umpoint.modules.accommodation.availability;

import my.edu.um.umpoint.modules.accommodation.entity.AccAvailabilityEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccommodationAvailability {

    private final Map<Integer, byte[]> availabilityMap;

    public AccommodationAvailability(List<AccAvailabilityEntity> availabilityEntityList) {
        availabilityMap = new HashMap<>();
        availabilityEntityList.forEach(availabilityEntity ->
            availabilityMap.put(availabilityEntity.getYear(), availabilityEntity.getAvailability())
        );
    }

    public Map<Integer, byte[]> getAvailabilityMap() {
        return availabilityMap;
    }

    public void markUnavailable(LocalDate start, LocalDate end) {
        updateAvailability(start, end, true);
    }

    public void markUnavailable(Date start, Date end) {
        updateAvailability(
                start.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                end.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                true
        );
    }

    public void markAvailable(LocalDate start, LocalDate end) {
        updateAvailability(start, end, false);
    }

    public void markAvailable(Date start, Date end) {
        updateAvailability(
                start.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                end.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                false
        );
    }

    public void updateAvailability(LocalDate start, LocalDate end, boolean unavailable) {
        int startYear = start.getYear();
        int endYear = end.getYear();

        for (int year = startYear; year <= endYear; year++) {
            int finalYear = year;
            byte[] yearBytes = availabilityMap.computeIfAbsent(year, k -> new byte[(daysInYear(finalYear) + 7) / 8]);

            long startIndex = dateToIndex(start);
            long endIndex = dateToIndex(end);

            for (long i = startIndex; i <= endIndex; i++) {
                int byteIndex = (int) (i / 8);
                int bitIndex = (int) (i % 8);

                if (unavailable) {
                    yearBytes[byteIndex] |= (byte) (1 << bitIndex);
                } else {
                    yearBytes[byteIndex] &= (byte) ~(1 << bitIndex);
                }
            }
        }
    }

    private int daysInYear(int year) {
        return LocalDate.of(year, 12, 31).getDayOfYear();
    }

    private long dateToIndex(LocalDate date) {
        return date.getDayOfYear() - 1;
    }
}
