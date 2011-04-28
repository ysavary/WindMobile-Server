package ch.windmobile.server.socialmodel.mogodb;

public interface MongoDBConstants {
	public final static String DATABASE_NAME = "epyx_windmobile";
	public final static String COL_CHAT_ROOM_PREFIX = "chatroom_";
	public final static String COL_USERS = "users";
	public final static String COL_SESSIONS = "sessions";
	
	public static final String CHAT_PROP_COMMENT = "comment";
	public static final String CHAT_PROP_USER = "user";
	public static final String CHAT_PROP_TIME = "time";
	
	
	public static final String USER_PROP_PSEUDO = "pseudo";
	public static final String USER_PROP_EMAIL = "email";
	public static final String USER_PROP_FULLNAME = "fullName";
	public static final String USER_PROP_SHA1 = "sha1";
	
	
	public static final String SESSION_PROP_PSEUDO = "pseudo";
	public static final String SESSION_PROP_EXPIRATION = "expirationDate";

	public static final int SESSION_DURATION_IN_MINUTES = 24*60; //24 hours

}
