package com.askdog.service.impl.admin;

import com.askdog.dao.repository.UserRepository;
import com.askdog.dao.repository.mongo.EventLogRepository;
import com.askdog.service.admin.DashboardService;
import com.askdog.service.bo.admin.dashboard.BasicStatistics;
import com.askdog.service.bo.admin.dashboard.UserStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import static com.askdog.model.entity.inner.user.UserStatus.MAIL_CONFIRMED;

@RestController
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventLogRepository eventLogRepository;

    @Override
    public BasicStatistics basicStatistics() {
        BasicStatistics basicStatistics = new BasicStatistics();
        basicStatistics.setUserCount(userRepository.countRegisteredUser());
        return basicStatistics;
    }

    @Override
    public UserStatistics userStatistic() {
        UserStatistics userStatistics = new UserStatistics();
        userStatistics.setTotalUserCount(userRepository.countRegisteredUser());
        userStatistics.setTotalRegistrationConfirmedUserCount(userRepository.countByUserStatus(MAIL_CONFIRMED));
        userStatistics.setUserDistribution(userRepository.userProviders());
        userStatistics.setUserRegistrationTrend(userRepository.userRegistrationStatistics("day", "1 years"));
        userStatistics.setUserActiveTrend(eventLogRepository.activeUserStatistics());
        return userStatistics;
    }

}
