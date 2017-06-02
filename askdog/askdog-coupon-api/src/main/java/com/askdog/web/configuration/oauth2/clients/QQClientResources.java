package com.askdog.web.configuration.oauth2.clients;

import com.askdog.web.configuration.oauth2.AdOAuth2RestTemplate;
import com.askdog.web.configuration.oauth2.AdOauth2AttributeDefinitions;
import com.askdog.web.configuration.oauth2.AdResourceServerProperties;
import com.askdog.web.configuration.oauth2.AdUserInfoTokenServices;
import com.askdog.web.configuration.oauth2.converter.HtmlFormOAuth2AccessTokenMessageConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

public class QQClientResources extends DefaultClientResources {

    @Override
    public OAuth2ClientAuthenticationProcessingFilter filter(String path) {
        OAuth2ClientAuthenticationProcessingFilter filter = super.filter(path);

        AdOAuth2RestTemplate restTemplate = (AdOAuth2RestTemplate) filter.restTemplate;
        restTemplate.addMessageConverters(Collections.singletonList(new HtmlFormOAuth2AccessTokenMessageConverter()));

        filter.setTokenServices(new QQUserInfoTokenServices(getResource(), getClient(), getDefinition(), restTemplate));

        return filter;
    }

    private class QQUserInfoTokenServices extends AdUserInfoTokenServices {

        QQUserInfoTokenServices(AdResourceServerProperties resource, OAuth2ProtectedResourceDetails client, AdOauth2AttributeDefinitions definition, OAuth2RestOperations oAuth2RestTemplate) {
            super(resource, client, definition, oAuth2RestTemplate);
        }

        @Override
        @SuppressWarnings("unchecked")
        public String getUserId(String userIdEndpointUrl, String accessToken, String principalKey) throws IOException {
            Map<String, String> resultMap = new ObjectMapper().readValue(sendRequestForData(userIdEndpointUrl), Map.class);
            if (resultMap.containsKey("error")) {
                throw OAuth2Exception.valueOf(resultMap);
            }

            return resultMap.get(principalKey);
        }

        @Override
        @SuppressWarnings("unchecked")
        protected Map<String, Object> extractUserProfile(String body) throws IOException {
            Map<String, Object> resultMap = new ObjectMapper().readValue(removeCallback(body), Map.class);
            if (!String.valueOf(resultMap.get("ret")).equals("0")) {
                throw OAuth2Exception.create(String.valueOf(resultMap.get("ret")), String.valueOf(resultMap.get("msg")));
            }

            return resultMap;
        }

        @Override
        protected String sendRequestForData(String url) {
            return removeCallback(restTemplate.getForEntity(url, String.class).getBody());
        }

        private String removeCallback(String origin) {
            if (StringUtils.isEmpty(origin) || !origin.startsWith("callback(")) {
                return origin;
            }

            return origin.substring(0, origin.indexOf("}") + 1).substring(origin.indexOf("{"));
        }
    }
}