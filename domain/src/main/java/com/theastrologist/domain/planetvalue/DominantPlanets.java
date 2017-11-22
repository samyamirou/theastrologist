package com.theastrologist.domain.planetvalue;

import java.util.SortedSet;

public class DominantPlanets {

    private SortedSet<PlanetValue> signCalculation;
    private SortedSet<PlanetValue> houseCalculation;

    public SortedSet<PlanetValue> getSignCalculation() {
        return signCalculation;
    }

    public void setSignCalculation(SortedSet<PlanetValue> signCalculation) {
        this.signCalculation = signCalculation;
    }

    public SortedSet<PlanetValue> getHouseCalculation() {
        return houseCalculation;
    }

    public void setHouseCalculation(SortedSet<PlanetValue> houseCalculation) {
        this.houseCalculation = houseCalculation;
    }
}
