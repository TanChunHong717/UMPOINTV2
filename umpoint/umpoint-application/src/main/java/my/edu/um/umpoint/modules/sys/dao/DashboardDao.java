package my.edu.um.umpoint.modules.sys.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface DashboardDao {
    Map<String, Object> getSpcBookingStats(Map<String, Object> params);
    Map<String, Object> getSvcBookingStats(Map<String, Object> params);
    Map<String, Object> getAccBookingStats(Map<String, Object> params);
    int getTotalSpaces();
    int getTotalServices();
    int getTotalAccommodations();
    List<Double> getSpcDailyBookingAmounts(Map<String, Object> params);
    List<Double> getSvcDailyBookingAmounts(Map<String, Object> params);
    List<Double> getAccDailyBookingAmounts(Map<String, Object> params);
    List<Map<String, Object>> getTop10SpacesByBookingAmount(Map<String, Object> params);
    List<Map<String, Object>> getTop10ServicesByBookingAmount(Map<String, Object> params);
    List<Map<String, Object>> getTop10AccommodationsByBookingAmount(Map<String, Object> params);
}
