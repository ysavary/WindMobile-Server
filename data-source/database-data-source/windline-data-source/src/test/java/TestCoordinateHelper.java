import junit.framework.Assert;

import org.testng.annotations.Test;

import ch.windmobile.server.windline.CoordinateHelper;

public class TestCoordinateHelper {

    @Test
    public void testLongitude() {
        double longitute = CoordinateHelper.parseDMS("36°57'9\" N");
        Assert.assertEquals(36.9525000, longitute, 0.001);
    }

    @Test
    public void testLatitude() {
        double latitude = CoordinateHelper.parseDMS("110°4'21\" W");
        Assert.assertEquals(-110.0725000, latitude, 0.001);
    }
}
