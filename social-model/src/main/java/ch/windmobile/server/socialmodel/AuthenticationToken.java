package ch.windmobile.server.socialmodel;

public interface AuthenticationToken {
	/**
	 * Return true if the current authentication token has been authenticate
	 * @return
	 */
	boolean isAuthenticate();
	
	/**
	 * If authenticate, this will return the tokenID associated with this authentication
	 * @return
	 */
	String getTokenIdentifier();
	
	/**
	 * Return the user name pseudo for this authentication token
	 * @return
	 */
	String getUserPseudo();
}
