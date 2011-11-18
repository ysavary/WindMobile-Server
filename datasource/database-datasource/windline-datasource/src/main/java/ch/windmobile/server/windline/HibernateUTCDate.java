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

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;

public class HibernateUTCDate implements UserType {
    private static final TimeZone TZ_UTC = TimeZone.getTimeZone("UTC");
    private static final SimpleDateFormat _utcFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
    static {
        _utcFormat.setTimeZone(TZ_UTC);
    }

    @Override
    public int[] sqlTypes() {
        return new int[] { Types.TIMESTAMP };
    }

    @Override
    public Class<?> returnedClass() {
        return Date.class;
    }

    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        return (x == null) ? (y == null) : x.equals(y);
    }

    @Override
    public int hashCode(Object x) throws HibernateException {
        return x.hashCode();
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, Object owner) throws HibernateException, SQLException {
        try {
            String date = rs.getString(names[0]);
            // DateFormat are NOT thread safe
            DateFormat format = (DateFormat) _utcFormat.clone();
            return format.parse(date);
        } catch (ParseException e) {
            throw new HibernateException(e);
        }
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index) throws HibernateException, SQLException {
        // DateFormat are NOT thread safe
        DateFormat format = (DateFormat) _utcFormat.clone();
        String date = format.format((Date) value);
        st.setString(index, date);
    }

    @Override
    public Object deepCopy(Object value) throws HibernateException {
        return (value == null) ? null : new Date(((Date) value).getTime());
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable) deepCopy(value);
    }

    @Override
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return (Serializable) deepCopy(cached);
    }

    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return (Serializable) deepCopy(original);
    }
}
