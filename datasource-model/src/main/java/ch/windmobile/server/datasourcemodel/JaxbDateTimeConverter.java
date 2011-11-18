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

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class JaxbDateTimeConverter {

    /* 'Z' outputs timezone offset without a colon to be compatible with Java standard 'SimpleDateFormat'
       withOffsetParsed() keeps the original timezone */
    private static final DateTimeFormatter compatibleParser = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZ").withOffsetParsed();

    public static DateTime parseDateTime(String source) {
        try {
            DateTime dateTime = compatibleParser.parseDateTime(source);
            return dateTime;
        } catch (Exception e) {
            return null;
        }
    }

    public static String printDateTime(DateTime dateTime) {
        try {
            return dateTime.toString(compatibleParser);
        } catch (Exception e) {
            return null;
        }
    }
}
