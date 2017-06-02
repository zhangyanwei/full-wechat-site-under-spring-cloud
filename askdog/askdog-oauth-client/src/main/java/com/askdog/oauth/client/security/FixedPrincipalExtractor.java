package com.askdog.oauth.client.security;

import java.util.Map;

/**
 * Default implementation of {@link PrincipalExtractor}. Extracts the principal from the
 * map with well known keys.
 *
 * @author Phillip Webb
 * @since 1.4.0
 */
public class FixedPrincipalExtractor implements PrincipalExtractor {

	private static final String[] PRINCIPAL_KEYS = new String[] { "user", "username",
			"userid", "user_id", "login", "id", "name" };

	@Override
	public Object extractPrincipal(Map<String, Object> map) {
		for (String key : PRINCIPAL_KEYS) {
			if (map.containsKey(key)) {
				return map.get(key);
			}
		}
		return null;
	}

}