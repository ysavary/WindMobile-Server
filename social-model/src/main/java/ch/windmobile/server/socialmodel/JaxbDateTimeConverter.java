package ch.windmobile.server.socialmodel;

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
