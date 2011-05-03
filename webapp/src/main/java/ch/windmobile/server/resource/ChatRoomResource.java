package ch.windmobile.server.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import org.springframework.security.access.annotation.Secured;

import ch.windmobile.server.socialmodel.ChatService;
import ch.windmobile.server.socialmodel.ServiceLocator;
import ch.windmobile.server.socialmodel.xml.Messages;

public class ChatRoomResource {
    private ServiceLocator serviceLocator;

    private String chatRoomId;

    @Context
    UriInfo uriInfo;
    @Context
    Request request;
    String stationId;

    public ChatRoomResource(UriInfo uriInfo, Request request, ServiceLocator serviceLocator, String chatRoomId) {
        this.uriInfo = uriInfo;
        this.request = request;
        this.serviceLocator = serviceLocator;
        this.chatRoomId = chatRoomId;
    }

    @POST
    @Path("postmessage")
    @Secured({ "user" })
    @Consumes(MediaType.TEXT_PLAIN)
    public void postMessage(String message) {
        try {
            ChatService chatService = serviceLocator.getService(ChatService.class);
            chatService.postMessage(chatRoomId, "yann", message);
        } catch (Exception e) {
            ExceptionHandler.treatException(e);
        }
    }

    @GET
    @Path("lastmessages/{maxCount}")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public Messages getMessages(@PathParam("maxCount") int maxCount) {
        try {
            ChatService chatService = serviceLocator.getService(ChatService.class);
            return chatService.findMessages(chatRoomId, maxCount);
        } catch (Exception e) {
            ExceptionHandler.treatException(e);
            return null;
        }
    }
}