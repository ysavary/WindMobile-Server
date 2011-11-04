package ch.windmobile.server.social.mongodb;

import java.util.ArrayList;
import java.util.List;

import ch.windmobile.server.socialmodel.UserService;
import ch.windmobile.server.socialmodel.xml.Favorite;
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
        DBCollection col = db.getCollection(MongoDBConstants.COLLECTION_USERS);
        // Search user by email
        DBObject userDb = col.findOne(new BasicDBObject(MongoDBConstants.USER_PROP_EMAIL, email));
        if (userDb == null) {
            throw new UserNotFound("Unable to find user with email '" + email + "'");
        }
        return createUser(userDb);
    }

    @Override
    public User findByPseudo(String pseudo) throws UserNotFound {
        if (pseudo == null) {
            throw new IllegalArgumentException("Pseudo cannot be null");
        }
        DBCollection col = db.getCollection(MongoDBConstants.COLLECTION_USERS);
        // Search user by pseudo
        DBObject userDb = col.findOne(new BasicDBObject(MongoDBConstants.USER_PROP_PSEUDO, pseudo));
        if (userDb == null) {
            throw new UserNotFound("Unable to find user with pseudo '" + pseudo + "'");
        }
        return createUser(userDb);
    }

    @Override
    public List<Favorite> getFavorites(String email) throws UserNotFound {
        if (email == null) {
            throw new IllegalArgumentException("Email cannot be null");
        }
        DBCollection col = db.getCollection(MongoDBConstants.COLLECTION_USERS);
        // Search user by email
        DBObject userDb = col.findOne(new BasicDBObject(MongoDBConstants.USER_PROP_EMAIL, email));
        if (userDb == null) {
            throw new UserNotFound("Unable to find user with email '" + email + "'");
        }

        DBObject favoritesDb = (DBObject) userDb.get(MongoDBConstants.USER_PROP_FAVORITES);
        if (favoritesDb == null) {
            favoritesDb = new BasicDBObject();

            userDb.put(MongoDBConstants.USER_PROP_FAVORITES, favoritesDb);
            col.save(userDb);
        }

        List<Favorite> returnValue = new ArrayList<Favorite>(favoritesDb.keySet().size());
        for (String stationId : favoritesDb.keySet()) {
            Favorite favorite = new Favorite();
            favorite.setStationId(stationId);
            DBObject favoriteItemsDb = (DBObject) favoritesDb.get(stationId);
            favorite.setLastMessageId((Long) favoriteItemsDb.get(MongoDBConstants.USER_PROP_FAVORITE_LASTMESSAGEID));
            returnValue.add(favorite);
        }
        return returnValue;
    }

    @Override
    public List<Favorite> addToFavorites(String email, List<Favorite> localFavorites) throws UserNotFound {
        if (email == null) {
            throw new IllegalArgumentException("Email cannot be null");
        }
        DBCollection col = db.getCollection(MongoDBConstants.COLLECTION_USERS);
        // Search user by email
        DBObject userDb = col.findOne(new BasicDBObject(MongoDBConstants.USER_PROP_EMAIL, email));
        if (userDb == null) {
            throw new UserNotFound("Unable to find user with email '" + email + "'");
        }

        DBObject favoritesDb = (DBObject) userDb.get(MongoDBConstants.USER_PROP_FAVORITES);
        if (favoritesDb == null) {
            favoritesDb = new BasicDBObject();
        }

        if (localFavorites != null) {
            for (Favorite localFavorite : localFavorites) {
                DBObject favoriteItemsDb = (DBObject) favoritesDb.get(localFavorite.getStationId());
                long lastMessageIdDb = -1;
                if (favoriteItemsDb == null) {
                    favoriteItemsDb = new BasicDBObject();
                } else {
                    if (favoriteItemsDb.containsField(MongoDBConstants.USER_PROP_FAVORITE_LASTMESSAGEID)) {
                        lastMessageIdDb = (Long) favoriteItemsDb.get(MongoDBConstants.USER_PROP_FAVORITE_LASTMESSAGEID);
                    }
                }

                favoriteItemsDb.put(MongoDBConstants.USER_PROP_FAVORITE_LASTMESSAGEID, Math.max(lastMessageIdDb, localFavorite.getLastMessageId()));
                favoritesDb.put(localFavorite.getStationId(), favoriteItemsDb);
            }
        }
        userDb.put(MongoDBConstants.USER_PROP_FAVORITES, favoritesDb);
        col.save(userDb);

        List<Favorite> returnValue = new ArrayList<Favorite>(favoritesDb.keySet().size());
        for (String stationId : favoritesDb.keySet()) {
            Favorite favorite = new Favorite();
            favorite.setStationId(stationId);
            DBObject favoriteItemDb = (DBObject) favoritesDb.get(stationId);
            favorite.setLastMessageId((Long) favoriteItemDb.get(MongoDBConstants.USER_PROP_FAVORITE_LASTMESSAGEID));
            returnValue.add(favorite);
        }
        return returnValue;
    }

    @Override
    public List<Favorite> removeFromFavorites(String email, List<Favorite> favoritesToRemove) throws UserNotFound {
        if (email == null) {
            throw new IllegalArgumentException("Email cannot be null");
        }
        DBCollection col = db.getCollection(MongoDBConstants.COLLECTION_USERS);
        // Search user by email
        DBObject userDb = col.findOne(new BasicDBObject(MongoDBConstants.USER_PROP_EMAIL, email));
        if (userDb == null) {
            throw new UserNotFound("Unable to find user with email '" + email + "'");
        }

        DBObject favoritesDb = (DBObject) userDb.get(MongoDBConstants.USER_PROP_FAVORITES);
        if (favoritesDb == null) {
            favoritesDb = new BasicDBObject();
        }

        if (favoritesToRemove != null) {
            for (Favorite favoriteToRemove : favoritesToRemove) {
                DBObject favoriteItemsDb = (DBObject) favoritesDb.get(favoriteToRemove.getStationId());
                if (favoriteItemsDb != null) {
                    favoritesDb.removeField(favoriteToRemove.getStationId());
                }
            }
        }
        userDb.put(MongoDBConstants.USER_PROP_FAVORITES, favoritesDb);
        col.save(userDb);

        List<Favorite> returnValue = new ArrayList<Favorite>(favoritesDb.keySet().size());
        for (String stationId : favoritesDb.keySet()) {
            Favorite favorite = new Favorite();
            favorite.setStationId(stationId);
            DBObject favoriteItemDb = (DBObject) favoritesDb.get(stationId);
            favorite.setLastMessageId((Long) favoriteItemDb.get(MongoDBConstants.USER_PROP_FAVORITE_LASTMESSAGEID));
            returnValue.add(favorite);
        }
        return returnValue;
    }

    @Override
    public void clearFavorites(String email) throws UserNotFound {
        if (email == null) {
            throw new IllegalArgumentException("Email cannot be null");
        }
        DBCollection col = db.getCollection(MongoDBConstants.COLLECTION_USERS);
        // Search user by email
        DBObject userDb = col.findOne(new BasicDBObject(MongoDBConstants.USER_PROP_EMAIL, email));
        if (userDb == null) {
            throw new UserNotFound("Unable to find user with email '" + email + "'");
        }

        userDb.removeField(MongoDBConstants.USER_PROP_FAVORITES);
        col.save(userDb);
    }
}
