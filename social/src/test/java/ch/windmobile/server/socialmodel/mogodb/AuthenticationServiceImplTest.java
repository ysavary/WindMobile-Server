package ch.windmobile.server.socialmodel.mogodb;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.FileCopyUtils;

import ch.windmobile.server.socialmodel.AuthenticationService;
import ch.windmobile.server.socialmodel.AuthenticationToken;
import ch.windmobile.server.socialmodel.ServiceLocator;

import com.mongodb.DB;
import com.mongodb.Mongo;

public class AuthenticationServiceImplTest {

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

	@Test(expected=IllegalArgumentException.class)
	public void testAuthenticationBadToken() throws Exception {
		ServiceLocator locator = new MongoDBServiceLocator().connect(null);
		try {
			AuthenticationService authenticationService = locator.getService(AuthenticationService.class);
			authenticationService.authenticate(null,null);
		} finally {
			locator.disconnect();
		}
	}
	
	@Test
	public void testAuthenticationValidUser() throws Exception {
		ServiceLocator locator = new MongoDBServiceLocator().connect(null);
		try {
			AuthenticationService authenticationService = locator.getService(AuthenticationService.class);
			String sessionId = authenticationService.authenticate("david@epyx.ch", "123");
			Assert.assertNotNull( sessionId );
		} finally {
			locator.disconnect();
		}
	}
	
	@Test(expected=AuthenticationService.AuthenticationServiceException.class)
	public void testAuthenticationInvalidUser() throws Exception {
		ServiceLocator locator = new MongoDBServiceLocator().connect(null);
		try {
			AuthenticationService authenticationService = locator.getService(AuthenticationService.class);
			String sessionId = authenticationService.authenticate("unknown@epyx.ch", "445");
			Assert.assertNotNull( sessionId );
		} finally {
			locator.disconnect();
		}
	}
	
	@Test(expected=AuthenticationService.AuthenticationServiceException.class)
	public void testAuthenticationInvalidPassword() throws Exception {
		ServiceLocator locator = new MongoDBServiceLocator().connect(null);
		try {
			AuthenticationService authenticationService = locator.getService(AuthenticationService.class);
			String sessionId = authenticationService.authenticate("david@epyx.ch", "445");
			Assert.assertNotNull( sessionId );
		} finally {
			locator.disconnect();
		}
	}
	
	@Test
	public void testAuthenticationGetSessionBack() throws Exception {
		ServiceLocator locator = new MongoDBServiceLocator().connect(null);
		try {
			AuthenticationService authenticationService = locator.getService(AuthenticationService.class);
			String sessionId = authenticationService.authenticate("david@epyx.ch", "123");
			Assert.assertNotNull( sessionId );
			Logger.getLogger("TEST").info("Session : "+sessionId);
			AuthenticationToken token = authenticationService.getAuthenticationToken( sessionId );
			org.junit.Assert.assertNotNull( token );
			Assert.assertEquals( token.getPseudo(), "dsi");
		} finally {
			locator.disconnect();
		}
	}
}
