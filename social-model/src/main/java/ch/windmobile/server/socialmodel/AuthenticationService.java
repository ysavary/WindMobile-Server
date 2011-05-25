package ch.windmobile.server.socialmodel;

public interface AuthenticationService {

    /**
     * @param email which is the id the account
     * @param password
     * @return the highest role
     * @throws AuthenticationServiceException
     */
    String authenticate(String email, Object password) throws AuthenticationServiceException;

    static class AuthenticationServiceException extends Exception {
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
