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
