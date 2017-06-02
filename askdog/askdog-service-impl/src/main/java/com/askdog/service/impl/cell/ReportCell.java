package com.askdog.service.impl.cell;

import com.askdog.dao.repository.mongo.ReportRepository;
import com.askdog.model.data.Report;
import com.askdog.model.data.inner.EntityType;
import com.askdog.service.UserService;
import com.askdog.service.bo.PureReport;
import com.askdog.service.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.askdog.common.utils.Conditions.checkState;
import static com.askdog.model.data.builder.ReportBuilder.reportBuilder;

@Component
@Transactional
public class ReportCell {

    @Autowired private ReportRepository reportRepository;
    @Autowired private UserService userService;

    public void report(@Nullable Long userId,
                       @Nonnull EntityType targetType,
                       @Nonnull Long target,
                       @Nonnull PureReport pureReport) {

        checkState(userId == null || userService.isExists(userId), () -> new NotFoundException(NotFoundException.Error.USER));

        Report report = reportBuilder()
                .type(pureReport.getType())
                .message(pureReport.getMessage())
                .target(target)
                .targetType(targetType)
                .user(userId)
                .build();
        reportRepository.save(report);
    }

}
