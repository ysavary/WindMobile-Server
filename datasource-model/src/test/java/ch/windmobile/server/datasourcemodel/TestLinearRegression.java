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
