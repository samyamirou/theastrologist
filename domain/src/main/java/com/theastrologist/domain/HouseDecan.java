package com.theastrologist.domain;

public class HouseDecan extends Decan {

    private final House baseHouse;
    private House relatedHouse;

    public HouseDecan(Degree relativeDegree, House baseHouse) {
        super(relativeDegree);
        this.baseHouse = baseHouse;
        this.relatedHouse = House.getHouse(calculateRelatedHouseOrSign(this.decanNumber, baseHouse.getHouseNumber()));
    }

    public static HouseDecan getDecan(Degree relativeDegree, House baseHouse) {
        return new HouseDecan(relativeDegree, baseHouse);
    }

    public House getRelatedHouse() {
        return relatedHouse;
    }

    public House getBaseHouse() {
        return baseHouse;
    }
}
