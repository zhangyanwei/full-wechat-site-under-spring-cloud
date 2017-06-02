package com.askdog.web.configuration.oauth2.clients;

import com.askdog.model.entity.inner.user.UserProvider;
import com.askdog.service.ExternalUserService;
import com.askdog.service.bo.BasicExternalUser;
import com.askdog.service.exception.ServiceException;
import com.askdog.web.configuration.handler.WebAuthenticationFailureHandler;
import com.askdog.web.configuration.handler.WebAuthenticationSuccessHandler;
import com.askdog.web.configuration.oauth2.AdOAuth2RestTemplate;
import com.askdog.web.configuration.oauth2.AdUserInfoTokenServices;
import com.askdog.web.configuration.oauth2.ClientResources;
import com.askdog.web.configuration.userdetails.AdUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.springframework.security.core.authority.AuthorityUtils.commaSeparatedStringToAuthorityList;

public class DefaultClientResources extends ClientResources {

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private OAuth2ClientContext oAuth2ClientContext;

    @Autowired
    private WebAuthenticationSuccessHandler webAuthenticationSuccessHandler;

    @Autowired
    private ExternalUserService externalUserService;

    @Override
    public OAuth2ClientAuthenticationProcessingFilter filter(String path) {
        OAuth2ClientAuthenticationProcessingFilter filter = new AdOAuth2ClientAuthenticationProcessingFilter(path);
        filter.setRestTemplate(new AdOAuth2RestTemplate(getClient(), oAuth2ClientContext));
        filter.setTokenServices(new AdUserInfoTokenServices(getResource(), getClient(), getDefinition(), filter.restTemplate));
        filter.setAuthenticationSuccessHandler(webAuthenticationSuccessHandler);
        filter.setAuthenticationFailureHandler(authenticationFailureHandler().errorPage("/error"));
        return filter;
    }

    @Bean(name = "oauth2")
    public WebAuthenticationFailureHandler authenticationFailureHandler() {
        return new WebAuthenticationFailureHandler();
    }

    class AdOAuth2ClientAuthenticationProcessingFilter extends OAuth2ClientAuthenticationProcessingFilter {

        AdOAuth2ClientAuthenticationProcessingFilter(String defaultFilterProcessesUrl) {
            super(defaultFilterProcessesUrl);
        }

        @Override
        public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
                throws AuthenticationException, IOException, ServletException {

            Map<String, Object> oAuthUserDetails = getOAuthUserDetails(request, response);
            String openId = getDefinition().getProperty(oAuthUserDetails, getDefinition().getId());

            BasicExternalUser externalUser = findOrCreateExternalUser(oAuthUserDetails, openId);
            // TODO why not response the Authentication of OAuth2Authentication directly ?
            AdUserDetails userDetails = new AdUserDetails(externalUser);
            return new UsernamePasswordAuthenticationToken(userDetails, "N/A", userDetails.getAuthorities());
        }

        @SuppressWarnings("unchecked")
        private Map<String, Object> getOAuthUserDetails(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
            OAuth2Authentication oAuthAuthentication = (OAuth2Authentication) super.attemptAuthentication(request, response);
            Authentication userAuthentication = oAuthAuthentication.getUserAuthentication();
            return (Map<String, Object>) userAuthentication.getDetails();
        }

        private BasicExternalUser findOrCreateExternalUser(Map<String, Object> oAuthUserDetails, String openId) {
            try {
                UserProvider provider = UserProvider.valueOf(getClient().getId());
                Optional<BasicExternalUser> externalUserOptional = externalUserService.findByExternalUserIdAndProvider(openId, provider);

                String nickname = getDefinition().getProperty(oAuthUserDetails, getDefinition().getNickname());
                String avatar = getDefinition().getProperty(oAuthUserDetails, getDefinition().getAvatar());

                Map<String, String> details = new HashMap<>();
                oAuthUserDetails.forEach((key, value) -> {
                    if (value != null) {
                        details.put(key, value.toString());
                    }
                });
                if (!externalUserOptional.isPresent()) {
                    BasicExternalUser basicExternalUser = new BasicExternalUser();
                    basicExternalUser.setExternalUserId(openId);
                    basicExternalUser.setNickname(nickname);
                    basicExternalUser.setAvatar(avatar);
                    basicExternalUser.setProvider(provider);
                    basicExternalUser.setDetails(details);
                    return externalUserService.save(basicExternalUser);
                }

                BasicExternalUser externalUser = externalUserOptional.get();
                externalUser.setNickname(nickname);
                externalUser.setAvatar(avatar);
                externalUser.setDetails(details);
                externalUserService.update(externalUser);
                return externalUser;
            } catch (ServiceException e) {
                throw new AuthenticationServiceException("can not retrieve the external user entity", e);
            }
        }

    }

}
