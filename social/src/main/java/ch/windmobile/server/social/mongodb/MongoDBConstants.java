package ch.windmobile.server.social.mongodb;

public interface MongoDBConstants {
    public final static String DATABASE_NAME = "windmobile";

    public static final String COLLECTION_CHAT_ROOM_PREFIX = "chatroom_";
    public static final String CHAT_PROP_COMMENT = "comment";
    public static final String CHAT_PROP_USER = "user";
    public static final String CHAT_PROP_TIME = "time";

    public static final String COLLECTION_USERS = "users";
    public static final String USER_PROP_EMAIL = "email";
    public static final String USER_PROP_PSEUDO = "pseudo";
    public static final String USER_PROP_FULLNAME = "fullName";
    public static final String USER_PROP_SHA1 = "sha1";
    public static final String USER_PROP_ROLE = "role";
    public static final String USER_PROP_FAVORITES = "favorites";

    public static final String COLLECTION_COUNTERS = "counters";
}
