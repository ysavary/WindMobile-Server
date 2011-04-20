package ch.windmobile.server.socialmodel.util;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import ch.windmobile.server.socialmodel.AuthenticationService;
import ch.windmobile.server.socialmodel.AuthenticationToken;

/**
 * Simple implementation of authentication service for testing purpose.
 * This implementation does not check for multiple login
 *
 */
public class SimpleAuthenticationService implements AuthenticationService{
	private final Map<String,Object> authenticationMap;
	private final Map<String,AuthenticationToken> tokens = new ConcurrentHashMap<String,AuthenticationToken>();

	public SimpleAuthenticationService() {
		this( null );
	}
	
	public SimpleAuthenticationService(Map<String,Object> authenticationMap) {
		if ( authenticationMap != null ) {
			this.authenticationMap = Collections.unmodifiableMap( authenticationMap );
		} else {
			this.authenticationMap = Collections.emptyMap();
		}
	}

	@Override
	public AuthenticationToken authenticate(AuthenticationToken source) throws AuthenticationServiceException {
		if ( source instanceof BasicAuthenticationToken ) {
			Object p = authenticationMap.get(((BasicAuthenticationToken) source).getUsername());
			if ( p != null ) {
				if (p.equals( ((BasicAuthenticationToken)source).getPassword()) ) {
					String tokenId = UUID.randomUUID().toString();
					BasicAuthenticationToken token =  new BasicAuthenticationToken(((BasicAuthenticationToken)source),tokenId,((BasicAuthenticationToken) source).getUsername());
					tokens.put(tokenId,token);
					return token;
				} else {
					throw new AuthenticationServiceException("Invalid login");
				}
			} else {
				throw new AuthenticationServiceException("Unkown username/password");
			}
		} else {
			throw new AuthenticationServiceException("Unable to handle this kind of token");
		}
	}

	@Override
	public AuthenticationToken getAuthenticationToken(String tokenId) {
		return tokens.get( tokenId );
	}

	public void logout(String tokenId) {
		tokens.remove( tokenId );
	}
}
