package ch.windmobile.server.social.mongodb;

import java.util.List;

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
        user.setRole((String) dbObject.get(MongoDBConstants.USER_PROP_ROLE));
        return user;
    }

    @Override
    public User findByEmail(String email) throws UserNotFound {
        if (email == null) {
            throw new IllegalArgumentException("Email cannot be null");
        }
        DBCollection col = database.getCollection(MongoDBConstants.COLLECTION_USERS);
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
        DBCollection col = database.getCollection(MongoDBConstants.COLLECTION_USERS);
        // Search user by pseudo
        DBObject dbObject = col.findOne(new BasicDBObject(MongoDBConstants.USER_PROP_PSEUDO, pseudo));
        if (dbObject == null) {
            throw new UserNotFound("Unable to find user with pseudo '" + pseudo + "'");
        }
        return createUser(dbObject);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> getFavorites(String email) throws UserNotFound {
        if (email == null) {
            throw new IllegalArgumentException("Email cannot be null");
        }
        DBCollection col = database.getCollection(MongoDBConstants.COLLECTION_USERS);
        // Search user by email
        DBObject dbObject = col.findOne(new BasicDBObject(MongoDBConstants.USER_PROP_EMAIL, email));
        if (dbObject == null) {
            throw new UserNotFound("Unable to find user with email '" + email + "'");
        }

        return (List<String>) dbObject.get(MongoDBConstants.USER_PROP_FAVORITES);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> addToFavorites(String email, List<String> favoritesToAdd) throws UserNotFound {
        if (email == null) {
            throw new IllegalArgumentException("Email cannot be null");
        }
        DBCollection col = database.getCollection(MongoDBConstants.COLLECTION_USERS);
        // Search user by email
        DBObject dbObject = col.findOne(new BasicDBObject(MongoDBConstants.USER_PROP_EMAIL, email));
        if (dbObject == null) {
            throw new UserNotFound("Unable to find user with email '" + email + "'");
        }

        List<String> favorites = (List<String>) dbObject.get(MongoDBConstants.USER_PROP_FAVORITES);
        favorites.addAll(favoritesToAdd);

        dbObject.put(MongoDBConstants.USER_PROP_FAVORITES, favorites);
        col.save(dbObject);

        return favorites;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> removeFromFavorites(String email, List<String> favoritesToRemove) throws UserNotFound {
        if (email == null) {
            throw new IllegalArgumentException("Email cannot be null");
        }
        DBCollection col = database.getCollection(MongoDBConstants.COLLECTION_USERS);
        // Search user by email
        DBObject dbObject = col.findOne(new BasicDBObject(MongoDBConstants.USER_PROP_EMAIL, email));
        if (dbObject == null) {
            throw new UserNotFound("Unable to find user with email '" + email + "'");
        }

        List<String> favorites = (List<String>) dbObject.get(MongoDBConstants.USER_PROP_FAVORITES);
        favorites.removeAll(favoritesToRemove);

        dbObject.put(MongoDBConstants.USER_PROP_FAVORITES, favorites);
        col.save(dbObject);

        return favorites;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void clearFavorites(String email) throws UserNotFound {
        if (email == null) {
            throw new IllegalArgumentException("Email cannot be null");
        }
        DBCollection col = database.getCollection(MongoDBConstants.COLLECTION_USERS);
        // Search user by email
        DBObject dbObject = col.findOne(new BasicDBObject(MongoDBConstants.USER_PROP_EMAIL, email));
        if (dbObject == null) {
            throw new UserNotFound("Unable to find user with email '" + email + "'");
        }

        List<String> favorites = (List<String>) dbObject.get(MongoDBConstants.USER_PROP_FAVORITES);
        favorites.clear();

        dbObject.put(MongoDBConstants.USER_PROP_FAVORITES, favorites);
        col.save(dbObject);
    }
}
