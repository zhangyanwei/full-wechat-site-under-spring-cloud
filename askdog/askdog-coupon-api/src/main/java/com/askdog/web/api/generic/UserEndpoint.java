package com.askdog.web.api.generic;

import com.askdog.service.exception.ServiceException;
import com.askdog.web.configuration.userdetails.AdUserDetails;
import com.askdog.web.vo.UserInfo;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/")
public class UserEndpoint {

    @RequestMapping("/oauth/userinfo")
    public UserInfo me(Principal user) throws ServiceException {
        return new UserInfo().from((AdUserDetails) ((OAuth2Authentication) user).getPrincipal());
    }
}