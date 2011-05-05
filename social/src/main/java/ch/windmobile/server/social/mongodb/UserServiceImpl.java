package ch.windmobile.server.social.mongodb;

import ch.windmobile.server.socialmodel.UserService;
import ch.windmobile.server.socialmodel.xml.User;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class UserServiceImpl extends BaseMongoDBService implements UserService {

    public UserServiceImpl(DB database) {
        super(database);
    }

    private User createUser(DBObject dbObject) {
        User user = new User();
        user.setEmail((String) dbObject.get(MongoDBConstants.USER_PROP_EMAIL));
        user.setPseudo((String) dbObject.get(MongoDBConstants.USER_PROP_PSEUDO));
        user.setFullName((String) dbObject.get(MongoDBConstants.USER_PROP_FULLNAME));
        return user;
    }

    @Override
    public User findByEmail(String email) throws UserNotFound {
        if (email == null) {
            throw new IllegalArgumentException("Email cannot be null");
        }
        DBCollection col = database.getCollection(MongoDBConstants.COL_USERS);
        // Search user by email
        DBObject dbObject = col.findOne(new BasicDBObject(MongoDBConstants.USER_PROP_EMAIL, email));
        if (dbObject == null) {
            throw new UserNotFound("Unable to find user with email '" + email + "'");
        }
        return createUser(dbObject);
    }

    @Override
    public User findByPseudo(String pseudo) throws UserNotFound {
        if (pseudo == null) {
            throw new IllegalArgumentException("Pseudo cannot be null");
        }
        DBCollection col = database.getCollection(MongoDBConstants.COL_USERS);
        // Search user by pseudo
        DBObject dbObject = col.findOne(new BasicDBObject(MongoDBConstants.USER_PROP_PSEUDO, pseudo));
        if (dbObject == null) {
            throw new UserNotFound("Unable to find user with pseudo '" + pseudo + "'");
        }
        return createUser(dbObject);
    }
}
