package com.theastrologist.domain;

import com.theastrologist.util.CalcUtil;

public class PlanetPosition {
    private Sign sign;
    private Degree degree;
    private Degree degreeInSign;
    private final SignDecan decanInSign;
    private House house;
    private Degree degreeInHouse;
    private final HouseDecan decanInHouse;
    private boolean retrograde = false;

    public PlanetPosition(Degree degree, Sign sign, House house, Degree degreeInSign, Degree degreeInHouse) {
        this.degree = degree;
        this.sign = sign;
        this.house = house;
        this.degreeInSign = degreeInSign;
        this.decanInSign = SignDecan.getDecan(degreeInSign, sign);
        this.degreeInHouse = degreeInHouse;
        this.decanInHouse = HouseDecan.getDecan(degreeInHouse, house);
    }

    public static PlanetPosition createPlanetPosition(Degree degree, Degree asDegree) {
        return new PlanetPosition(
                degree,
                CalcUtil.getSign(degree),
                CalcUtil.getHouse(degree, asDegree),
                CalcUtil.getDegreeInSign(degree),
                CalcUtil.getDegreeInHouse(degree, asDegree)
        );
    }

    public SignDecan getDecanInSign() {
        return decanInSign;
    }

    public HouseDecan getDecanInHouse() {
        return decanInHouse;
    }

    public Degree getDegree() {
        return degree;
    }

    public void setDegree(Degree degree) {
        this.degree = degree;
    }

    public Sign getSign() {
        return sign;
    }

    public House getHouse() {
        return house;
    }

    public void setHouse(House house) {
        this.house = house;
    }

    public Degree getDegreeInSign() {
        return degreeInSign;
    }

    public Degree getDegreeInHouse() {
        return degreeInHouse;
    }

    public void setRetrograde(boolean retrograde) {
        this.retrograde = retrograde;
    }

    public boolean isRetrograde() {
        return retrograde;
    }

    @Override
    public String toString() {
        return this.sign.name() + " " + this.degreeInSign;
    }
}
