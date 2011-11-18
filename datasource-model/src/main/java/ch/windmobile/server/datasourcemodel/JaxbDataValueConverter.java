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
