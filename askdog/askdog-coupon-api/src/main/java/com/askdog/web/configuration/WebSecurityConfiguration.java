package com.askdog.web.configuration;

import com.askdog.web.Header;
import com.askdog.web.configuration.handler.*;
import com.askdog.web.configuration.oauth2.clients.DefaultClientResources;
import com.askdog.web.configuration.oauth2.clients.QQClientResources;
import com.askdog.web.configuration.oauth2.clients.WechatClientResources;
import com.askdog.web.configuration.oauth2.clients.WeiboClientResources;
import com.askdog.wechat.oauth2.filter.OAuth2ClientContextFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.RememberMeAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.CompositeFilter;

import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.List;

import static com.askdog.model.security.Authority.Role.USER;
import static com.google.common.collect.Lists.newArrayList;
import static org.springframework.http.HttpMethod.GET;

@Configuration
@EnableOAuth2Client
@EnableAuthorizationServer
@Order(6)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    @Qualifier("formLogin")
    private PersistentTokenBasedRememberMeServices formLoginRememberMeServices;

    @Autowired
    @Qualifier("oauth2")
    private PersistentTokenBasedRememberMeServices oauth2RememberMeServices;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers(
                        "/login",
                        "/login/**",
                        "/error",
                        "/error_*.html",
                        "/web_*.txt",
                        "/oauth/**",
                        "/api/**",
                        "/notification/**",
                        "/**/*.html",
                        "/sitemap*.xml",
                        "/**/*.js",
                        "/**/*.css"
                ).permitAll()
                .anyRequest().hasRole(USER.name())
                .and()
                .exceptionHandling()
                // .accessDeniedHandler(new RedirectAccessDeniedHandler("/error_403.html"))
                .accessDeniedHandler(redirectAccessDeniedHandler())
                .authenticationEntryPoint(
                        new AuthenticationResponseEntryPoint("/login")
                                .apiMatcher(new AntPathRequestMatcher("/api/**"))
                                .authenticationResponseHandler(authenticationResponseHandler())
                )
                .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/index.html")
                .successHandler(authenticationSuccessHandler().targetUrlParam("request"))
                .failureHandler(authenticationFailureHandler().errorPage("/login?authentication_error=true"))
                .and()
                .logout()
                // TODO should using constant instead, and also should support the ajax logout
                .deleteCookies("SESSION")
                .invalidateHttpSession(true)
                .logoutUrl("/logout")
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout", GET.name()))
                .logoutSuccessHandler(webLogoutSuccessHandler())
                .permitAll()
                .and()
                .addFilterBefore(ssoFilter(), BasicAuthenticationFilter.class)
                .addFilterBefore(rememberMeFilter(), BasicAuthenticationFilter.class)
                .csrf()
                .disable()
//                    .requireCsrfProtectionMatcher(new OrRequestMatcher(
//                            new AntPathRequestMatcher("/login", POST.name()),
//                            new AntPathRequestMatcher("/oauth/confirm_access")
//                    ))
//                    .csrfTokenRepository(csrfTokenRepository()).and()
//                    .addFilterBefore(csrfCookieFilter(), CsrfFilter.class)
                .headers()
                .frameOptions().disable()
                .and()
                .rememberMe()
                .key(formLoginRememberMeServices.getKey())
                .rememberMeServices(formLoginRememberMeServices);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .parentAuthenticationManager(authenticationManager)
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
    }

    @Bean(name = "github")
    @ConfigurationProperties("github")
    protected DefaultClientResources github() {
        return new DefaultClientResources();
    }

    @Bean(name = "weibo")
    @ConfigurationProperties("weibo")
    protected WeiboClientResources weibo() {
        return new WeiboClientResources();
    }

    @Bean(name = "qq")
    @ConfigurationProperties("qq")
    protected QQClientResources qq() {
        return new QQClientResources();
    }

    @Bean(name = "wx")
    @ConfigurationProperties("wx")
    protected WechatClientResources wechat() {
        return new WechatClientResources();
    }

    @Bean(name = "wx-web")
    @ConfigurationProperties("wx-web")
    protected WechatClientResources wechatWeb() {
        return new WechatClientResources();
    }

    @Bean
    protected FilterRegistrationBean oAuth2ClientFilterRegistration() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new OAuth2ClientContextFilter());
        registrationBean.setOrder(-100);
        return registrationBean;
    }

    @Bean
    public WebAuthenticationSuccessHandler authenticationSuccessHandler() {
        return new WebAuthenticationSuccessHandler();
    }

    @Bean(name = "formLogin")
    public WebAuthenticationFailureHandler authenticationFailureHandler() {
        return new WebAuthenticationFailureHandler();
    }

    @Bean
    public ApiAuthenticationResponseHandler authenticationResponseHandler() {
        return new ApiAuthenticationResponseHandler();
    }

    @Bean
    public LogoutSuccessHandler webLogoutSuccessHandler() {
        return new WebLogoutSuccessHandler();
    }

    @Bean
    public RedirectAccessDeniedHandler redirectAccessDeniedHandler() {
        return new RedirectAccessDeniedHandler();
    }

    @Bean
    public CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
        repository.setHeaderName(Header.HEADER_X_XSRF_TOKEN);
        return repository;
    }

    @Bean
    @ConditionalOnMissingBean(PasswordEncoder.class)
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public CsrfCookieFilter csrfCookieFilter() {
//        return new CsrfCookieFilter();
//    }

    private Filter ssoFilter() {
        CompositeFilter filter = new CompositeFilter();
        List<Filter> filters = new ArrayList<>();
        filters.add(github().filter("/login/github"));
        filters.add(weibo().filter("/login/weibo"));
        filters.add(qq().filter("/login/qq"));
        filters.add(wechatWeb().filter("/login/wechat/connect"));
        filters.add(wechat().filter("/login/wechat"));
        filter.setFilters(filters);
        return filter;
    }

    private Filter rememberMeFilter() {
        ProviderManager providerManager = new ProviderManager(newArrayList(new RememberMeAuthenticationProvider(oauth2RememberMeServices.getKey())));
        return new RememberMeAuthenticationFilter(providerManager, oauth2RememberMeServices);
    }
}
