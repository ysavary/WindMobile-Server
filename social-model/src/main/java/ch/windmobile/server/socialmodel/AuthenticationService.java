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
