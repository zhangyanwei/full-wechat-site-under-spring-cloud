package com.askdog.oauth.client.security;

import java.util.Map;

/**
 * Strategy used by {@link UserInfoTokenServices} to extract the principal from the
 * resource server's response.
 *
 * @author Phillip Webb
 * @since 1.4.0
 */
public interface PrincipalExtractor {

	/**
	 * Extract the principal that should be used for the token.
	 * @param map the source map
	 * @return the extracted principal or {@code null}
	 */
	Object extractPrincipal(Map<String, Object> map);

}