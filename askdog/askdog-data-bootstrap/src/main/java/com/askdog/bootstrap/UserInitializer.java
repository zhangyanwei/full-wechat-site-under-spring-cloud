package com.askdog.bootstrap;

import com.askdog.dao.repository.UserRepository;
import com.askdog.model.entity.User;
import com.askdog.model.entity.builder.UserBuilder;
import com.askdog.model.entity.inner.user.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Date;

@Service
@Order(2)
@Profile("dev")
public class UserInitializer {

    @Autowired private UserRepository userRepository;

    private static final Long USER_ID = 30962247440242404L;

    @PostConstruct
    private void initialize() {

        if (userRepository.findOne(USER_ID) == null) {
            User user = UserBuilder.userBuilder()
                    .nickname("askdog")
                    .email("askdog@askdog.com")
                    .phoneNumber("13812341234")
                    .password(new BCryptPasswordEncoder().encode("123123"))
                    .type(UserType.REGISTERED)
                    .registrationTime(new Date()).build();

            user.setId(USER_ID);
            userRepository.save(user);
        }

    }

}
