/*******************************************************************************
 * Copyright (c) 2011 epyx SA.
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Affero General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Affero General Public License along
 * with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package ch.windmobile.server.social.mongodb;

public interface MongoDBConstants {
	public final static String DATABASE_NAME = "windmobile";

	public static final String COLLECTION_CHAT_ROOM_PREFIX = "chatroom_";
	public static final String CHAT_PROP_TEXT = "text";
	public static final String CHAT_PROP_USER = "user";
	public static final String CHAT_PROP_TIME = "time";
	public static final String CHAT_PROP_EMAIL_HASH = "emailHash";

	public static final String COLLECTION_USERS = "users";
	public static final String USER_PROP_EMAIL = "email";
	public static final String USER_PROP_PSEUDO = "pseudo";
	public static final String USER_PROP_FULLNAME = "fullName";
	public static final String USER_PROP_SHA1 = "sha1";
	public static final String USER_PROP_ROLE = "role";
	public static final String USER_PROP_FAVORITES = "favorites";
	public static final String USER_PROP_FAVORITE_STATIONID = "stationId";
	public static final String USER_PROP_FAVORITE_LASTMESSAGEID = "lastMessageId";

	public static final String COLLECTION_CHATROOMS = "chatrooms";
	public static final String CHATROOM_REFUSE_ANONYMOUS = "refuseAnonymous";
}
