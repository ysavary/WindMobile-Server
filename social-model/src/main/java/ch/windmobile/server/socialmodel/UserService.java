package ch.windmobile.server.socialmodel;

/**
 * User service
 *
 */
public interface UserService {
	User login(String username,char[] pwd);
	
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
