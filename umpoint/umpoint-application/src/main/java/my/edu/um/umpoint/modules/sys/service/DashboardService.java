package my.edu.um.umpoint.modules.sys.service;

import java.util.Map;

public interface DashboardService {
    Map<String, Object> getData(Map<String, Object> params);

    void generateReport(Map<String, Object> params);
}
