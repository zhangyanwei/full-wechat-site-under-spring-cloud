package com.askdog.web.configuration.oauth2;

import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;

import javax.servlet.Filter;

@SuppressWarnings("WeakerAccess")
public abstract class ClientResources {

    private OAuth2ProtectedResourceDetails client = new AuthorizationCodeResourceDetails();
    private AdResourceServerProperties resource = new AdResourceServerProperties();
    private AdOauth2AttributeDefinitions definition = new AdOauth2AttributeDefinitions();

    public OAuth2ProtectedResourceDetails getClient() {
        return client;
    }

    public AdResourceServerProperties getResource() {
        return resource;
    }

    public AdOauth2AttributeDefinitions getDefinition() {
        return definition;
    }

    abstract public Filter filter(String path);
}
