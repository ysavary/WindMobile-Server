package ch.windmobile.server.socialmodel;

/**
 * User service
 *
 */
public interface UserService {
	
	/**
	 * Login a user by providing username + password 
	 * and return a valid session ID
	 * @param username
	 * @param password
	 * @return
	 */
	String login(String username,char[] password);
	
	/**
	 * Exception thrown by a login operation
	 *
	 */
	static class LoginException extends RuntimeException {
		private static final long serialVersionUID = 1L;
		public LoginException(String message) {
			super( message );
		}
	}
}
