package com.theastrologist.domain.planetvalue;

/**
 * Created by Samy on 26/07/2015.
 */
public enum PlanetValueReasonType {
    CONJONCTION_SUN_MOON_AS(10, true),
    CONJONCTION_MC_SUD_NODE(7, true),
    MAIN_SIGN(15, true),
    MASTER_SIGN(4, true),
    EXALTED_SIGN(2, true),
    DETRIMENT_SIGN(-4, true),
    FALL_SIGN(-2, true),
    IS_IN_MAIN_SIGN(2, true),
    MAIN_HOUSE(15, false),
    MASTER_HOUSE(4, false),
    EXALTED_HOUSE(2, false),
    DETRIMENT_HOUSE(-4, false),
    FALL_HOUSE(-2, false),
    IS_IN_MAIN_HOUSE(2, false);

    private final int points;
    private final boolean signReason;

    PlanetValueReasonType(int points, boolean signReason) {

        this.points = points;
        this.signReason = signReason;
    }

    public boolean isSignReason() {
        return signReason;
    }

    public boolean isHouseReason() {
        return !signReason;
    }

    public int getPoints() {
        return points;
    }
}
