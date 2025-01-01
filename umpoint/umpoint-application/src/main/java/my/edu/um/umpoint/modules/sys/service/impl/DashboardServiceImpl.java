package my.edu.um.umpoint.modules.sys.service.impl;

import my.edu.um.umpoint.common.constant.Constant;
import my.edu.um.umpoint.common.exception.RenException;
import my.edu.um.umpoint.modules.sys.dao.DashboardDao;
import my.edu.um.umpoint.modules.sys.service.DashboardService;
import my.edu.um.umpoint.modules.utils.PdfGenerator;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class DashboardServiceImpl implements DashboardService {

    private static final String BOOKING_COUNT = "bookingCount";
    private static final String BOOKING_AMOUNT = "bookingAmount";

    private static final String contextText = """
        Context information is below, surrounded by ---------------------
    
        ---------------------
        %s
        ---------------------
    
        Write a short description to describe and summarise the data.
        Your response should no contain "you" and without markdown syntax.
    """;

    private final ChatClient chatClient;

    @Autowired
    private DashboardDao dashboardDao;

    public DashboardServiceImpl(@Qualifier("clientChat") ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @Override
    public Map<String, Object> getData(Map<String, Object> params) {
        Map<String, Object> spcBookingStats = dashboardDao.getSpcBookingStats(params);
        Map<String, Object> svcBookingStats = dashboardDao.getSvcBookingStats(params);
        Map<String, Object> accBookingStats = dashboardDao.getAccBookingStats(params);

        long bookingCount = (long) spcBookingStats.get(BOOKING_COUNT) + (long) svcBookingStats.get(BOOKING_COUNT) + (long) accBookingStats.get(BOOKING_COUNT);
        BigDecimal spcBookingAmount = (BigDecimal) spcBookingStats.get(BOOKING_AMOUNT);
        BigDecimal svcBookingAmount = (BigDecimal) svcBookingStats.get(BOOKING_AMOUNT);
        BigDecimal accBookingAmount = (BigDecimal) accBookingStats.get(BOOKING_AMOUNT);
        BigDecimal bookingAmount = spcBookingAmount.add(svcBookingAmount).add(accBookingAmount);

        Map<String, Object> result = new HashMap<>();
        result.put(BOOKING_COUNT, bookingCount);
        result.put(BOOKING_AMOUNT, bookingAmount);
        result.put("spaceNumber", dashboardDao.getTotalSpaces());
        result.put("serviceNumber", dashboardDao.getTotalServices());
        result.put("accommodationNumber", dashboardDao.getTotalAccommodations());
        result.put("spaceBookingAmount", spcBookingAmount);
        result.put("serviceBookingAmount", svcBookingAmount);
        result.put("accommodationBookingAmount", accBookingAmount);
        result.put("spaceData", dashboardDao.getSpcDailyBookingAmounts(params));
        result.put("serviceData", dashboardDao.getSvcDailyBookingAmounts(params));
        result.put("accommodationData", dashboardDao.getAccDailyBookingAmounts(params));
        result.put("spaceList", dashboardDao.getTop10SpacesByBookingAmount(params));
        result.put("serviceList", dashboardDao.getTop10ServicesByBookingAmount(params));
        result.put("accommodationList", dashboardDao.getTop10AccommodationsByBookingAmount(params));

        return result;
    }

    @Override
    public void generateReport(Map<String, Object> params) {
        Map<String, Object> data = getData(params);
        data.put(Constant.START_TIME, params.get(Constant.START_TIME));
        data.put(Constant.END_TIME, params.get(Constant.END_TIME));

        String response = chatClient
                .prompt()
                .user(String.format(contextText, data.toString()))
                .call()
                .content();

        try {
            PdfGenerator.generateDashboardReport(data, response);
        } catch (Exception e) {
            throw new RenException("Failed to generated report");
        }
    }
}
