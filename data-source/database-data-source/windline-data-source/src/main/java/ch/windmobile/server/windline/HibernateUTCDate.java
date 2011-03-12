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
    private static final SimpleDateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
    static {
        utcFormat.setTimeZone(TZ_UTC);
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
            DateFormat format = (DateFormat) utcFormat.clone();
            return format.parse(date);
        } catch (ParseException e) {
            throw new HibernateException(e);
        }
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index) throws HibernateException, SQLException {
        DateFormat format = (DateFormat) utcFormat.clone();
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
