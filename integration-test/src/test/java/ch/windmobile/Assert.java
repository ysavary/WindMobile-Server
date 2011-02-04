package ch.windmobile;

public class Assert extends org.testng.Assert {

    public static void assertNotEmpty(String actual, String message) {
        Assert.assertNotNull(actual, message);
        if (actual.equals("") == false) {
            return;
        }
        Assert.fail(message);
    }
}
