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
