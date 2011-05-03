package ch.windmobile.server.socialmodel;

import ch.windmobile.server.socialmodel.xml.Messages;

/**
 * Chat service to handle chat on a station
 * 
 */
public interface ChatService {
    // ---- Service command ------
    /**
     * Post a chat message on a given chat room ID for a given user session ID. Session ID must first be created using
     * {@link AuthenticationService}. If the session ID is not valid, an {@link SecurityException} is raise
     * Implementation should handle null sessionId for anonymous chat
     */
    void postMessage(String chatRoomId, String pseudo, String message) throws SecurityException;

    /**
     * Retrieve the last 'maxCount' chat item for a given chat room ID. Result is returned ordered by chat post time
     */
    Messages findMessages(String chatRoomId, int maxCount);
}
