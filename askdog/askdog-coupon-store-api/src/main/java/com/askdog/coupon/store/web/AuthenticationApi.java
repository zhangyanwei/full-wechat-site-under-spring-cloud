package com.askdog.coupon.store.web;

import com.askdog.coupon.store.web.vo.AuthenticationUser;
import com.askdog.coupon.store.web.vo.UserBasic;
import com.askdog.oauth.client.vo.UserInfo;
import com.askdog.service.StoreService;
import com.askdog.service.UserService;
import com.askdog.service.bo.SimpleUserCreation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.askdog.service.utils.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/api/users")
public class AuthenticationApi {

    @Autowired private StoreService storeService;
    @Autowired private UserService userService;

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(path = "/current", produces = APPLICATION_JSON_UTF8_VALUE)
    public AuthenticationUser me(@AuthenticationPrincipal UserInfo userInfo) {
        AuthenticationUser authenticationUser = new AuthenticationUser();
        authenticationUser.setId(userInfo.getId());
        authenticationUser.setName(userInfo.getName());
        authenticationUser.setAvatar(userInfo.getAvatar());
        authenticationUser.setAuthorities(userInfo.getAuthorityStrings());
        authenticationUser.setStoreIds(storeService.findOwnedStores(userInfo.getId()));
        return authenticationUser;
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_AGENT_ADMIN', 'ROLE_AGENT_EMPLOYEE')")
    @RequestMapping(method = POST)
    public UserBasic createSimpleUser(@Valid @RequestBody SimpleUserCreation user) {
        return new UserBasic().from(userService.createSimpleUser(user));
    }

}
