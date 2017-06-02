package com.askdog.wechat.oauth2;

import com.askdog.wechat.api.wxclient.WxClient;
import com.askdog.wechat.api.wxclient.exception.WxClientException;
import com.askdog.wechat.api.wxclient.model.UserInfo;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.askdog.common.utils.Json.readValue;
import static com.askdog.common.utils.Json.writeValueAsString;
import static com.google.common.collect.Iterables.any;
import static org.springframework.security.core.authority.AuthorityUtils.commaSeparatedStringToAuthorityList;

public class WxResourceTokenServices implements ResourceServerTokenServices {

    private static final List<String> OAUTH2_TOKEN_REQUIRED = Arrays.asList(
            "snsapi_userinfo",
            "snsapi_login"
    );

    private OAuth2ProtectedResourceDetails resourceDetails;
    private OAuth2RestOperations restTemplate;
    private WxClient client;

    public WxResourceTokenServices(WxClient client, OAuth2RestOperations restTemplate, OAuth2ProtectedResourceDetails resourceDetails) {
        this.resourceDetails = resourceDetails;
        this.client = client;
        this.restTemplate = restTemplate;
    }

    @Override
    public OAuth2Authentication loadAuthentication(String accessToken) throws AuthenticationException, InvalidTokenException {
        OAuth2ClientContext oAuth2ClientContext = this.restTemplate.getOAuth2ClientContext();
        OAuth2AccessToken oAuth2AccessToken = oAuth2ClientContext.getAccessToken();
        String openid = (String) oAuth2AccessToken.getAdditionalInformation().get("openid");
        try {
            boolean userInfoScope = any(this.resourceDetails.getScope(), OAUTH2_TOKEN_REQUIRED::contains);
            UserInfo principal = userInfoScope ? this.client.userInfo(oAuth2AccessToken.getValue(), openid).request() : this.client.userInfo(openid).request();
            return extractAuthentication(principal);
        } catch (WxClientException e) {
            throw new AuthenticationServiceException("load authentication failed", e);
        }
    }

    @Override
    public OAuth2AccessToken readAccessToken(String accessToken) {
        throw new UnsupportedOperationException("Not supported: read access token");
    }

    private OAuth2Authentication extractAuthentication(UserInfo principal) {
        List authorities = commaSeparatedStringToAuthorityList("ROLE_USER");
        OAuth2Request request = new OAuth2Request(null, this.resourceDetails.getClientId(), null, true, null, null, null, null, null);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(principal, "N/A", authorities);
        //noinspection ConstantConditions
        token.setDetails(readValue(writeValueAsString(principal), HashMap.class));
        return new OAuth2Authentication(request, token);
    }
}
