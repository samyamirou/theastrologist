package com.theastrologist.domain;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

import java.io.IOException;

/**
 * Created by SAM on 03/08/2015.
 */
public class DateTimeJSONAdapter extends TypeAdapter<DateTime>{

    @Override
    public void write(JsonWriter out, DateTime dateTime) throws IOException {
            String returnString;
        if(dateTime == null) {
            returnString = "-";
        } else {
            if (dateTime.getHourOfDay() == 0 && dateTime.getMinuteOfHour() == 0 && dateTime.getSecondOfMinute() == 0) {
                returnString = dateTime.toString(ISODateTimeFormat.date());
            } else {
                returnString = dateTime.toString(ISODateTimeFormat.dateTimeNoMillis());
            }
        }
        out.value(returnString);
    }

    @Override
    public DateTime read(JsonReader in) throws IOException {
        // May be it came in formatted as a java.util.Date, so try that
        DateTime dateTime = DateTime.parse(in.nextString(), ISODateTimeFormat.dateTimeNoMillis());
        return dateTime;
    }
}
