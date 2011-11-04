package ch.windmobile.server.socialmodel.mogodb;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileCopyUtils;

import ch.windmobile.server.social.mongodb.MongoDBConstants;
import ch.windmobile.server.social.mongodb.MongoDBServiceLocator;
import ch.windmobile.server.socialmodel.ServiceLocator;
import ch.windmobile.server.socialmodel.UserService;
import ch.windmobile.server.socialmodel.xml.Favorite;

import com.mongodb.DB;
import com.mongodb.Mongo;

public class FavoritesTest {
    private final Logger log = LoggerFactory.getLogger(getClass());

    void beforeTest() {
        Mongo mongo = null;
        try {
            mongo = new Mongo();
            DB db = mongo.getDB(MongoDBConstants.DATABASE_NAME);
            InputStream is = getClass().getResourceAsStream("/init-script.js");
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            FileCopyUtils.copy(is, out);
            String code = out.toString();
            Object result = db.doEval(code);
            log.info("Create test database: " + result.toString());

        } catch (Exception e) {
            log.error("Error : " + e.getMessage(), e);
        } finally {
            if (mongo != null) {
                mongo.close();
            }
        }
    }

    void logFavorites(List<Favorite> favorites) {
        for (Favorite favorite : favorites) {
            log.info("stationId=" + favorite.getStationId() + ", lastMessageId=" + favorite.getLastMessageId());
        }
    }

    void testGetFavorites() throws Exception {
        ServiceLocator locator = new MongoDBServiceLocator().connect(null);
        try {
            final UserService userService = locator.getService(UserService.class);

            List<Favorite> favorites = userService.getFavorites("yann.savary@epyx.ch");
            log.info("testGetFavorites()");
            logFavorites(favorites);
        } finally {
            locator.disconnect();
        }
    }

    void testAddToFavorites() throws Exception {
        ServiceLocator locator = new MongoDBServiceLocator().connect(null);
        try {
            final UserService userService = locator.getService(UserService.class);

            List<Favorite> favorites = new ArrayList<Favorite>();
            Favorite favorite = new Favorite();
            favorite.setStationId("1");
            favorite.setLastMessageId(100);
            favorites.add(favorite);

            favorite = new Favorite();
            favorite.setStationId("2");
            favorite.setLastMessageId(200);
            favorites.add(favorite);

            List<Favorite> result = userService.addToFavorites("yann.savary@epyx.ch", favorites);
            log.info("testAddToFavorites()");
            logFavorites(result);
        } finally {
            locator.disconnect();
        }
    }

    void testRemoveFromFavorites() throws Exception {
        ServiceLocator locator = new MongoDBServiceLocator().connect(null);
        try {
            final UserService userService = locator.getService(UserService.class);

            List<Favorite> favorites = new ArrayList<Favorite>();
            Favorite favorite = new Favorite();
            favorite.setStationId("2");
            favorites.add(favorite);

            List<Favorite> result = userService.removeFromFavorites("yann.savary@epyx.ch", favorites);
            log.info("testRemoveFromFavorites()");
            logFavorites(result);
        } finally {
            locator.disconnect();
        }
    }

    void testClearFavorites() throws Exception {
        ServiceLocator locator = new MongoDBServiceLocator().connect(null);
        try {
            final UserService userService = locator.getService(UserService.class);

            userService.clearFavorites("yann.savary@epyx.ch");
        } finally {
            locator.disconnect();
        }
    }

    public static void main(String[] args) {
        try {
            FavoritesTest test = new FavoritesTest();
            test.beforeTest();
            long before = System.nanoTime();
            test.testAddToFavorites();
            test.testAddToFavorites();
            test.testRemoveFromFavorites();
            test.testClearFavorites();
            test.testGetFavorites();
            System.out.println("Time : " + TimeUnit.NANOSECONDS.toMicros((System.nanoTime() - before)) + " ms");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
