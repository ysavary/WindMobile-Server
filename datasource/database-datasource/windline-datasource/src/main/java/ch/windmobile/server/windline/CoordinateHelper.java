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
