package ch.windmobile.server.social.mongodb;

import java.net.UnknownHostException;

import ch.windmobile.server.socialmodel.AuthenticationService;
import ch.windmobile.server.socialmodel.ChatService;
import ch.windmobile.server.socialmodel.ServiceLocator;
import ch.windmobile.server.socialmodel.UserService;

import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

public class MongoDBServiceLocator implements ServiceLocator {
    private Mongo mongoService;
    private DB database;

    public synchronized ServiceLocator connect(String url) throws ServiceLocatorException {
        if (mongoService != null) {
            return this;
        }
        try {
            if (url == null) {
                mongoService = new Mongo();
            } else {
                mongoService = new Mongo(url);
            }
        } catch (UnknownHostException e) {
            throw new ServiceLocatorException(e);
        } catch (MongoException e) {
            throw new ServiceLocatorException(e);
        }
        database = mongoService.getDB(MongoDBConstants.DATABASE_NAME);
        return this;
    }

    public synchronized void disconnect() {
        if (mongoService == null) {
            return;
        }
        mongoService.close();
        mongoService = null;
    }

    @SuppressWarnings("unchecked")
    public <S> S getService(Class<S> serviceType) throws ServiceLocatorException {
        assertConnected();
        if (ChatService.class.isAssignableFrom(serviceType)) {
            return (S) new ChatServiceImpl(database);
        } else if (AuthenticationService.class.isAssignableFrom(serviceType)) {
            return (S) new AuthenticationServiceImpl(database);
        } else if (UserService.class.isAssignableFrom(serviceType)) {
            return (S) new UserServiceImpl(database);
        }
        throw new ServiceLocatorException("Unknown service type '" + serviceType + "'");
    }

    private void assertConnected() {
        if (mongoService == null) {
            throw new RuntimeException("Not connected, call connect() first");
        }
    }
}
