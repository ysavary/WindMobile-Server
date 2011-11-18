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
package ch.windmobile.server.datasourcemodel;

import junit.framework.Assert;

import org.joda.time.DateTime;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class TestJaxbDateTimeConverter {

    @Test(dataProvider = "dates")
    public void testJaxbDateTimeConverter(String input, String result) {
        DateTime dateTime = JaxbDateTimeConverter.parseDateTime(input);
        String printedDateTime = JaxbDateTimeConverter.printDateTime(dateTime);

        Assert.assertEquals(printedDateTime, result);
    }

    @DataProvider(name = "dates")
    public String[][] getDates() {
        return new String[][] {
            // Winter
            { "2010-02-18T22:30:00+0100", "2010-02-18T22:30:00+0100" },
            // Summer, daylight saving
            { "2010-08-18T22:30:00+0200", "2010-08-18T22:30:00+0200" },
            // Other time zones
            { "2010-08-18T22:30:00+0300", "2010-08-18T22:30:00+0300" },
            { "2010-08-18T22:30:00-0100", "2010-08-18T22:30:00-0100" }, };
    }
}
