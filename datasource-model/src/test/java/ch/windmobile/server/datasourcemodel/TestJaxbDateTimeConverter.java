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
