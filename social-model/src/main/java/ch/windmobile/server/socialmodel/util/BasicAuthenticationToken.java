package ch.windmobile.server.socialmodel.util;

import ch.windmobile.server.socialmodel.AuthenticationToken;

/**
 * Basic implementation for authentication token based on a username / password mechanism
 *
 */
public class BasicAuthenticationToken implements AuthenticationToken{
	private final String sessionIdentifier;
	private final String pseudo;

	public BasicAuthenticationToken(String sessionId,String pseudo) {
		this.sessionIdentifier = sessionId;
		this.pseudo = pseudo;

	}
	
	@Override
	public String getSessionIdentifier() {
		return sessionIdentifier;
	}

	@Override
	public String getPseudo() {
		return pseudo;
	}

}
