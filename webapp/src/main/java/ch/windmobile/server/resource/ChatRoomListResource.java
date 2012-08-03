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
package ch.windmobile.server.resource;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import ch.windmobile.server.socialmodel.ChatService;
import ch.windmobile.server.socialmodel.ServiceLocator;
import ch.windmobile.server.socialmodel.xml.MessageIds;

import com.sun.jersey.api.core.InjectParam;
import com.sun.jersey.spi.resource.Singleton;

@Path("/chatrooms")
@Singleton
public class ChatRoomListResource {
    @InjectParam("serviceLocator")
    private ServiceLocator serviceLocator;

    @Context
    UriInfo uriInfo;
    @Context
    Request request;
    @Context
    HttpServletRequest servletRequest;

    @Path("{chatRoomId}")
    public ChatRoomResource getChatRoom(@PathParam("chatRoomId") String chatRoomId) {
        try {
            return new ChatRoomResource(uriInfo, request, servletRequest, serviceLocator, chatRoomId);
        } catch (Exception e) {
            ExceptionHandler.treatException(e);
            return null;
        }
    }

    @GET
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public MessageIds getLastMessageId(@QueryParam("chatroom") List<String> chatRoomIds) {
        try {
            ChatService chatService = serviceLocator.getService(ChatService.class);
            MessageIds returnValue = new MessageIds();
            for (Long id : chatService.getLastMessageIds(chatRoomIds)) {
                returnValue.getMessageIds().add(id);
            }
            return returnValue;
        } catch (Exception e) {
            ExceptionHandler.treatException(e);
            return null;
        }
    }
}
