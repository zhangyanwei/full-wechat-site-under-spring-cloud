package com.askdog.web.configuration.handler;

import com.askdog.common.exception.AbstractRuntimeException;
import com.askdog.service.LocationService;
import com.askdog.service.MessageService;
import com.askdog.service.UserService;
import com.askdog.service.bo.BasicInnerUser;
import com.askdog.service.exception.ServiceException;
import com.askdog.web.HttpEntityProcessor;
import com.askdog.web.advice.UncaughtExceptionsControllerAdvice.RepresentationMessage;
import com.askdog.web.common.async.AsyncCaller;
import com.askdog.web.configuration.userdetails.AdUserDetails;
import com.askdog.web.vo.UserSelf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

import static com.askdog.model.common.EventType.USER_LOGIN;
import static com.askdog.model.data.inner.EntityType.USER;
import static com.askdog.service.utils.MediaType.APPLICATION_JSON_UTF8;
import static com.askdog.web.utils.HeaderUtils.getRequestRealIp;
import static com.google.common.base.Strings.isNullOrEmpty;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;

public class WebAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler implements AjaxAuthenticationHandler<WebAuthenticationSuccessHandler> {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebAuthenticationSuccessHandler.class);

    @Autowired private UserService userService;
    @Autowired private LocationService locationService;
    @Autowired private AsyncCaller asyncCaller;
    @Autowired private HttpEntityProcessor httpEntityProcessor;
    @Autowired private MessageService messageService;

    private String ajaxParam = DEFAULT_AJAX_PARAM;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        try {
            // update the last access time.
            AdUserDetails userDetails = (AdUserDetails) authentication.getPrincipal();
            userService.updateLastAccessTime(userDetails.getId(), new Date());

            // user location
            location(request, userDetails.getId());

            // write user info into response

            // redirect previous url
            if (!Boolean.valueOf(request.getParameter(ajaxParam))) {
                super.onAuthenticationSuccess(request, response, authentication);
            }

            ResponseEntity<UserSelf> entity = ResponseEntity.status(OK)
                    .contentType(APPLICATION_JSON_UTF8)
                    .body(toUserSelf(userDetails));
            httpEntityProcessor.writeEntity(entity, response);

            messageService.sendEventMessage(userDetails.getId(), USER_LOGIN, 1L);

        } catch (AbstractRuntimeException e) {
            ResponseEntity<RepresentationMessage> entity = ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .contentType(APPLICATION_JSON_UTF8)
                    .body(new RepresentationMessage(e));
            httpEntityProcessor.writeEntity(entity, response);
        }
    }

    public WebAuthenticationSuccessHandler ajaxParam(String name) {
        this.ajaxParam = name;
        return this;
    }

    public WebAuthenticationSuccessHandler targetUrlParam(String name) {
        super.setTargetUrlParameter(name);
        return this;
    }

    private UserSelf toUserSelf(AdUserDetails userDetails) {
        BasicInnerUser innerUser = userDetails.getUser();
        UserSelf userSelf = new UserSelf();
        userSelf.setId(innerUser.getId());
        userSelf.setName(innerUser.getNickname());
        userSelf.setMail(innerUser.getMail());
        userSelf.setPhone(innerUser.getPhone());
        userSelf.setType(innerUser.getType());
        userSelf.setNoticeCount(innerUser.getNoticeCount());
        userSelf.setAvatar(innerUser.getAvatar());
        return userSelf;
    }

    private void location(HttpServletRequest request, Long userId) {
        String realIp = getRequestRealIp(request);
        String latParam = request.getParameter(GEO_LAT_PARAM);
        String lngParam = request.getParameter(GEO_LNG_PARAM);
        asyncCaller.asyncCall(() -> {
            try {
                try {
                    Double lat = isNullOrEmpty(latParam) ? null : Double.valueOf(latParam);
                    Double lng = isNullOrEmpty(lngParam) ? null : Double.valueOf(lngParam);
                    locationService.recordLocation(USER, userId, realIp, lat, lng);
                } catch (NumberFormatException e) {
                    locationService.recordLocation(USER, userId, realIp, null, null);
                }

                // user residence
                locationService.updateUserResidence(userId);
            } catch (ServiceException e) {
                LOGGER.warn(e.getLocalizedMessage());
            }
        });
    }

}
