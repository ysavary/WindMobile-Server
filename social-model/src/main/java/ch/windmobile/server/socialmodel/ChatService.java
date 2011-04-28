package ch.windmobile.server.socialmodel;


/**
 * Chat service to handle chat on a station
 *
 */
public interface ChatService {
	//---- Service command ------
	/**
	 * Push a chat message on a given char room ID for a given user session ID
	 * session ID must first be created using {@link AuthenticationService}
	 * If the session ID is not valid, an {@link SecurityException} is raise
	 * Implementation should handle null user for anonymous chat
	 */
	void pushChat(String charRoomId,String userSessionId,String message) throws SecurityException;
	
	/**
	 * Retrieve the last 'maxcount' chat item for a given Chat room ID
	 * result is returned ordered by chat post time in an already formated JSON value
	 */
	String findChatItems(String chatRoomId,int maxcount);
}
