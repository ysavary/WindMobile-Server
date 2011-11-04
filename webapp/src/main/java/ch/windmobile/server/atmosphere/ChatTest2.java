package ch.windmobile.server.atmosphere;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.atmosphere.annotation.Broadcast;
import org.atmosphere.annotation.Suspend;

@Path("/chat2")
public class ChatTest2 {

    @Suspend
    @GET
    @Produces({ MediaType.TEXT_PLAIN })
    public String getMessages() {
        return "Old messages";
    }

    @Broadcast
    @Consumes(MediaType.TEXT_PLAIN)
    @POST
    public String publishMessage(String message) {
        return message;
    }
}