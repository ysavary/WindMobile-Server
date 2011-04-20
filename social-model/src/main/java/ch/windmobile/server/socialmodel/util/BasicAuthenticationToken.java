package ch.windmobile.server.socialmodel.util;

import ch.windmobile.server.socialmodel.AuthenticationToken;

/**
 * Basic implementation for authentication token based on a username / password mechanism
 *
 */
public class BasicAuthenticationToken implements AuthenticationToken{
	private final boolean authenticated;
	private final String username;
	private final Object password;
	private final String tokenIdentifier;
	private final String userPseudo;
	
	public BasicAuthenticationToken(String username,Object password) {
		this.authenticated = false;
		this.tokenIdentifier = null;
		this.username = username;
		this.password = password;
		this.userPseudo = null;
	}
	
	public BasicAuthenticationToken(BasicAuthenticationToken source,String authenticationTokenIdentifier,String userPseudo) {
		this.authenticated = true;
		this.tokenIdentifier = authenticationTokenIdentifier;
		this.userPseudo = userPseudo;
		this.username = new String(source.username);
		this.password = null; // reset password, do not copy nor link it !
	}

	@Override
	public boolean isAuthenticate() {
		return authenticated;
	}

	@Override
	public String getTokenIdentifier() {
		return tokenIdentifier;
	}

	public Object getPassword() {
		return password;
	}
	
	public String getUsername() {
		return username;
	}
	
	@Override
	public String getUserPseudo() {
		return userPseudo;
	}
}
