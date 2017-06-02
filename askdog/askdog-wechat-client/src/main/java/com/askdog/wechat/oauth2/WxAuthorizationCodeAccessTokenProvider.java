package com.askdog.wechat.oauth2;

import com.askdog.web.common.http.converter.TextJackson2HttpMessageConverter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.client.filter.state.DefaultStateKeyGenerator;
import org.springframework.security.oauth2.client.filter.state.StateKeyGenerator;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.resource.UserApprovalRequiredException;
import org.springframework.security.oauth2.client.resource.UserRedirectRequiredException;
import org.springframework.security.oauth2.client.token.AccessTokenRequest;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidRequestException;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

public class WxAuthorizationCodeAccessTokenProvider extends AuthorizationCodeAccessTokenProvider {

    private StateKeyGenerator stateKeyGenerator = new DefaultStateKeyGenerator();

    private boolean stateMandatory = true;
    private List<HttpMessageConverter<?>> messageConverters;

    public WxAuthorizationCodeAccessTokenProvider() {
        super();
        this.setAuthenticationHandler(new WxClientAuthenticationHandler());
    }

    /**
     * Flag to say that the use of state parameter is mandatory.
     *
     * @param stateMandatory the flag value (default true)
     */
    public void setStateMandatory(boolean stateMandatory) {
        this.stateMandatory = stateMandatory;
    }

    @Override
    public OAuth2AccessToken obtainAccessToken(OAuth2ProtectedResourceDetails details, AccessTokenRequest request) throws UserRedirectRequiredException, UserApprovalRequiredException, AccessDeniedException {
        AuthorizationCodeResourceDetails resource = (AuthorizationCodeResourceDetails) details;

        if (request.getAuthorizationCode() == null) {
            if (request.getStateKey() == null) {
                throw getRedirectForAuthorization(resource, request);
            }
            obtainAuthorizationCode(resource, request);
        }
        return retrieveToken(request, resource, getParametersForTokenRequest(resource, request), getHeadersForTokenRequest(request));
    }

    public void setMessageConverters(List<HttpMessageConverter<?>> messageConverters) {
        this.messageConverters = messageConverters;
        this.messageConverters.add(new TextJackson2HttpMessageConverter());
        super.setMessageConverters(this.messageConverters);
    }

    private UserRedirectRequiredException getRedirectForAuthorization(AuthorizationCodeResourceDetails resource,
                                                                      AccessTokenRequest request) {

        // NOTE: 尤其注意：由于授权操作安全等级较高，所以在发起授权请求时，微信会对授权链接做正则强匹配校验，如果链接的参数顺序不对，授权页面将无法正常访问
        // we don't have an authorization code yet. So first get that.
        TreeMap<String, String> requestParameters = new TreeMap<>();

        // Client secret is not required in the initial authorization request
        requestParameters.put("appid", resource.getClientId());

        String redirectUri = resource.getRedirectUri(request);
        if (redirectUri != null) {
            requestParameters.put("redirect_uri", redirectUri);
        }

        requestParameters.put("response_type", "code"); // oauth2 spec, section 3

        if (resource.isScoped()) {
            StringBuilder builder = new StringBuilder();
            List<String> scope = resource.getScope();
            if (scope != null) {
                Iterator<String> scopeIt = scope.iterator();
                while (scopeIt.hasNext()) {
                    builder.append(scopeIt.next());
                    if (scopeIt.hasNext()) {
                        builder.append(' ');
                    }
                }
            }
            requestParameters.put("scope", builder.toString());
        }

        UserRedirectRequiredException redirectException = new com.askdog.wechat.oauth2.filter.UserRedirectRequiredException(
                resource.getUserAuthorizationUri(), requestParameters, "wechat_redirect");
        String stateKey = stateKeyGenerator.generateKey(resource);
        redirectException.setStateKey(stateKey);
        request.setStateKey(stateKey);
        redirectException.setStateToPreserve(redirectUri);
        request.setPreservedState(redirectUri);

        return redirectException;
    }

    private MultiValueMap<String, String> getParametersForTokenRequest(AuthorizationCodeResourceDetails resource,
                                                                       AccessTokenRequest request) {

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.set("grant_type", "authorization_code");
        form.set("code", request.getAuthorizationCode());

        Object preservedState = request.getPreservedState();
        if (request.getStateKey() != null || stateMandatory) {
            // The token endpoint has no use for the state so we don't send it back, but we are using it
            // for CSRF detection client side...
            if (preservedState == null) {
                throw new InvalidRequestException(
                        "Possible CSRF detected - state parameter was required but no state could be found");
            }
        }

        // Extracting the redirect URI from a saved request should ignore the current URI, so it's not simply a call to
        // resource.getRedirectUri()
        String redirectUri;
        // Get the redirect uri from the stored state
        if (preservedState instanceof String) {
            // Use the preserved state in preference if it is there
            // TODO: treat redirect URI as a special kind of state (this is a historical mini hack)
            redirectUri = String.valueOf(preservedState);
        }
        else {
            redirectUri = resource.getRedirectUri(request);
        }

        if (redirectUri != null && !"NONE".equals(redirectUri)) {
            form.set("redirect_uri", redirectUri);
        }

        return form;

    }

    private HttpHeaders getHeadersForTokenRequest(AccessTokenRequest request) {
        HttpHeaders headers = new HttpHeaders();
        // No cookie for token request
        return headers;
    }

}
