package ch.windmobile.server.socialmodel;

import java.util.List;

import ch.windmobile.server.socialmodel.xml.Favorite;
import ch.windmobile.server.socialmodel.xml.User;

public interface UserService {

	User findByEmail(String email) throws UserNotFound;

	User findByPseudo(String pseudo) throws UserNotFound;

	List<Favorite> getFavorites(String email) throws UserNotFound;

	List<Favorite> addToFavorites(String email, List<Favorite> localFavorites) throws UserNotFound;

	List<Favorite> removeFromFavorites(String email, List<Favorite> favorites) throws UserNotFound;

	void clearFavorites(String email) throws UserNotFound;

	static class UserNotFound extends Exception {
		private static final long serialVersionUID = 1L;

		public UserNotFound(String message) {
			super(message);
		}
	}
}
