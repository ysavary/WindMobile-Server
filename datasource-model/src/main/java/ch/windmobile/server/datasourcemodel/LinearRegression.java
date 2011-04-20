package ch.windmobile.server.datasourcemodel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LinearRegression {
    private final Logger log = LoggerFactory.getLogger(getClass());

    // Input values
    private double[][] xyValues;

    double[] x, y;
    double sumx, sumy, sumx2;
    int n;
    double xbar, ybar;
    double xxbar, yybar, xybar;

    // Results
    private double beta0;
    private double beta1;

    public LinearRegression(double[][] xyValues) {
        this.xyValues = xyValues;
    }

    public void compute() {
        x = new double[xyValues.length];
        y = new double[xyValues.length];

        // first pass: read in data, compute xbar and ybar
        sumx = sumy = sumx2 = 0.0;
        for (n = 0; n < xyValues.length; n++) {
            x[n] = xyValues[n][0];
            y[n] = xyValues[n][1];
            sumx += x[n];
            sumx2 += x[n] * x[n];
            sumy += y[n];
        }
        xbar = sumx / n;
        ybar = sumy / n;

        // second pass: compute summary statistics
        xxbar = yybar = xybar = 0.0;
        for (int i = 0; i < n; i++) {
            xxbar += (x[i] - xbar) * (x[i] - xbar);
            yybar += (y[i] - ybar) * (y[i] - ybar);
            xybar += (x[i] - xbar) * (y[i] - ybar);
        }
        beta1 = xybar / xxbar;
        beta0 = ybar - beta1 * xbar;

        // print results
        log.debug("y = " + beta1 + " * x + " + beta0);
    }

    // analyze results
    public void analyse() {
        int df = n - 2;
        double rss = 0.0; // residual sum of squares
        double ssr = 0.0; // regression sum of squares
        for (int i = 0; i < n; i++) {
            double fit = beta1 * x[i] + beta0;
            rss += (fit - y[i]) * (fit - y[i]);
            ssr += (fit - ybar) * (fit - ybar);
        }
        double R2 = ssr / yybar;
        double svar = rss / df;
        double svar1 = svar / xxbar;
        double svar0 = svar / n + xbar * xbar * svar1;
        log.info("R^2                 = " + R2);
        log.info("std error of beta_1 = " + Math.sqrt(svar1));
        log.info("std error of beta_0 = " + Math.sqrt(svar0));
        svar0 = svar * sumx2 / (n * xxbar);
        log.info("std error of beta_0 = " + Math.sqrt(svar0));

        log.info("SSTO = " + yybar);
        log.info("SSE  = " + rss);
        log.info("SSR  = " + ssr);
    }

    public double getBeta0() {
        return beta0;
    }

    public double getBeta1() {
        return beta1;
    }
}
