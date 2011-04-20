package ch.windmobile.server.datasourcemodel;

import junit.framework.Assert;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class TestJaxbDataValueConverter {

    @Test(dataProvider = "doubles")
    public void testJaxbDataValueConverter(float realValue, String result) {
        String formatterStringValue = JaxbDataValueConverter.printDataValue(realValue);
        Assert.assertEquals(result, formatterStringValue);
        Float formattedFloatValue = JaxbDataValueConverter.parseDataValue(formatterStringValue);
        Assert.assertEquals(result, formattedFloatValue.toString());
    }

    @DataProvider(name = "doubles")
    public Object[][] getDoubles() {
        return new Object[][] { { 10f, "10.0" }, { 0f, "0.0" }, { 0.001f, "0.0" }, { -1.22f, "-1.2" },
            { 32.23f, "32.2" }, { 32.56f, "32.6" } };

    }
}
