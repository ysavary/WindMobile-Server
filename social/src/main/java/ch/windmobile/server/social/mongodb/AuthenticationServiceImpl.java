package ch.windmobile.server.social.mongodb;

import java.security.NoSuchAlgorithmException;

import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

import ch.windmobile.server.social.mongodb.util.UserServiceUtil;
import ch.windmobile.server.socialmodel.AuthenticationService;
import ch.windmobile.server.socialmodel.AuthenticationToken;
import ch.windmobile.server.socialmodel.util.BasicAuthenticationToken;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class AuthenticationServiceImpl extends BaseMongoDBService implements AuthenticationService {

	public AuthenticationServiceImpl(DB database) {
		super(database);
	}

	@Override
	public String authenticate(final String email,final Object password) throws AuthenticationServiceException {
		if ( password == null ) {
			throw new IllegalArgumentException("Password cannot be null");
		}
		DBCollection col = database.getCollection(MongoDBConstants.COL_USERS);
		// Search user by email
		DBObject result = col.findOne(new BasicDBObject(MongoDBConstants.USER_PROP_EMAIL, email));
		if ( result != null ) {
			String b64 = (String) result.get(MongoDBConstants.USER_PROP_SHA1);
			try {
				boolean ok = new UserServiceUtil().validateSHA1(email, password.toString(), b64);
				if ( ok ) {
					return createSession( result );
				} else {
					throw new AuthenticationService.AuthenticationServiceException("Invalid password");
				}
			} catch (NoSuchAlgorithmException e) {
				throw new AuthenticationService.AuthenticationServiceException("Unexcepted error : "+e.getMessage());
			}
		}
		throw new AuthenticationService.AuthenticationServiceException("User not found");
	}

	private String createSession(DBObject sourceUserObject) {
		final DBCollection col = database.getCollection(MongoDBConstants.COL_SESSIONS);
		final String expiraitonDate = ISODateTimeFormat.dateTimeNoMillis().print( new DateTime().plusMinutes( MongoDBConstants.SESSION_DURATION_IN_MINUTES ) );
		final DBObject toInsert = BasicDBObjectBuilder.start(MongoDBConstants.SESSION_PROP_PSEUDO,sourceUserObject.get(MongoDBConstants.USER_PROP_PSEUDO)).
					add(MongoDBConstants.SESSION_PROP_EXPIRATION,expiraitonDate).get();
		col.insert(toInsert);
		return toInsert.get("_id").toString();
	}

	@Override
	public AuthenticationToken getAuthenticationToken(String sessionId) throws AuthenticationServiceException{
		final DBCollection col = database.getCollection(MongoDBConstants.COL_SESSIONS);
		final DBObject foundSesion = col.findOne(new BasicDBObject("_id",new ObjectId(sessionId)));
		if ( foundSesion != null ) {
			try {
				final String pseudo = foundSesion.get(MongoDBConstants.SESSION_PROP_PSEUDO).toString();
				final String expirationDateAsString = foundSesion.get(MongoDBConstants.SESSION_PROP_EXPIRATION).toString();
				final DateTime expirationDate = ISODateTimeFormat.dateTimeNoMillis().parseDateTime( expirationDateAsString );
				if ( expirationDate.isBeforeNow() ) {
					throw new AuthenticationServiceException("Session has expired");
				}
				return new BasicAuthenticationToken(sessionId,pseudo);
			} catch ( AuthenticationServiceException ex ) {
				throw ex;
			} catch ( Exception ex ) {
				// may raise NullPointerException if a field is missing in the DB
				// This may be due to an modified session document.
				throw new AuthenticationServiceException(ex);
			}
			
		}
		throw new AuthenticationServiceException("No such session");
	}

}
