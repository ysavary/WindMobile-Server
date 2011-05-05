package ch.windmobile.server.socialmodel;

import ch.windmobile.server.socialmodel.xml.User;

public interface UserService {

    User findByEmail(String email) throws UserNotFound;

    User findByPseudo(String pseudo) throws UserNotFound;

    static class UserNotFound extends Exception {
        private static final long serialVersionUID = 1L;

        public UserNotFound(String message) {
            super(message);
        }
    }
}
