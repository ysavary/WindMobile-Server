package ch.windmobile.server.model;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Locale;

public class JaxbDataValueConverter {

    private static final DecimalFormat decimalFormat;
    static {
        decimalFormat = (DecimalFormat) DecimalFormat.getInstance(Locale.US);
        decimalFormat.applyPattern("0.0");
    }

    public static Float parseDataValue(String source) {
        try {
            return decimalFormat.parse(source).floatValue();
        } catch (ParseException e) {
            return null;
        }
    }

    public static String printDataValue(Float number) {
        try {
            return decimalFormat.format(number);
        } catch (Exception e) {
            return null;
        }
    }
}
