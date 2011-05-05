package ch.windmobile.server.socialmodel;

public interface AuthenticationService {

    boolean authenticate(String email, Object password) throws AuthenticationServiceException;

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
