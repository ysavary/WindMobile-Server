package ch.windmobile.server.windline;

public class CoordinateHelper {

    public static double parseDMS(String input) {
        String[] parts = input.split("[^\\d\\w]+");
        return convertDMSToDD(parts[0], parts[1], parts[2], parts[3]);
    }

    public static double convertDMSToDD(String days, String minutes, String seconds, String direction) {
        double dd = Integer.parseInt(days) + Integer.parseInt(minutes) / 60d + Integer.parseInt(seconds) / (60d * 60d);

        if (direction.equalsIgnoreCase("S") || direction.equalsIgnoreCase("W")) {
            dd = dd * -1;
        } // Don't do anything for N or E
        return dd;
    }
}
