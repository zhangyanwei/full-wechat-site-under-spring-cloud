package com.askdog.oauth.client.security;

import com.askdog.oauth.client.vo.UserInfo;
import com.askdog.service.UserService;
import com.askdog.service.bo.BasicInnerUser;
import com.askdog.service.exception.NotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class RememberMeUserDetailsService implements UserDetailsService {

    private UserService userService;

    public RememberMeUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        // OUR principal is user id (long type)
        BasicInnerUser innerUser = userService.findById(Long.valueOf(userId)).orElseThrow(() -> new NotFoundException(NotFoundException.Error.USER));
        UserInfo userInfo = new UserInfo();
        userInfo.setId(innerUser.getId());
        userInfo.setName(innerUser.getNickname());
        userInfo.setAvatar(innerUser.getAvatar());
        userInfo.setAuthorityStrings(innerUser.authorities());
        return userInfo;
    }
}
