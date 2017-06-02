package com.askdog.oauth.client.api;

import com.askdog.oauth.client.vo.UserInfo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.askdog.service.utils.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping("/api/users")
public class UserApi {

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(path = "/me", produces = APPLICATION_JSON_UTF8_VALUE)
    public UserInfo me(@AuthenticationPrincipal UserInfo userInfo) {
        return userInfo;
    }

}
