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

import org.testng.Assert;
import org.testng.annotations.Test;

public class TestLinearRegression {

	@Test
	public void xyValues() {
		double[][] xyValues = {{1,2}, {2,3}, {3,4}};
		LinearRegression linearRegression = new LinearRegression(xyValues);
		linearRegression.compute();
        double slope = linearRegression.getBeta1();
        double angle = Math.toDegrees(Math.atan(slope));
        Assert.assertEquals(angle, 45d);
	}
}
