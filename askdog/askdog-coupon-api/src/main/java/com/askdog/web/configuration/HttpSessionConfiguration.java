package com.askdog.web.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.session.Session;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.CookieHttpSessionStrategy;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.session.web.http.HeaderHttpSessionStrategy;
import org.springframework.session.web.http.HttpSessionStrategy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.google.common.base.Strings.isNullOrEmpty;

@Configuration
@EnableRedisHttpSession
@EnableConfigurationProperties(RedisProperties.class)
public class HttpSessionConfiguration {

    @Autowired
    private RedisProperties redisProperties;

    @Bean
    public JedisConnectionFactory connectionFactory() {
        JedisConnectionFactory factory = new JedisConnectionFactory();
        factory.setHostName(redisProperties.getHost());
        factory.setPort(redisProperties.getPort());
        factory.setPassword(redisProperties.getPassword());
        return factory;
    }

    @Bean
    public HttpSessionStrategy httpSessionStrategy(WebMvcConfiguration.WebSettings webSettings) {
        return new WebSessionStrategy(webSettings.getCookieDomainNamePattern());
    }

    @SuppressWarnings("WeakerAccess")
    public final static class WebSessionStrategy implements HttpSessionStrategy {

        private HeaderHttpSessionStrategy headerHttpSessionStrategy;
        private CookieHttpSessionStrategy cookieHttpSessionStrategy;

        public WebSessionStrategy(String cookieDomainNamePattern) {
            this.headerHttpSessionStrategy = new HeaderHttpSessionStrategy();
            this.cookieHttpSessionStrategy = new CookieHttpSessionStrategy();
            DefaultCookieSerializer cookieSerializer = new DefaultCookieSerializer();
            if (!isNullOrEmpty(cookieDomainNamePattern)) {
                cookieSerializer.setDomainNamePattern(cookieDomainNamePattern);
            }

            this.cookieHttpSessionStrategy.setCookieSerializer(cookieSerializer);
        }

        @Override
        public String getRequestedSessionId(HttpServletRequest request) {
            String sessionId = this.headerHttpSessionStrategy.getRequestedSessionId(request);
            if (isNullOrEmpty(sessionId)) {
                sessionId = this.cookieHttpSessionStrategy.getRequestedSessionId(request);
            }

            return sessionId;
        }

        @Override
        public void onNewSession(Session session, HttpServletRequest request, HttpServletResponse response) {
            this.headerHttpSessionStrategy.onNewSession(session, request, response);
            this.cookieHttpSessionStrategy.onNewSession(session, request, response);
        }

        @Override
        public void onInvalidateSession(HttpServletRequest request, HttpServletResponse response) {
            this.headerHttpSessionStrategy.onInvalidateSession(request, response);
            this.cookieHttpSessionStrategy.onInvalidateSession(request, response);
        }
    }

}
