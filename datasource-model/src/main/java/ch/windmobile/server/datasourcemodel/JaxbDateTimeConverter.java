package ch.windmobile.server.datasourcemodel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class JaxbDateTimeConverter {

    private static final DateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
    private static TimeZone serverTimeZone = TimeZone.getDefault();

    public static TimeZone getServerTimeZone() {
        return serverTimeZone;
    }

    public static void setServerTimeZone(TimeZone serverTimeZone) {
        JaxbDateTimeConverter.serverTimeZone = serverTimeZone;
    }

    public static Calendar parseDateTime(String source) {
        try {
            Calendar calendar = new GregorianCalendar(getServerTimeZone());
            dateTimeFormat.setCalendar(calendar);
            Date dateTime = dateTimeFormat.parse(source);
            calendar.setTime(dateTime);
            return calendar;
        } catch (ParseException e) {
            return null;
        }
    }

    public static String printDateTime(Calendar calendar) {
        try {
            dateTimeFormat.setCalendar(calendar);
            return dateTimeFormat.format(calendar.getTime());
        } catch (Exception e) {
            return null;
        }
    }
}
