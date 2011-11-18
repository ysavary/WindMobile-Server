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
import ch.windmobile.server.socialmodel.AuthenticationService;
import ch.windmobile.server.socialmodel.ServiceLocator;

import com.mongodb.DB;
import com.mongodb.Mongo;

public class ITTestAuthenticationServiceImpl {

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

    @Test(expected = IllegalArgumentException.class)
    public void testAuthenticationBadToken() throws Exception {
        ServiceLocator locator = new MongoDBServiceLocator().connect(null);
        try {
            AuthenticationService authenticationService = locator.getService(AuthenticationService.class);
            authenticationService.authenticate(null, null);
        } finally {
            locator.disconnect();
        }
    }

    @Test
    public void testAuthenticationValidUser() throws Exception {
        ServiceLocator locator = new MongoDBServiceLocator().connect(null);
        try {
            AuthenticationService authenticationService = locator.getService(AuthenticationService.class);
            String pseudo = authenticationService.authenticate("david@epyx.ch", "123");
            Assert.assertNotNull(pseudo);
        } finally {
            locator.disconnect();
        }
    }

    @Test(expected = AuthenticationService.AuthenticationServiceException.class)
    public void testAuthenticationInvalidUser() throws Exception {
        ServiceLocator locator = new MongoDBServiceLocator().connect(null);
        try {
            AuthenticationService authenticationService = locator.getService(AuthenticationService.class);
            String pseudo = authenticationService.authenticate("unknown@epyx.ch", "445");
            Assert.assertNotNull(pseudo);
        } finally {
            locator.disconnect();
        }
    }

    @Test(expected = AuthenticationService.AuthenticationServiceException.class)
    public void testAuthenticationInvalidPassword() throws Exception {
        ServiceLocator locator = new MongoDBServiceLocator().connect(null);
        try {
            AuthenticationService authenticationService = locator.getService(AuthenticationService.class);
            String pseudo = authenticationService.authenticate("david@epyx.ch", "445");
            Assert.assertNotNull(pseudo);
        } finally {
            locator.disconnect();
        }
    }
}
