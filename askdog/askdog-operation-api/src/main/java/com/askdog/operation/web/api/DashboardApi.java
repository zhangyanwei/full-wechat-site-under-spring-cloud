package com.askdog.operation.web.api;

import com.askdog.service.admin.DashboardService;
import com.askdog.service.bo.admin.dashboard.BasicStatistics;
import com.askdog.service.bo.admin.dashboard.UserStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.askdog.service.utils.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping("/api/dashboard/statistics")
public class DashboardApi {

    @Autowired
    private DashboardService dashboardService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/summary", produces =  APPLICATION_JSON_UTF8_VALUE)
    public BasicStatistics basicStatistics() {
        return dashboardService.basicStatistics();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/user", produces =  APPLICATION_JSON_UTF8_VALUE)
    public UserStatistics user() {
        return dashboardService.userStatistic();
    }

}
