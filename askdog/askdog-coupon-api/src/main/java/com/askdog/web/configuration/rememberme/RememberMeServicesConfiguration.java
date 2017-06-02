package com.askdog.web.configuration.rememberme;

import com.askdog.service.ExternalUserService;
import com.askdog.web.configuration.WebMvcConfiguration;
import com.askdog.web.configuration.userdetails.OAuthRememberMeUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration
public class RememberMeServicesConfiguration {

    private static final String PARAM_REMEMBER_ME = "remember-me";

    @Value("${askdog.remember-me.token.key:54ed0fb2b50b4bcf}")
    private String rememberMeTokenKey;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private WebMvcConfiguration.WebSettings webSettings;

    @Autowired
    private ExternalUserService externalUserService;

    @Bean
    @Qualifier("formLogin")
    public PersistentTokenBasedRememberMeServices formLoginRememberMeServices() {
        PersistentTokenBasedRememberMeServices rememberMeServices = new TokenRememberMeServices(
                rememberMeTokenKey,
                userDetailsService,
                persistentTokenRepository(),
                webSettings.getRememberMeCookieDomain()
        );

        rememberMeServices.setParameter(PARAM_REMEMBER_ME);
        rememberMeServices.setAlwaysRemember(true);
        return rememberMeServices;
    }

    @Bean
    @Qualifier("oauth2")
    public PersistentTokenBasedRememberMeServices oauth2RememberMeServices() {
        PersistentTokenBasedRememberMeServices rememberMeServices = new TokenRememberMeServices(
                rememberMeTokenKey,
                new OAuthRememberMeUserDetailsService(externalUserService),
                persistentTokenRepository(),
                webSettings.getRememberMeCookieDomain()
        );

        rememberMeServices.setCookieName("oauth-remember-me");
        rememberMeServices.setAlwaysRemember(true);
        return rememberMeServices;
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl db = new JdbcTokenRepositoryImpl();
        db.setDataSource(dataSource);
        return db;
    }

}
