package ch.windmobile.server.socialmodel;

import java.util.List;

/**
 * Chat service to handle chat on a station
 *
 */
public interface ChatService {
	//---- Service command ------
	/**
	 * Push a chat message on a given char room ID for a given user
	 * Implementation should handle null user for anonymous chat
	 */
	void pushChat(String charRoomId,User user,String message);
	
	/**
	 * Retreive the last 'maxcount' chat item for a given Chat room ID
	 * result is returned ordered by chat post time
	 */
	List<ChatItem> findChatItems(String chatRoomId,int maxcount);
}
