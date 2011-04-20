package ch.windmobile.server.jdc;

public class SwissGridHelper {
    
    public static final class Wgs84 {
        double latitude;
        double longitude;
        double ellHeight;
    }
    
    public static final class Lv03 {
        double east;
        double north;
        double height;
    }

    public static Wgs84 lv03toWgs84(double east, double north, double height) {
        Wgs84 wgs84 = new Wgs84();
        wgs84.latitude = chToWgsLat(east, north);
        wgs84.longitude = chToWgsLng(east, north);
        wgs84.ellHeight = chToWgsHeight(east, north, height);
        return wgs84;
    }

    public static Lv03 WGS84toLV03(double latitude, double longitude, double ellHeight) {
        Lv03 lv03 = new Lv03();
        lv03.east = wgsToChY(latitude, longitude);
        lv03.north = wgsToChX(latitude, longitude);
        lv03.height = wgsToChH(latitude, longitude, ellHeight);
        return lv03;
    }


    // Convert WGS lat/long (° dec) to CH y
    public static double wgsToChY(double lat, double lng) {
        // Converts degrees dec to sex
        lat = decToSexAngle(lat);
        lng = decToSexAngle(lng);

        // Converts degrees to seconds (sex)
        lat = sexAngleToSeconds(lat);
        lng = sexAngleToSeconds(lng);

        // Axiliary values (% Bern)
        double lat_aux = (lat - 169028.66) / 10000;
        double lng_aux = (lng - 26782.5) / 10000;

        // Process Y
        double y = 600072.37 
            + 211455.93 * lng_aux 
            -  10938.51 * lng_aux * lat_aux
            -      0.36 * lng_aux * Math.pow(lat_aux, 2)
            -     44.54 * Math.pow(lng_aux, 3);
 
        return y;
    }

    // Convert WGS lat/long (° dec) to CH x
    public static double wgsToChX(double lat, double lng) {
        // Converts degrees dec to sex
        lat = decToSexAngle(lat);
        lng = decToSexAngle(lng);

        // Converts degrees to seconds (sex)
        lat = sexAngleToSeconds(lat);
        lng = sexAngleToSeconds(lng);

        // Axiliary values (% Bern)
        double lat_aux = (lat - 169028.66) / 10000;
        double lng_aux = (lng - 26782.5) / 10000;

        // Process X
        double x = 200147.07
            + 308807.95 * lat_aux 
            +   3745.25 * Math.pow(lng_aux, 2)
            +     76.63 * Math.pow(lat_aux, 2)
            -    194.56 * Math.pow(lng_aux, 2) * lat_aux
            +    119.79 * Math.pow(lat_aux, 3);

        return x;
    }

    // Convert WGS lat/long (° dec) and height to CH h
    public static double wgsToChH(double lat, double lng, double h) {
        // Converts degrees dec to sex
        lat = decToSexAngle(lat);
        lng = decToSexAngle(lng);

        // Converts degrees to seconds (sex)
        lat = sexAngleToSeconds(lat);
        lng = sexAngleToSeconds(lng);

        // Axiliary values (% Bern)
        double lat_aux = (lat - 169028.66)/10000;
        double lng_aux = (lng - 26782.5)/10000;

        // Process h
        h = h - 49.55 
              +  2.73 * lng_aux 
              +  6.94 * lat_aux;
 
        return h;
    }
    

    // Convert CH y/x to WGS lat
    public static double chToWgsLat(double y, double x) {
        // Converts militar to civil and to unit = 1000km
        // Axiliary values (% Bern)
        double y_aux = (y - 600000) / 1000000;
        double x_aux = (x - 200000) / 1000000;

        // Process lat
        double lat = 16.9023892
            +  3.238272 * x_aux
            -  0.270978 * Math.pow(y_aux, 2)
            -  0.002528 * Math.pow(x_aux, 2)
            -  0.0447   * Math.pow(y_aux, 2) * x_aux
            -  0.0140   * Math.pow(x_aux, 3);

        // Unit 10000" to 1 " and converts seconds to degrees (dec)
        lat = lat * 100/36;

        return lat;
    }

    // Convert CH y/x to WGS long
    public static double chToWgsLng(double y, double x) {
        // Converts militar to civil and to unit = 1000km
        // Axiliary values (% Bern)
        double y_aux = (y - 600000) / 1000000;
        double x_aux = (x - 200000) / 1000000;

        // Process long
        double lng = 2.6779094
            + 4.728982 * y_aux
            + 0.791484 * y_aux * x_aux
            + 0.1306   * y_aux * Math.pow(x_aux, 2)
            - 0.0436   * Math.pow(y_aux, 3);
 
        // Unit 10000" to 1 " and converts seconds to degrees (dec)
        lng = lng * 100/36;
 
        return lng;
    }

    // Convert CH y/x/h to WGS height
    public static double chToWgsHeight(double y, double x, double h) {
        // Converts militar to civil and to unit = 1000km
        // Axiliary values (% Bern)
        double y_aux = (y - 600000) / 1000000;
        double x_aux = (x - 200000) / 1000000;

        // Process height
        h = h + 49.55
              - 12.60 * y_aux
              - 22.64 * x_aux;
 
        return h;
    }
    

    // Convert sexagesimal angle (degrees, minutes and seconds "dd.mmss") to
    // decimal angle (degrees)
    public static double sexToDecAngle(double dms) {
        // Extract DMS
        // Input: dd.mmss(,)ss
        double deg = 0, min = 0, sec = 0;
        deg = Math.floor(dms);
        min = Math.floor((dms - deg) * 100);
        sec = (((dms - deg) * 100) - min) * 100;

        // Result in degrees dec (dd.dddd)
        return deg + min / 60 + sec / 3600;
    }

    // Convert decimal angle (degrees) to sexagesimal angle (degrees, minutes
    // and seconds dd.mmss,ss)
    public static double decToSexAngle(double dec) {
        int deg = (int) Math.floor(dec);
        int min = (int) Math.floor((dec - deg) * 60);
        double sec = (((dec - deg) * 60) - min) * 60;

        // Output: dd.mmss(,)ss
        return deg + min / 100 + sec / 10000;
    }

    // Convert sexagesimal angle (degrees, minutes and seconds dd.mmss,ss) to
    // seconds
    public static double sexAngleToSeconds(double dms) {
        double deg = 0, min = 0, sec = 0;
        deg = Math.floor(dms);
        min = Math.floor((dms - deg) * 100);
        sec = (((dms - deg) * 100) - min) * 100;

        // Result in degrees sex (dd.mmss)
        return sec + min * 60 + deg * 3600;
    }
}
