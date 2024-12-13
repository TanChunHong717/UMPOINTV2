package my.edu.um.umpoint.modules.sys.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import my.edu.um.umpoint.common.constant.Constant;
import my.edu.um.umpoint.common.utils.Result;
import my.edu.um.umpoint.modules.security.user.SecurityUser;
import my.edu.um.umpoint.modules.security.user.UserDetail;
import my.edu.um.umpoint.modules.sys.enums.SuperAdminEnum;
import my.edu.um.umpoint.modules.sys.service.DashboardService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/dashboard")
@Tag(name = "Dashboard API")
public class DashboardController {
    @Autowired
    private DashboardService dashboardService;

    @GetMapping("data")
    @Operation(summary = "Dashboard Data")
    @Parameters({
        @Parameter(name = Constant.START_TIME, description = "Start Date Time", in = ParameterIn.QUERY),
        @Parameter(name = Constant.END_TIME, description = "End Date Time", in = ParameterIn.QUERY),
    })
    public Result<Map<String, Object>> data(@Parameter(hidden = true) @RequestParam Map<String, Object> params){
        return new Result<Map<String, Object>>().ok(dashboardService.getData(params));
    }

    @GetMapping("report")
    @Operation(summary = "Dashboard Report")
    public Result<String> report(HttpServletRequest request){
        Map<String, Object> params = new HashMap<>();
        UserDetail userDetail = SecurityUser.getUser();
        if (userDetail.getSuperAdmin() == SuperAdminEnum.NO.value())
            params.put(Constant.DEPT_ID, userDetail.getDeptIdList());
        dashboardService.generateReport(params);

        String fullUrl = request.getRequestURL().toString();
        String baseUrl = fullUrl.substring(0, fullUrl.indexOf("/api") + 4);
        // return new Result<String>().ok(fullUrl + "/report.pdf");
        return new Result<String>().ok("https://mallstore.blob.core.windows.net/upload/DataAnalyticsReport.pdf");
    }
}
