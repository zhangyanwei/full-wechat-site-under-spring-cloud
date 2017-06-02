package com.askdog.web.configuration.userdetails;

import com.askdog.service.ExternalUserService;
import com.askdog.service.bo.BasicExternalUser;
import com.askdog.service.exception.NotFoundException;
import com.askdog.service.exception.ServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static com.askdog.model.entity.inner.user.UserProvider.WECHAT;

public class OAuthRememberMeUserDetailsService implements UserDetailsService {

    private ExternalUserService externalUserService;

    public OAuthRememberMeUserDetailsService(ExternalUserService externalUserService) {
        this.externalUserService = externalUserService;
    }

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        // OUR principal is user id (long type)
        BasicExternalUser basicExternalUser;
        try {
            basicExternalUser = externalUserService.findByUserIdAndProvider(Long.valueOf(userId), WECHAT).orElseThrow(() -> new NotFoundException(NotFoundException.Error.USER));
        } catch (ServiceException e) {
            throw new UsernameNotFoundException("can not find the external user or other error occurs.", e);
        }
        return new AdUserDetails(basicExternalUser);
    }
}
