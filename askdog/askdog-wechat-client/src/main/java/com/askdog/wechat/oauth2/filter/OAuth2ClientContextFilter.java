package com.askdog.wechat.oauth2.filter;

import org.springframework.security.oauth2.client.resource.UserRedirectRequiredException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.util.Assert;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class OAuth2ClientContextFilter extends org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter {

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    public void afterPropertiesSet() throws Exception {
        Assert.notNull(redirectStrategy, "A redirect strategy must be supplied.");
    }

    @Override
    protected void redirectUser(UserRedirectRequiredException e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String redirectUri = e.getRedirectUri();
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(redirectUri);
        Map<String, String> requestParams = e.getRequestParams();
        for (Map.Entry<String, String> param : requestParams.entrySet()) {
            builder.queryParam(param.getKey(), param.getValue());
        }

        if (e.getStateKey() != null) {
            builder.queryParam("state", e.getStateKey());
        }

        if (e instanceof com.askdog.wechat.oauth2.filter.UserRedirectRequiredException) {
            com.askdog.wechat.oauth2.filter.UserRedirectRequiredException origin = (com.askdog.wechat.oauth2.filter.UserRedirectRequiredException) e;
            builder.fragment(origin.getFragment());
        }

        this.redirectStrategy.sendRedirect(request, response, builder.build().encode().toUriString());
    }

    public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
        this.redirectStrategy = redirectStrategy;
    }
}
