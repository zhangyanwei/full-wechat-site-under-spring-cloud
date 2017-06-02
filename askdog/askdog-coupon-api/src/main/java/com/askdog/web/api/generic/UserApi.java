package com.askdog.web.api.generic;

import com.askdog.service.NotificationService;
import com.askdog.service.UserService;
import com.askdog.service.bo.BasicUser;
import com.askdog.service.bo.UserDetail;
import com.askdog.service.exception.ServiceException;
import com.askdog.web.configuration.userdetails.AdUserDetails;
import com.askdog.web.vo.*;
import com.askdog.wechat.api.wxclient.WxClient;
import com.askdog.wechat.api.wxclient.exception.WxClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.askdog.service.utils.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static com.askdog.web.vo.UserStatus.ANONYMOUS;
import static com.askdog.web.vo.UserStatus.AUTHENTICATED;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping("/api/users")
public class UserApi {

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private WxClient wxClient;

    @PreAuthorize("isAuthenticated()")
    @RequestMapping("/me")
    public UserSelf me(@AuthenticationPrincipal AdUserDetails user) throws ServiceException {
        UserDetail refreshedUser = userService.findDetail(user.getId());
        UserSelf userSelf = new UserSelf().from(refreshedUser);
        userSelf.setNoticeCount(notificationService.findNoticeCount(user.getId()));

        String openid = user.getDetails().get("openid");
        if (null != openid) {
            try {
                com.askdog.wechat.api.wxclient.model.UserInfo wxUser;
                wxUser = wxClient.userInfo(openid).request();
                userSelf.setSubscribed(wxUser.isSubscribe());
            } catch (WxClientException e) {
                throw new AuthenticationServiceException("load authentication failed", e);
            }
        }
        return userSelf;
    }

    @RequestMapping("/me/status")
    public UserStatus status(@AuthenticationPrincipal Authentication authentication) {
        return authentication == null ? ANONYMOUS : AUTHENTICATED;
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/me/profile/personal_info", method = GET, produces = APPLICATION_JSON_UTF8_VALUE)
    public PersonalInfo profileBasic(@AuthenticationPrincipal AdUserDetails user) throws ServiceException {
        return new PersonalInfo().from(userService.findDetail(user.getId()));
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/me/profile/personal_info", method = PUT, produces = APPLICATION_JSON_UTF8_VALUE)
    public PersonalInfo updateProfileBasic(@AuthenticationPrincipal AdUserDetails user,
                                           @Valid @RequestBody UpdatedPersonalInfo detail) throws ServiceException {
        return new PersonalInfo().from(userService.updateUserAttribute(user.getId(), detail.convert()));
    }

    @RequestMapping(value = "/{userId}/user_info", method = GET, produces = APPLICATION_JSON_UTF8_VALUE)
    public BasicUser publicUserInfo(@PathVariable("userId") Long userId) throws ServiceException {
        UserDetail detail = userService.findDetail(userId);
        BasicUser basicUser = new BasicUser();
        basicUser.setId(detail.getId());
        basicUser.setNickname(detail.getNickname());
        basicUser.setAvatar(detail.getAvatar());
        basicUser.setSignature(detail.getSignature());
        basicUser.setTags(detail.getTags());
        basicUser.setGender(detail.getGender());
        return basicUser;
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/me/profile/avatar", method = PUT, produces = APPLICATION_JSON_UTF8_VALUE)
    public void updateProfileAvatar(@AuthenticationPrincipal AdUserDetails user,
                                    @RequestParam("linkId") Long linkId) throws ServiceException {
        userService.updateAvatar(user.getId(), linkId);
    }

    @PreAuthorize("isAnonymous()")
    @RequestMapping(method = POST, consumes = APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(CREATED)
    public UserSelf createMailUser(@Valid @RequestBody MailUser user) throws ServiceException {
        UserDetail savedUser = userService.createEmailUser(user.convert());
        UserSelf userSelf = new UserSelf().from(savedUser);
        userSelf.setNoticeCount(notificationService.findNoticeCount(savedUser.getId()));
        return userSelf;
    }

    @PreAuthorize("isAnonymous()")
    @RequestMapping(value = "/phone", method = POST, consumes = APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(CREATED)
    public UserSelf createPhoneUser(@Valid @RequestBody PhoneUser user) throws ServiceException {
        UserDetail savedUser = userService.createPhoneUser(user.getCode(), user.convert());
        UserSelf userSelf = new UserSelf().from(savedUser);
        userSelf.setNoticeCount(notificationService.findNoticeCount(savedUser.getId()));
        return userSelf;
    }

    @PreAuthorize("isAnonymous()")
    @RequestMapping(value = "/phone/code", method = POST, consumes = APPLICATION_JSON_UTF8_VALUE)
    public void generateIdentifyingCode(@Valid @RequestBody PhoneValidator phoneValidator) throws ServiceException {
        userService.generateIdentifyingCode(phoneValidator.getPhone());
    }

    @PreAuthorize("permitAll")
    @RequestMapping(value = "/token/validation")
    public TokenValidation validateRegistrationToken(@RequestParam("token") String token) {
        TokenValidation validation = new TokenValidation();
        validation.setToken(token);
        validation.setValid(userService.validateRegistrationToken(token));
        return validation;
    }

    @PreAuthorize("isAnonymous()")
    @RequestMapping(value = "/confirm", method = PUT)
    public UserSelf confirmRegistration(@RequestParam("token") String token) throws ServiceException {
        return new UserSelf().from(userService.confirmRegistration(token));
    }

}
