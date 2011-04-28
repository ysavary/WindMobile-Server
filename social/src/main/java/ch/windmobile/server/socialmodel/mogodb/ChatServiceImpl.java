package ch.windmobile.server.socialmodel.mogodb;

import java.util.List;

import org.joda.time.DateTime;

import ch.windmobile.server.socialmodel.AuthenticationService;
import ch.windmobile.server.socialmodel.AuthenticationService.AuthenticationServiceException;
import ch.windmobile.server.socialmodel.AuthenticationToken;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

public class ChatServiceImpl extends BaseMongoDBService implements ch.windmobile.server.socialmodel.ChatService {
	private final boolean allowAnonymous = Boolean.getBoolean("ch.epyx.wind-mobile.allowAnonymousChat");
	private final AuthenticationService authenticationService;
	
	public ChatServiceImpl(DB database,AuthenticationService authenticationService) {
		super( database );
		this.authenticationService = authenticationService;
	}

	@Override
	public void pushChat(final String chatRoomIdentifier,final String userSessionId,final String message) {
		final String pseudo;
		if ( userSessionId == null) {
			if (allowAnonymous == false ) {
				throw new IllegalArgumentException("Unable to push chat on anonymous user");
			} else {
				pseudo = "anonymous";
			}
		} else {
			pseudo = lookupUserPseudoForSessionIdentifier( userSessionId );
		}
		
		// create collection if does not exist
		final String collectionName = computeCollectionName( chatRoomIdentifier );
		DBCollection col;
		try {
			// try to create the collection, it may raise an exception if already create, in this case just ignore it and get the collection
			// Due to the "consistency" behavior od mongoDB trying to first check if the collection exist the create it 
			// will not work, race condition will certainly occurs...
			col = createChatCappedCollection( collectionName );
		} catch ( Exception ex ) {
			col = database.getCollection( collectionName );
		}
		
		DBObject chatItem = new BasicDBObject();
		chatItem.put(MongoDBConstants.CHAT_PROP_COMMENT, message);
		chatItem.put(MongoDBConstants.CHAT_PROP_USER,pseudo);
		chatItem.put(MongoDBConstants.CHAT_PROP_TIME, new DateTime().toDateTimeISO().toString());
		col.insert( chatItem );
	}

	private DBCollection createChatCappedCollection( String collectionName ) {
		DBObject options = BasicDBObjectBuilder.start("capped", true).add("size", 10000).get();
		// create the capped chat room of 10K 
		return database.createCollection(collectionName, options);
	}
	
	private String lookupUserPseudoForSessionIdentifier(String userSessionId) throws SecurityException {
		try {
			AuthenticationToken token = authenticationService.getAuthenticationToken( userSessionId );
			return token.getPseudo();
		} catch (AuthenticationServiceException e) {
			throw new SecurityException( e );
		}
	}

	@Override
	public String findChatItems(String chatRoomIdentifier, int maxcount) {
 		if ( maxcount <= 0 ) {
			throw new IllegalArgumentException("maxcount arg[1] must be greater than 0");
		}
		final String collectionName = computeCollectionName( chatRoomIdentifier );
			
		// bypass transformation and get the json content directly
		//final String code = MessageFormat.format("db.{0}.find().limit("+maxcount+").sort('{'$natural:-1'}')" , collectionName,maxcount);
		final DBObject fields = BasicDBObjectBuilder.start("user", 1).add("_id", 0).add("comment", 1).add("time", 1).get();
	    DBCursor result = database.getCollection(collectionName).find(null,fields).sort(new BasicDBObject("$natural", -1)).limit(maxcount);
	    List<DBObject> all = result.toArray();
	    return JSON.serialize(all);
	}

	private String computeCollectionName(final String chatRoomId) {
		return MongoDBConstants.COL_CHAT_ROOM_PREFIX+chatRoomId ;
	}
}
