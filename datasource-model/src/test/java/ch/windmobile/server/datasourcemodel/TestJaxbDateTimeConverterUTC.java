package ch.windmobile.server.datasourcemodel;

import java.util.Calendar;
import java.util.TimeZone;

import junit.framework.Assert;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class TestJaxbDateTimeConverterUTC {
    
    @BeforeClass
    public void initialize() {
        JaxbDateTimeConverter.setServerTimeZone(TimeZone.getTimeZone("UTC"));
    }

    @Test(dataProvider = "dates")
    public void testJaxbDateTimeConverter(String dateTime, String result) {
        Calendar calendar = JaxbDateTimeConverter.parseDateTime(dateTime);
        String printedDateTime = JaxbDateTimeConverter.printDateTime(calendar);

        Assert.assertEquals(printedDateTime, result);
    }

    @DataProvider(name = "dates")
    public String[][] getDates() {
        return new String[][] {
            // Winter
            { "2010-02-18T22:30:00+0000", "2010-02-18T22:30:00+0000" },
            // Summer, no daylight saving with UTC
            { "2010-08-18T22:30:00+0100", "2010-08-18T21:30:00+0000" },
            // Other time zone
            { "2010-08-18T22:30:00-0100", "2010-08-18T23:30:00+0000" },
        };
        
    }
}
