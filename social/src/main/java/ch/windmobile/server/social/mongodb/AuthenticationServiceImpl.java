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
package ch.windmobile.server.social.mongodb;

import java.security.NoSuchAlgorithmException;

import ch.windmobile.server.social.mongodb.util.AuthenticationServiceUtil;
import ch.windmobile.server.socialmodel.AuthenticationService;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class AuthenticationServiceImpl extends BaseMongoDBService implements AuthenticationService {

    public AuthenticationServiceImpl(DB database) {
        super(database);
    }

    @Override
    public String authenticate(final String email, final Object password) throws AuthenticationServiceException {
        if (password == null) {
            throw new IllegalArgumentException("Password cannot be null");
        }
        DBCollection col = db.getCollection(MongoDBConstants.COLLECTION_USERS);
        // Search user by email
        DBObject user = col.findOne(new BasicDBObject(MongoDBConstants.USER_PROP_EMAIL, email));
        if (user != null) {
            String b64 = (String) user.get(MongoDBConstants.USER_PROP_SHA1);
            try {
                boolean ok = AuthenticationServiceUtil.validateSHA1(email, password.toString(), b64);
                if (ok) {
                    return (String) user.get(MongoDBConstants.USER_PROP_ROLE);
                } else {
                    throw new AuthenticationService.AuthenticationServiceException("Invalid password");
                }
            } catch (NoSuchAlgorithmException e) {
                throw new AuthenticationService.AuthenticationServiceException("Unexcepted error : " + e.getMessage());
            }
        }
        throw new AuthenticationService.AuthenticationServiceException("User not found");
    }
}
