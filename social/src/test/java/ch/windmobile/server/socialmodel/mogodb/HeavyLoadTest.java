package ch.windmobile.server.socialmodel.mogodb;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.util.FileCopyUtils;

import ch.windmobile.server.socialmodel.AuthenticationService;
import ch.windmobile.server.socialmodel.ChatService;
import ch.windmobile.server.socialmodel.ServiceLocator;

import com.mongodb.DB;
import com.mongodb.Mongo;

public class HeavyLoadTest {

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
	
	public void testFullChatCycle() throws Exception {
		ServiceLocator locator = new MongoDBServiceLocator().connect(null);
		try {
			final int CNT = 50000;
			final Executor executor = Executors.newFixedThreadPool(10);
			final AuthenticationService authenticationService = locator.getService(AuthenticationService.class);
			final ChatService chatService = locator.getService(ChatService.class);
			final String sessionId = authenticationService.authenticate("david@epyx.ch","123");
			final AtomicInteger counter = new AtomicInteger();
			final CountDownLatch latch = new CountDownLatch(CNT);
			for ( int i = 0 ; i < CNT ; i++ ) {
				executor.execute( new Runnable() {
					@Override
					public void run() {
						chatService.pushChat("TEST", sessionId, "Hello, this is message "+counter.incrementAndGet());	
						latch.countDown();
					}					
				});				
			}
			System.out.println("Chat sent, waiting for the end...");
			latch.await(2, TimeUnit.MINUTES);
			String ret = chatService.findChatItems("TEST", 5);
			System.out.println("result : "+ret);
		} finally {
			locator.disconnect();
		}
	}
	
	public static void main(String[] args) {
		try {
			HeavyLoadTest test = new HeavyLoadTest();
			test.beforeTest();
			long before = System.nanoTime();
			test.testFullChatCycle();
			System.out.println("Time : "+TimeUnit.NANOSECONDS.toMicros( ( System.nanoTime() - before ) )+" µs");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
