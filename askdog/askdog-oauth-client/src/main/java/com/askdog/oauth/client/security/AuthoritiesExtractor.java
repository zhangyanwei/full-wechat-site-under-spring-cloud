package com.askdog.oauth.client.security;

import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;

/**
 * Strategy used by {@link UserInfoTokenServices} to extract authorities from the resource
 * server's response.
 *
 * @author Dave Syer
 * @since 1.3.0
 */
public interface AuthoritiesExtractor {

	/**
	 * Extract the authorities from the resource server's response.
	 * @param map the response
	 * @return the extracted authorities
	 */
	List<GrantedAuthority> extractAuthorities(Map<String, Object> map);

}