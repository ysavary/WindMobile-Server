package ch.windmobile.server.datasourcemodel;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Locale;

public class JaxbDataValueConverter {

    private static final ThreadLocal<DecimalFormat> decimalFormat = new ThreadLocal<DecimalFormat>() {
        @Override
        protected DecimalFormat initialValue() {
            DecimalFormat decimalFormat = (DecimalFormat) DecimalFormat.getInstance(Locale.US);
            decimalFormat.applyPattern("0.0");
            return decimalFormat;
        }
    };

    public static Float parseDataValue(String source) {
        try {
            return decimalFormat.get().parse(source).floatValue();
        } catch (ParseException e) {
            return null;
        }
    }

    public static String printDataValue(Float number) {
        try {
            return decimalFormat.get().format(number);
        } catch (Exception e) {
            return null;
        }
    }
}
