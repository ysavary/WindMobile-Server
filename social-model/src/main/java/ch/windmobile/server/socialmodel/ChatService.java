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
package ch.windmobile.server.socialmodel;

import java.util.List;

import ch.windmobile.server.socialmodel.xml.Message;
import ch.windmobile.server.socialmodel.xml.Messages;

/**
 * Chat service to handle chat on a station
 * 
 */
public interface ChatService {

    Message postMessage(String chatRoomId, String pseudo, String message, String emailHash);

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

    /**
     *
     */
    boolean allowAnonymousMessages(String chatRoomId);
}
