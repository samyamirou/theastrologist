package com.theastrologist.util;

import com.theastrologist.domain.Degree;
import org.joda.time.*;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import swisseph.SweDate;

import java.util.TimeZone;

/**
 * Created by SAM on 16/11/2014.
 */
public class DateUtil {

    // private static final Degree DIRECTION_ARC_FOR_ONE_YEAR = new Degree(1,0,0);
    private static final Degree DIRECTION_ARC_FOR_ONE_YEAR = new Degree(0,59,8);
    private static final Period PRIMARY_DIRECTION_ARC = new Period(0,3,56,0);
    //private static final Period PRIMARY_DIRECTION_ARC = new Period(0,4,0,0);
    private static final double MILLIS_FOR_ONE_YEAR = DateTimeConstants.MILLIS_PER_DAY * 365.2425;
    private static final PeriodFormatterBuilder PERIOD_FORMATTER_BUILDER = new PeriodFormatterBuilder()
            .appendDays()
            .appendSuffix("d")
            .appendHours()
            .appendSuffix("h")
            .appendMinutes()
            .appendSuffix("m")
            .appendSeconds()
            .appendSuffix("s");

    public static SweDate getSweDateUTC(DateTime dateTime) {
        DateTimeZone.setDefault(DateTimeZone.UTC);
        DateTimeZone zone = dateTime.getZone();
        long millis = dateTime.getMillis();
        DateTime utcDate = dateTime.withZone(DateTimeZone.UTC);
        int year = utcDate.getYear();
        int month = utcDate.getMonthOfYear();
        int day = utcDate.getDayOfMonth();
        double hour = utcDate.getHourOfDay() + utcDate.getMinuteOfHour() / 60.;
        return new SweDate(year,month,day,hour);
    }

    public static Duration getDurationForArc(Degree arc) {
        // 59'8'' pour un an (365)
        double durationInMillis = (arc.getBaseDegree() * MILLIS_FOR_ONE_YEAR) / DIRECTION_ARC_FOR_ONE_YEAR.getBaseDegree();
        return new Duration((long)durationInMillis);
    }

    public static Period convertPrimaryDirectionDuration(Period duration) {
        // 3 min 56 sec ou 4 min pour 1 an
        double durationInMillis = (duration.toStandardDuration().getMillis() * MILLIS_FOR_ONE_YEAR)
                / PRIMARY_DIRECTION_ARC.toStandardDuration().getMillis();
        return new Period((long)durationInMillis);
    }

    public static String formatPeriod(Period period) {
        return PERIOD_FORMATTER_BUILDER.toFormatter().print(period);
    }

    public static DateTime convertDateTimeInPrimaryDirectionTime(DateTime natalDate, DateTime moreRecentDate) {
        long diff = moreRecentDate.getMillis() - natalDate.getMillis();

        // 1h = 15 ans, 4 min = 1 an (365,25 jours), 1 minute = 3 mois
        long minutes = diff * 4;
        long convertedDiff = minutes / 525960;

        return natalDate.plus(convertedDiff);
    }
}
