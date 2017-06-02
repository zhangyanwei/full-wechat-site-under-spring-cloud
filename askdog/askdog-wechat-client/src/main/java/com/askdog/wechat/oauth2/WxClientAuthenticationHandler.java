package com.askdog.wechat.oauth2;

import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.auth.ClientAuthenticationHandler;
import org.springframework.security.oauth2.common.AuthenticationScheme;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

public class WxClientAuthenticationHandler implements ClientAuthenticationHandler {

    @Override
    public void authenticateTokenRequest(OAuth2ProtectedResourceDetails resource, MultiValueMap<String, String> form, HttpHeaders headers) {
        if (resource.isAuthenticationRequired()) {
            AuthenticationScheme scheme = AuthenticationScheme.query;
            if (resource.getClientAuthenticationScheme() != null) {
                scheme = resource.getClientAuthenticationScheme();
            }

            String clientSecret = resource.getClientSecret();
            clientSecret = clientSecret == null ? "" : clientSecret;
            switch (scheme) {
                case query:
                    form.set("appid", resource.getClientId());
                    if (StringUtils.hasText(clientSecret)) {
                        form.set("secret", clientSecret);
                    }
                    break;
                default:
                    throw new IllegalStateException("Wechat authentication handler doesn't know how to handle scheme: " + scheme);
            }
        }
    }
}
