package ch.windmobile.server.jdc;

import java.text.ParseException;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import ch.windmobile.server.datasourcemodel.xml.Status;
import ch.windmobile.server.jdc.JdcDataSource.JdcStatus;
import ch.windmobile.server.jdc.dataobject.Station;

public class TestJdcDataSource {
    private static final DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm ZZ").withOffsetParsed();

    @DataProvider(name = "expirationDates")
    public Object[][] expirationDates() {
        return new Object[][] {
            // Summer time
            { "31/07/2010 11:05 +0200", "31/07/2010 11:00 +0200", "31/07/2010 11:20 +0200", "GREEN" },
            { "31/07/2010 11:31 +0200", "31/07/2010 11:00 +0200", "31/07/2010 11:20 +0200", "ORANGE" },
            { "31/07/2010 22:45 +0200", "31/07/2010 19:45 +0200", "1/08/2010 08:00 +0200", "GREEN" },
            { "1/08/2010 08:09 +0200", "31/07/2010 19:45 +0200", "1/08/2010 08:00 +0200", "GREEN" },
            { "1/08/2010 09:00 +0200", "31/07/2010 19:45 +0200", "1/08/2010 08:00 +0200", "ORANGE" },
            { "1/08/2010 10:11 +0200", "31/07/2010 19:45 +0200", "1/08/2010 08:00 +0200", "RED" },
            // Winter time
            { "28/02/2010 11:35 +0100", "28/02/2010 11:00 +0100", "28/02/2010 12:00 +0100", "GREEN" },
            { "21/03/2010 12:11 +0100", "21/03/2010 11:00 +0100", "21/03/2010 12:00 +0100", "ORANGE" },
            { "28/02/2010 22:45 +0100", "28/02/2010 19:45 +0100", "1/03/2010 09:00 +0100", "GREEN" },
            { "1/03/2010 09:09 +0100", "28/02/2010 19:45 +0100", "1/03/2010 09:00 +0100", "GREEN" },
            { "1/03/2010 10:00 +0100", "28/02/2010 19:45 +0100", "1/03/2010 09:00 +0100", "ORANGE" },
            { "1/03/2010 11:11 +0100", "28/02/2010 19:45 +0100", "1/03/2010 09:00 +0100", "RED" } };
    }

    @Test(dataProvider = "expirationDates")
    public void testGetExpirationDate(String sNow, String sLastUpdate, String sExpectedResult, String sExpectedStatus) throws ParseException {
        DateTime now = dateFormatter.parseDateTime(sNow);
        DateTime lastUpdate = dateFormatter.parseDateTime(sLastUpdate);
        DateTime expectedResult = dateFormatter.parseDateTime(sExpectedResult);

        DateTime expirationDate = JdcDataSource.getExpirationDate(now, lastUpdate);
        Station station = new Station();
        station.setStatus(JdcStatus.active.getValue());
        Status status = JdcDataSource.getStatus(station, now, expirationDate);

        Assert.assertEquals(expirationDate.toString(dateFormatter), expectedResult.toString(dateFormatter));
        Assert.assertEquals(status.toString(), sExpectedStatus);
    }
}
