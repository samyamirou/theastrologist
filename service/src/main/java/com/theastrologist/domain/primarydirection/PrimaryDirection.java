package com.theastrologist.domain.primarydirection;

import com.google.gson.annotations.JsonAdapter;
import com.theastrologist.domain.DateTimeJSONAdapter;
import com.theastrologist.domain.Degree;
import com.theastrologist.domain.Planet;
import com.theastrologist.domain.aspect.Aspect;
import com.theastrologist.util.DateUtil;
import org.joda.time.DateTime;
import org.joda.time.Period;

import java.text.DecimalFormat;

public class PrimaryDirection implements Comparable<PrimaryDirection> {
    @JsonAdapter(DateTimeJSONAdapter.class)
    DateTime date;
    Degree arc;
    transient
    Period arcDuration;
    Aspect aspect;
    Planet planet;
    Planet directedPlanet;

    public PrimaryDirection(DateTime date, Degree position, Period arcDuration, Aspect aspect, Planet planet, Planet directedPlanet) {
        this.date = date;
        this.arc = position;
        this.arcDuration = arcDuration;
        this.aspect = aspect;
        this.planet = planet;
        this.directedPlanet = directedPlanet;
    }

    public DateTime getDate() {
        return date;
    }

    public Degree getArc() {
        return arc;
    }

    public Aspect getAspect() {
        return aspect;
    }

    public Planet getPlanet() {
        return planet;
    }

    public Planet getDirectedPlanet() {
        return directedPlanet;
    }

    public Period getArcDuration() {
        return arcDuration;
    }

    @Override
    public int compareTo(PrimaryDirection o) {
        int comparison;
        if(o == null) {
            comparison = -1;
        } else {
            int degreeComparison = this.date.compareTo(o.date);
            if(degreeComparison == 0) {
                comparison = planet.compareTo(o.planet);
            } else {
                comparison = degreeComparison;
            }
        }
        return comparison;
    }

    @Override
    public String toString() {
        return date +
                "\t-\t" + arc + " (" + new DecimalFormat("#.###").format(arc.getBaseDegree()) + ")" +
                "\t-\t" + DateUtil.formatPeriod(arcDuration) +
                "\t-\t" + planet.name() +
                "\t" + aspect.name() +
                "\t" + directedPlanet.name();
    }
}
