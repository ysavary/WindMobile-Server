package ch.windmobile.server.socialmodel;

/**
 * Service user to authenticate a token
 *
 */
public interface AuthenticationService {
	
	/**
	 * Authenticate a token a return a NEW token to use within the application.
	 * @param source Source token
	 * @return Return a new authentication token
	 */
	AuthenticationToken authenticate( AuthenticationToken source ) throws AuthenticationServiceException;
	
	/**
	 * Get an authentication token for a given ID, if the tokenID does not exist, this method returns null.
	 * Method may return null for multiple reason:
	 * 1) the token have never been created
	 * 2) the token has expired
	 * 3) the token has been revoked 
	 * @param tokenId Identifier for the token
	 * @return
	 */
	AuthenticationToken getAuthenticationToken( String tokenId );
	
	
	static class AuthenticationServiceException extends Exception{
		private static final long serialVersionUID = 1L;

		public AuthenticationServiceException() {
			super();
		}

		public AuthenticationServiceException(String message, Throwable cause) {
			super(message, cause);
		}

		public AuthenticationServiceException(String message) {
			super(message);
		}

		public AuthenticationServiceException(Throwable cause) {
			super(cause);
		}
			
		
	}
}
