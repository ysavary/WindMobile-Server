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
import java.security.MessageDigest;
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
        MessageDigest md = MessageDigest.getInstance("MD5");
        try {
            ChatService chatService = locator.getService(ChatService.class);
            for (int i = 0; i < 50; i++) {
                chatService.postMessage("Chat room", "Test user", "Hello, this is message " + i,
                    new String(md.digest("test@mycompany.com".getBytes())));
            }
            Messages messages = chatService.findMessages("TEST", 5);
            Assert.assertEquals("Hello, this is message 49", messages.getMessages().get(0).getText());
            Assert.assertEquals("dsi", messages.getMessages().get(0).getPseudo());
        } finally {
            locator.disconnect();
        }
    }
}
