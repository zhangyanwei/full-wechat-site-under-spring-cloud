package com.askdog.service.admin;

import com.askdog.service.bo.admin.dashboard.BasicStatistics;
import com.askdog.service.bo.admin.dashboard.UserStatistics;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.askdog.service.utils.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@FeignClient("service")
@RequestMapping("/service/dashboard")
public interface DashboardService {

    @RequestMapping(value = "/statistic/summary", method = GET, produces = APPLICATION_JSON_UTF8_VALUE)
    BasicStatistics basicStatistics();

    @RequestMapping(value = "/statistic/user", method = GET, produces = APPLICATION_JSON_UTF8_VALUE)
    UserStatistics userStatistic();
}
