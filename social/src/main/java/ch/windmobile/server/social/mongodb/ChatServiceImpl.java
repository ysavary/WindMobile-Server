package ch.windmobile.server.social.mongodb;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

import ch.windmobile.server.socialmodel.AuthenticationService;
import ch.windmobile.server.socialmodel.AuthenticationService.AuthenticationServiceException;
import ch.windmobile.server.socialmodel.AuthenticationToken;
import ch.windmobile.server.socialmodel.xml.Message;
import ch.windmobile.server.socialmodel.xml.Messages;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class ChatServiceImpl extends BaseMongoDBService implements ch.windmobile.server.socialmodel.ChatService {
    private final boolean allowAnonymous = Boolean.getBoolean("ch.epyx.wind-mobile.allowAnonymousChat");
    private final AuthenticationService authenticationService;

    public ChatServiceImpl(DB database, AuthenticationService authenticationService) {
        super(database);
        this.authenticationService = authenticationService;
    }

    @Override
    public void postMessage(final String chatRoomId, final String pseudo, final String message) {
        /*
        final String pseudo;
        if (sessionId == null) {
            if (allowAnonymous == false) {
                throw new IllegalArgumentException("Unable to push chat on anonymous user");
            } else {
                pseudo = "anonymous";
            }
        } else {
            pseudo = lookupUserPseudoForSessionIdentifier(sessionId);
        }
        */

        // create collection if does not exist
        final String collectionName = computeCollectionName(chatRoomId);
        DBCollection col;
        try {
            // try to create the collection, it may raise an exception if already create, in this case just ignore it
            // and get the collection
            // Due to the "consistency" behavior od mongoDB trying to first check if the collection exist the create it
            // will not work, race condition will certainly occurs...
            col = createChatCappedCollection(collectionName);
        } catch (Exception ex) {
            col = database.getCollection(collectionName);
        }

        DBObject chatItem = new BasicDBObject();
        chatItem.put(MongoDBConstants.CHAT_PROP_COMMENT, message);
        chatItem.put(MongoDBConstants.CHAT_PROP_USER, pseudo);
        chatItem.put(MongoDBConstants.CHAT_PROP_TIME, new DateTime().toDateTimeISO().toString());
        col.insert(chatItem);
    }

    private DBCollection createChatCappedCollection(String collectionName) {
        DBObject options = BasicDBObjectBuilder.start("capped", true).add("size", 10000).get();
        // create the capped chat room of 10K
        return database.createCollection(collectionName, options);
    }

    private String lookupUserPseudoForSessionIdentifier(String userSessionId) throws SecurityException {
        try {
            AuthenticationToken token = authenticationService.getAuthenticationToken(userSessionId);
            return token.getPseudo();
        } catch (AuthenticationServiceException e) {
            throw new SecurityException(e);
        }
    }

    @Override
    public Messages findMessages(String chatRoomId, int maxCount) {
        if (maxCount <= 0) {
            throw new IllegalArgumentException("maxCount arg[1] must be greater than 0");
        }
        final String collectionName = computeCollectionName(chatRoomId);

        final DBObject fields = BasicDBObjectBuilder.start("user", 1).add("_id", 0).add("comment", 1).add("time", 1).get();
        DBCursor result = database.getCollection(collectionName).find(null, fields).sort(new BasicDBObject("$natural", -1)).limit(maxCount);
        List<DBObject> all = result.toArray();

        Messages messages = new Messages();
        for (DBObject dbObject : all) {
            Message message = new Message();
            DateTime dateTime = ISODateTimeFormat.dateTime().parseDateTime((String) dbObject.get("time"));
            message.setDate(dateTime);
            message.setPseudo((String) dbObject.get("user"));
            message.setText((String) dbObject.get("comment"));
            messages.getMessages().add(message);
        }
        return messages;
    }

    private String computeCollectionName(final String chatRoomId) {
        return MongoDBConstants.COL_CHAT_ROOM_PREFIX + chatRoomId;
    }
}
