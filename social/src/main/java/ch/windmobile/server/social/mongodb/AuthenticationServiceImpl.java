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
        DBCollection col = database.getCollection(MongoDBConstants.COLLECTION_USERS);
        // Search user by email
        DBObject user = col.findOne(new BasicDBObject(MongoDBConstants.USER_PROP_EMAIL, email));
        if (user != null) {
            String b64 = (String) user.get(MongoDBConstants.USER_PROP_SHA1);
            try {
                boolean ok = new AuthenticationServiceUtil().validateSHA1(email, password.toString(), b64);
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
