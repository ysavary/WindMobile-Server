package ch.windmobile.server.socialmodel;

public interface AuthenticationToken {
	
	/**
	 * If authenticate, this will return the session ID associated with this authentication
	 * @return
	 */
	String getSessionIdentifier();
	
	/**
	 * Return the user pseudo name for this authentication token
	 * @return
	 */
	String getPseudo();
	
}
