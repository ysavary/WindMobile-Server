package ch.windmobile.server.socialmodel.mogodb;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.FileCopyUtils;

import ch.windmobile.server.socialmodel.AuthenticationService;
import ch.windmobile.server.socialmodel.ChatService;
import ch.windmobile.server.socialmodel.ServiceLocator;

import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.util.JSON;

public class ChatServiceImplTest {

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
			AuthenticationService authenticationService = locator.getService(AuthenticationService.class);
			ChatService chatService = locator.getService(ChatService.class);
			String sessionId = authenticationService.authenticate("david@epyx.ch","123");
			for ( int i = 0 ; i < 50 ; i++ ) {
				chatService.pushChat("TEST", sessionId, "Hello, this is message "+i);
			}
			String ret = chatService.findChatItems("TEST", 5);
			Object obj = JSON.parse( ret );
			Assert.assertTrue(obj instanceof List);
			@SuppressWarnings("rawtypes")
			DBObject dbo = (DBObject) ((List)obj).get(0);
			Assert.assertEquals("Hello, this is message 49", dbo.get("comment"));
			Assert.assertEquals("dsi", dbo.get("user"));
		} finally {
			locator.disconnect();
		}
	}
}
