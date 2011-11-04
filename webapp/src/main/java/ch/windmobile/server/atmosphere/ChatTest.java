package ch.windmobile.server.atmosphere;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.atmosphere.annotation.Broadcast;
import org.atmosphere.annotation.Suspend;

import ch.windmobile.server.social.mongodb.MongoDBServiceLocator;
import ch.windmobile.server.socialmodel.ChatService;
import ch.windmobile.server.socialmodel.ServiceLocator;
import ch.windmobile.server.socialmodel.xml.Messages;

@Path("/chat")
public class ChatTest {

    private ServiceLocator serviceLocator = new MongoDBServiceLocator();

    @Suspend
    @GET
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public Messages getMessages() {
        try {
            ChatService chatService = serviceLocator.getService(ChatService.class);
            return chatService.findMessages("jdc:1001", 5);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Broadcast
    @Consumes(MediaType.TEXT_PLAIN)
    @POST
    public String publishMessage(String message) {
        return message;
    }
}