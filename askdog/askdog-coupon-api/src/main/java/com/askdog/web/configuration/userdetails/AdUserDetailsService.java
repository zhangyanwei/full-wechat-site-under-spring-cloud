package com.askdog.web.configuration.userdetails;

import com.askdog.service.UserService;
import com.askdog.service.bo.BasicInnerUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.askdog.common.RegexPattern.*;
import static java.lang.String.format;

@Component
public class AdUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(AdUserDetailsService.class);

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<BasicInnerUser> foundUser;
        if (username.matches(REGEX_MAIL)) {
            foundUser = userService.findByEmail(username);
        } else if (username.matches(REGEX_PHONE)) {
            foundUser = userService.findByPhone(username);
        } else if (username.matches(REGEX_USER_NAME)) {
            foundUser = userService.findByName(username);
        } else {
            foundUser = userService.findById(Long.valueOf(username));
        }
        if (!foundUser.isPresent()) {
            throw new UsernameNotFoundException(format("User [%s] not found.", username));
        }
        return new AdUserDetails(foundUser.get());
    }
}
