package util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    public static final SimpleDateFormat format1 = new SimpleDateFormat( "yyyy-MM-dd" );
    public static final SimpleDateFormat format2 = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
    public static final SimpleDateFormat format3 = new SimpleDateFormat( "HH:mm:ss" );

    public static String toString( Date date, String format ) {
        return new SimpleDateFormat( format ).format( date );
    }

    public static String dateString( Date date ) {
        return format1.format( date );
    }

    public static String dateTimeString( Date date ) {
        return format2.format( date );
    }

    public static String timeString( Date date ) {
        return format3.format( date );
    }

    public static Date now() {
        return new Date();
    }
}
