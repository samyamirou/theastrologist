package com.theastrologist.domain.planetvalue;

import com.google.common.collect.Lists;
import com.theastrologist.domain.Planet;
import com.theastrologist.domain.SkyArtefact;

import java.util.List;

/**
 * Created by Samy on 26/07/2015.
 */
public class PlanetValue implements Comparable<PlanetValue> {
    private Planet planet;
    private int value = 0;
    private List<PlanetValueReason> reasons = Lists.newArrayList();

    public PlanetValue(Planet planet) {
        this.planet = planet;
    }

    public int compareTo(PlanetValue o) {
        int returnValue = 0;
        if(o == null) {
            returnValue = 1;
        } else {
            // On retourne le truc car on veut que le premier soit celui avec le moins de points
            returnValue = o.value - value;
            if(returnValue == 0) {
                returnValue = o.planet.toString().compareTo(planet.toString());
            }
        }
        return returnValue;
    }

    public int getValue() {
        return value;
    }

    public Planet getPlanet() {
        return planet;
    }

    public void appendValue(PlanetValueReasonType reason, SkyArtefact skyArtefact) {
        int points = reason.getPoints();
        this.value += points;
        reasons.add(new PlanetValueReason(reason, skyArtefact, points));
    }
}
