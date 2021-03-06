/*******************************************************************************
 * Copyright (c) 2011 epyx SA.
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Affero General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Affero General Public License along
 * with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
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
