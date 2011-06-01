package ch.windmobile.server.socialmodel;

import java.util.List;

import ch.windmobile.server.socialmodel.xml.Messages;

/**
 * Chat service to handle chat on a station
 * 
 */
public interface ChatService {

    void postMessage(String chatRoomId, String pseudo, String message);

    /**
     * @param chatRoomId
     * @return the last message id in the chatRoomId
     */
    Long getLastMessageId(String chatRoomIds);

    /**
     * @param chatRoomIds
     * @return the last message id in each chatRoomId
     */
    List<Long> getLastMessageIds(List<String> chatRoomIds);

    /**
     * Retrieve the last 'maxCount' chat item for a given chat room ID. Result is returned ordered by chat post time
     */
    Messages findMessages(String chatRoomId, int maxCount);
}
