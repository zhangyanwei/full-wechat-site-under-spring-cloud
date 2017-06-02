package com.askdog.coupon.store.web;

import com.askdog.oauth.client.vo.UserInfo;
import com.askdog.service.PasswordService;
import com.askdog.service.bo.ChangePassword;
import com.askdog.service.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import static org.springframework.web.bind.annotation.RequestMethod.PUT;


@RestController
@RequestMapping(value = "/api/user")
public class PasswordApi {

    @Autowired
    private PasswordService passwordService;

    protected HttpServletResponse response;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_AGENT_ADMIN', 'ROLE_AGENT_EMPLOYEE', 'ROLE_STORE_ADMIN', 'ROLE_STORE_EMPLOYEE')")
    @RequestMapping(value = "/password", method = PUT)
    public void updatePassword(@AuthenticationPrincipal UserInfo user, @Valid @RequestBody ChangePassword changePassword) throws ServiceException {
        passwordService.changePassword(user.getId(), changePassword);
        Cookie cookie = new Cookie("remember-me", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    @ModelAttribute
    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

}
