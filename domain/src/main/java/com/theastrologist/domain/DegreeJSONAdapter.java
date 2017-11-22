package com.theastrologist.domain;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * Created by SAM on 03/08/2015.
 */
public class DegreeJSONAdapter extends TypeAdapter<Degree> {
    @Override
    public void write(JsonWriter out, Degree degree) throws IOException {
        out.value(degree.toString());
    }

    @Override
    public Degree read(JsonReader in) throws IOException {
        // May be it came in formatted as a java.util.Date, so try that
        Degree degree;
        try {
            double nextDouble = in.nextDouble();
            degree = new Degree(nextDouble);
        } catch (IOException ioe) {
            String nextString = in.nextString();
            degree = new Degree(nextString);
        }
        return degree;
    }
}
