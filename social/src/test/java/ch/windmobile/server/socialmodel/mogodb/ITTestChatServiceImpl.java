package ch.windmobile.server.socialmodel.mogodb;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.FileCopyUtils;

import ch.windmobile.server.social.mongodb.MongoDBConstants;
import ch.windmobile.server.social.mongodb.MongoDBServiceLocator;
import ch.windmobile.server.socialmodel.ChatService;
import ch.windmobile.server.socialmodel.ServiceLocator;
import ch.windmobile.server.socialmodel.xml.Messages;

import com.mongodb.DB;
import com.mongodb.Mongo;

public class ITTestChatServiceImpl {

    @Before
    public void beforeTest() {
        Mongo mongo = null;
        try {
            mongo = new Mongo();
            DB db = mongo.getDB(MongoDBConstants.DATABASE_NAME);
            InputStream is = getClass().getResourceAsStream("/init-script.js");
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            FileCopyUtils.copy(is, out);
            String code = out.toString();
            Logger.getLogger("WindoMobile").info("Create test database");
            Object result = db.doEval(code);
            Logger.getLogger("WindoMobile").info("Result : " + result.toString());

        } catch (Exception e) {
            Logger.getLogger("WindoMobile").log(Level.SEVERE, "Error : " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (mongo != null) {
                mongo.close();
            }
        }
    }

    @Test
    public void testFullChatCycle() throws Exception {
        ServiceLocator locator = new MongoDBServiceLocator().connect(null);
        try {
            ChatService chatService = locator.getService(ChatService.class);
            for (int i = 0; i < 50; i++) {
                chatService.postMessage("Chat room", "Test user", "Hello, this is message " + i);
            }
            Messages messages = chatService.findMessages("TEST", 5);
            Assert.assertEquals("Hello, this is message 49", messages.getMessages().get(0).getText());
            Assert.assertEquals("dsi", messages.getMessages().get(0).getPseudo());
        } finally {
            locator.disconnect();
        }
    }
}
