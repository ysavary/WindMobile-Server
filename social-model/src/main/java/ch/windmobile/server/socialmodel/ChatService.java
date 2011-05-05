package ch.windmobile.server.socialmodel;

import ch.windmobile.server.socialmodel.xml.Messages;

/**
 * Chat service to handle chat on a station
 * 
 */
public interface ChatService {

    void postMessage(String chatRoomId, String pseudo, String message) throws SecurityException;

    /**
     * Retrieve the last 'maxCount' chat item for a given chat room ID. Result is returned ordered by chat post time
     */
    Messages findMessages(String chatRoomId, int maxCount);
}
