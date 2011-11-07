package ch.windmobile.server.social.mongodb;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

import ch.windmobile.server.socialmodel.ChatService;
import ch.windmobile.server.socialmodel.xml.Message;
import ch.windmobile.server.socialmodel.xml.Messages;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class ChatServiceImpl extends BaseMongoDBService implements ChatService {
    private static final String counter = "function counter(name) { return db." + MongoDBConstants.COLLECTION_CHATROOMS
        + ".findAndModify({query:{_id:name}, update:{$inc : {counter:1}}, new:true, upsert:true}).counter; }";

    public ChatServiceImpl(DB database) {
        super(database);
    }

    private String computeCollectionName(final String chatRoomId) {
        return MongoDBConstants.COLLECTION_CHAT_ROOM_PREFIX + chatRoomId;
    }

    private DBCollection createChatCappedCollection(String collectionName) {
        DBObject options = BasicDBObjectBuilder.start("capped", true).add("size", 10000).get();
        // create the capped chat room of 10K
        return db.createCollection(collectionName, options);
    }

    public DBCollection getOrCreateCappedCollection(String collectionName) {
        DBCollection col;
        try {
            // try to create the collection, it may raise an exception if already create, in this case just ignore it
            // and get the collection
            // Due to the "consistency" behavior of mongoDB trying to first check if the collection exist the create it
            // will not work, race condition will certainly occurs...
            col = createChatCappedCollection(collectionName);
        } catch (Exception ex) {
            col = db.getCollection(collectionName);
        }
        return col;
    }

    @Override
    public Message postMessage(String chatRoomId, String pseudo, String text, String emailHash) {
        String collectionName = computeCollectionName(chatRoomId);
        DBCollection col = getOrCreateCappedCollection(collectionName);

        DBObject chatItem = new BasicDBObject();
        Double id = (Double) db.eval(counter, chatRoomId);
        chatItem.put("_id", id.longValue());
        chatItem.put(MongoDBConstants.CHAT_PROP_TEXT, text);
        chatItem.put(MongoDBConstants.CHAT_PROP_USER, pseudo);
        DateTime date = new DateTime().toDateTimeISO();
        chatItem.put(MongoDBConstants.CHAT_PROP_TIME, date.toString());
        chatItem.put(MongoDBConstants.CHAT_PROP_EMAIL_HASH, emailHash);
        col.insert(chatItem);

        Message message = new Message();
        message.setId(id.toString());
        message.setDate(date);
        message.setPseudo(pseudo);
        message.setText(text);
        message.setEmailHash(emailHash);

        return message;
    }

    @Override
    public Messages findMessages(String chatRoomId, int maxCount) {
        if (maxCount <= 0) {
            throw new IllegalArgumentException("maxCount arg[1] must be greater than 0");
        }
        final String collectionName = computeCollectionName(chatRoomId);

        final DBObject fields = BasicDBObjectBuilder.start("_id", 1).add(MongoDBConstants.CHAT_PROP_USER, 1)
            .add(MongoDBConstants.CHAT_PROP_TEXT, 1).add(MongoDBConstants.CHAT_PROP_TIME, 1).add(MongoDBConstants.CHAT_PROP_EMAIL_HASH, 1).get();
        DBCursor result = db.getCollection(collectionName).find(null, fields).sort(new BasicDBObject("$natural", -1)).limit(maxCount);
        List<DBObject> all = result.toArray();

        Messages messages = new Messages();
        for (DBObject dbObject : all) {
            Message message = new Message();
            message.setId(dbObject.get("_id").toString());
            DateTime dateTime = ISODateTimeFormat.dateTime().parseDateTime((String) dbObject.get("time"));
            message.setDate(dateTime);
            message.setPseudo((String) dbObject.get(MongoDBConstants.CHAT_PROP_USER));
            message.setText((String) dbObject.get(MongoDBConstants.CHAT_PROP_TEXT));
            message.setEmailHash((String) dbObject.get(MongoDBConstants.CHAT_PROP_EMAIL_HASH));

            messages.getMessages().add(message);
        }
        return messages;
    }

    @Override
    public Long getLastMessageId(String chatRoomId) {
        try {
            Double value = (Double) db.getCollection(MongoDBConstants.COLLECTION_CHATROOMS).findOne(new BasicDBObject("_id", chatRoomId))
                .get("counter");
            return value.longValue();
        } catch (Exception e) {
            return -1L;
        }
    }

    @Override
    public List<Long> getLastMessageIds(List<String> chatRoomIds) {
        List<Long> returnValue = new ArrayList<Long>();
        for (String chatRoomId : chatRoomIds) {
            returnValue.add(getLastMessageId(chatRoomId));
        }
        return returnValue;
    }

    @Override
    public boolean allowAnonymousMessages(String chatRoomId) {
        try {
            Boolean refuseAnonymous = (Boolean) db.getCollection(MongoDBConstants.COLLECTION_CHATROOMS).findOne(new BasicDBObject("_id", chatRoomId))
                .get(MongoDBConstants.CHATROOM_REFUSE_ANONYMOUS);

            return !refuseAnonymous;
        } catch (Exception e) {
            return true;
        }
    }
}
