package testprojectcore.util;


import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class DateTimeUtil {


    //Check java.time.ZoneId for available Zone Ids
    public static ZonedDateTime getCurrentLocalTimeAccordingToZoneId(String zoneId) {
        Instant now = Instant.now(); // UTC time
        ZonedDateTime localTime = now.atZone(ZoneId.of(zoneId));
        return localTime;
    }

    //Check java.time.ZoneId for available Zone Ids
    public static Date getCurrentLocalTimeAccordingToZoneIdAndPattern(String zoneId, String pattern) {
        SimpleDateFormat f = new SimpleDateFormat(pattern);
        f.setTimeZone(TimeZone.getTimeZone(zoneId));
        Date localTime = GregorianCalendar.getInstance().getTime();
        return localTime;
    }

    public static Date addHoursToDate(Date date, int hoursToAdd) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, hoursToAdd);
        return calendar.getTime();
    }

    public static Date addDaysToDate(Date date, int daysToAdd) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, daysToAdd);
        return calendar.getTime();
    }


    public static String formatDate(Date date, String pattern){
        return new SimpleDateFormat(pattern).format(date);
    }
}
