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
