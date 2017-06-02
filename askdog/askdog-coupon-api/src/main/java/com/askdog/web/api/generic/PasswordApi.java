package com.askdog.web.api.generic;

import com.askdog.service.PasswordService;
import com.askdog.service.bo.ChangePassword;
import com.askdog.service.exception.ServiceException;
import com.askdog.web.configuration.userdetails.AdUserDetails;
import com.askdog.web.vo.IdentifyingCodePassword;
import com.askdog.web.vo.TokenPassword;
import com.askdog.web.vo.TokenValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nonnull;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@RestController
@RequestMapping("/api/password")
public class PasswordApi {

    @Autowired
    private PasswordService passwordService;

    protected HttpServletResponse response;

    @PreAuthorize("permitAll")
    @RequestMapping(value = "/recover", method = POST)
    public void recoverMailPassword(@RequestParam("mail") String mailAddress) throws ServiceException {
        passwordService.sendRecoverPasswordMail(mailAddress);
    }

    @PreAuthorize("permitAll")
    @RequestMapping(value = "/recover/phone", method = POST)
    public void recoverPhonePassword(@RequestParam("phone") String phoneNum) throws ServiceException {
        passwordService.sendRecoverPasswordPhone(phoneNum);
    }

    @PreAuthorize("permitAll")
    @RequestMapping(value = "/recover/code", method = POST)
    public void sendIdentifyingCode(@RequestParam("phone") String phoneNum) throws ServiceException {
        passwordService.sendIdentifyingCode(phoneNum);
    }

    @PreAuthorize("permitAll")
    @RequestMapping(value = "/code/validation", method = POST)
    public void validatIdentifyingCode(@Nonnull @RequestParam("phone") String phone, @RequestParam("code") String identifyingCode) throws ServiceException {
        passwordService.validateIdentifyingCode(phone, identifyingCode);
    }

    @PreAuthorize("permitAll")
    @RequestMapping(value = "/phone", method = PUT)
    public void updatePhonePassword(@Valid @RequestBody IdentifyingCodePassword password) throws ServiceException {
        passwordService.updatePhonePassword(password.getPhone(), password.getPassword(), password.getCode());
    }

    @PreAuthorize("permitAll")
    @RequestMapping(value = "/token/validation")
    public TokenValidation validatePasswordToken(@RequestParam("token") String token) {
        TokenValidation validation = new TokenValidation();
        validation.setToken(token);
        validation.setValid(passwordService.validatePasswordToken(token));
        return validation;
    }

    @PreAuthorize("permitAll")
    @RequestMapping(method = PUT)
    public void updateMailPassword(@Valid @RequestBody TokenPassword tokenPassword) throws ServiceException {
        passwordService.updateMailPassword(tokenPassword.getToken(), tokenPassword.getMail(), tokenPassword.getPassword());
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/change", method = PUT)
    public void updatePassword(@AuthenticationPrincipal AdUserDetails user, @Valid @RequestBody ChangePassword changePassword) throws ServiceException {
        passwordService.changePassword(user.getId(), changePassword);

        Cookie cookie = new Cookie("remember-me",null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
    @ModelAttribute
    public void setResponse(HttpServletResponse response){
        this.response = response;
    }
}
