package my.edu.um.umpoint.modules.space.availability;

import my.edu.um.umpoint.modules.space.entity.SpcAvailabilityEntity;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpaceAvailability {

    private static final int BYTES_IN_YEAR = 365 * 48 / 8;

    private final Map<Integer, byte[]> availabilityMap;

    public SpaceAvailability(List<SpcAvailabilityEntity> availabilityEntityList) {
        availabilityMap = new HashMap<>();
        availabilityEntityList.forEach(availabilityEntity ->
            availabilityMap.put(availabilityEntity.getYear(), availabilityEntity.getAvailability())
        );
    }

    public Map<Integer, byte[]> getAvailability() {
        return availabilityMap;
    }

    public void markUnavailable(LocalDateTime start, LocalDateTime end) {
        updateAvailability(start, end, true);
    }

    public void markUnavailable(Date start, Date end) {
        updateAvailability(
                start.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                end.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                true
        );
    }

    public void markAvailable(LocalDateTime start, LocalDateTime end) {
        updateAvailability(start, end, false);
    }

    public void markAvailable(Date start, Date end) {
        updateAvailability(
                start.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                end.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                false
        );
    }

    private void updateAvailability(LocalDateTime start, LocalDateTime end, boolean unavailable) {
        int startYear = start.getYear();
        int endYear = end.getYear();

        for (int year = startYear; year <= endYear; year++) {
            byte[] yearBytes = availabilityMap.computeIfAbsent(year, k -> new byte[BYTES_IN_YEAR]);

            LocalDateTime yearStart = year == startYear ? start : LocalDateTime.of(year, 1, 1, 0, 0);
            LocalDateTime yearEnd = year == endYear ? end : LocalDateTime.of(year, 12, 31, 23, 59);

            updateYearBytes(yearBytes, yearStart, yearEnd, unavailable);
        }
    }

    private void updateYearBytes(byte[] yearBytes, LocalDateTime start, LocalDateTime end, boolean unavailable) {
        long startIndex = timeToIndex(start);
        long endIndex = timeToIndex(end);

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

    private long timeToIndex(LocalDateTime time) {
        LocalDateTime startOfYear = LocalDateTime.of(time.getYear(), 1, 1, 0, 0);
        return ChronoUnit.MINUTES.between(startOfYear, time) / 30;
    }
}
