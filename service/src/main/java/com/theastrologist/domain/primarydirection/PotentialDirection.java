package com.theastrologist.domain.primarydirection;

import com.theastrologist.domain.Planet;
import com.theastrologist.domain.PlanetPosition;
import com.theastrologist.domain.aspect.Aspect;

public class PotentialDirection implements Comparable<PotentialDirection> {
    private Planet planet;
    private Aspect aspect;
    private PlanetPosition position;
    private PlanetPosition originalPlanetPosition;

    public PotentialDirection() {}

    public PotentialDirection(Planet planet, Aspect aspect, PlanetPosition position, PlanetPosition originalPlanetPosition) {
        this.planet = planet;
        this.aspect = aspect;
        this.position = position;
        this.originalPlanetPosition = originalPlanetPosition;
    }

    public Aspect getAspect() {
        return aspect;
    }

    public PlanetPosition getPosition() {
        return position;
    }

    public PlanetPosition getOriginalPlanetPosition() {
        return originalPlanetPosition;
    }

    @Override
    public int compareTo(PotentialDirection o) {
        int comparison;
        if(o == null) {
            comparison = -1;
        } else {
            int degreeComparison = this.position.getDegree().compareTo(o.getPosition().getDegree());
            //int degreeComparison = - this.position.getDegree().compareTo(o.getPosition().getDegree());
            if(degreeComparison == 0) {
                comparison = planet.compareTo(o.planet);
            } else {
                comparison = degreeComparison;
            }
        }
        return comparison;
    }

    public Planet getPlanet() {
        return planet;
    }
}
