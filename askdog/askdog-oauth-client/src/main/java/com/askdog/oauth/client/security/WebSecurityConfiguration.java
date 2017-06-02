package com.askdog.oauth.client.security;

import com.askdog.oauth.client.security.handler.WebLogoutSuccessHandler;
import com.askdog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.RememberMeAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.Filter;

import static com.google.common.collect.Lists.newArrayList;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices.SPRING_SECURITY_REMEMBER_ME_COOKIE_KEY;

@Configuration
@EnableOAuth2Client
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private SecurityConfiguration securityConfiguration;

    @Autowired
    private OAuth2ClientContext oauth2ClientContext;

    @Autowired
    private UserService userService;

    @Value("${askdog.remember-me.token.key:bdDF8D0c5OkJ8U2j}")
    private String rememberMeTokenKey;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests().anyRequest().permitAll()
                .and()
                .addFilterBefore(authenticationFilter(), BasicAuthenticationFilter.class)
                .addFilterBefore(rememberMeFilter(), BasicAuthenticationFilter.class)
                .logout()
                // TODO should using constant instead, and also should support the ajax logout
                .deleteCookies("SESSION", SPRING_SECURITY_REMEMBER_ME_COOKIE_KEY)
                .invalidateHttpSession(true)
                .logoutUrl("/logout")
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout", GET.name()))
                .logoutSuccessHandler(webLogoutSuccessHandler())
                .permitAll()
                .and()
                .csrf()
                .disable();
    }

    @Bean
    protected FilterRegistrationBean oAuth2ClientFilterRegistration(OAuth2ClientContextFilter filter) {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(filter);
        registrationBean.setOrder(-100);
        return registrationBean;
    }

    @Bean
    public LogoutSuccessHandler webLogoutSuccessHandler() {
        return new WebLogoutSuccessHandler();
    }

    @Bean
    public PersistentTokenBasedRememberMeServices rememberMeServices() {
        UserDetailsService userDetailsService = new RememberMeUserDetailsService(userService);
        PersistentTokenRepository tokenRepository = new InMemoryTokenRepositoryImpl();
        PersistentTokenBasedRememberMeServices rememberMeServices = new PersistentTokenBasedRememberMeServices(rememberMeTokenKey, userDetailsService, tokenRepository);
        rememberMeServices.setAlwaysRemember(true);
        return rememberMeServices;
    }

    private Filter authenticationFilter() {
        OAuth2ClientAuthenticationProcessingFilter authFilter = new OAuth2ClientAuthenticationProcessingFilter("/login");
        OAuth2RestTemplate template = new OAuth2RestTemplate(securityConfiguration.getClient(), oauth2ClientContext);
        authFilter.setRestTemplate(template);
        authFilter.setTokenServices(new UserInfoTokenServices(userService, securityConfiguration.getResource().getUserInfoUri(), securityConfiguration.getClient().getClientId()));

        SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
        successHandler.setTargetUrlParameter("request");
        authFilter.setAuthenticationSuccessHandler(successHandler);

        authFilter.setRememberMeServices(rememberMeServices());
        return authFilter;
    }

    private Filter rememberMeFilter() {
        ProviderManager providerManager = new ProviderManager(newArrayList(new RememberMeAuthenticationProvider(rememberMeTokenKey)));
        return new RememberMeAuthenticationFilter(providerManager, rememberMeServices());
    }
}
