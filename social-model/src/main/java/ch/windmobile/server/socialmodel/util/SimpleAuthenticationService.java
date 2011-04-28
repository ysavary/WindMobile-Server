package ch.windmobile.server.socialmodel.util;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import ch.windmobile.server.socialmodel.AuthenticationService;
import ch.windmobile.server.socialmodel.AuthenticationToken;

/**
 * Simple implementation of authentication service for testing purpose. This
 * implementation does not check for multiple login
 * 
 */
public class SimpleAuthenticationService implements AuthenticationService {
	private final Map<String, Object> authenticationMap;
	private final Map<String, AuthenticationToken> tokens = new ConcurrentHashMap<String, AuthenticationToken>();

	public SimpleAuthenticationService() {
		this(null);
	}

	public SimpleAuthenticationService(Map<String, Object> authenticationMap) {
		if (authenticationMap != null) {
			this.authenticationMap = Collections.unmodifiableMap(authenticationMap);
		} else {
			this.authenticationMap = Collections.emptyMap();
		}
	}

	@Override
	public String authenticate(String username, Object password) throws AuthenticationServiceException {
		Object p = authenticationMap.get(username);
		if (p != null) {
			if (p.equals( password )) {
				String tokenId = UUID.randomUUID().toString();
				BasicAuthenticationToken token = new BasicAuthenticationToken(tokenId,username);
				tokens.put(tokenId, token);
				return tokenId;
			} else {
				throw new AuthenticationServiceException("Invalid login");
			}
		} else {
			throw new AuthenticationServiceException("Unkown username/password");
		}

	}

	@Override
	public AuthenticationToken getAuthenticationToken(String tokenId) {
		return tokens.get(tokenId);
	}

	public void logout(String tokenId) {
		tokens.remove(tokenId);
	}
}
