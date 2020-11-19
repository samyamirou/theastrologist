package com.theastrologist.domain;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Transient;

@Embeddable
public class HouseDecan extends Decan {

    @Enumerated(EnumType.STRING)
    private House baseHouse;

    @Enumerated(EnumType.STRING)
    private House relatedHouse;

    public HouseDecan() {
    }

    // Pour la serialization
    public HouseDecan(House baseHouse, House relatedHouse) {
        this.baseHouse = baseHouse;
        this.relatedHouse = relatedHouse;
        this.decanNumber = calculateDecan(baseHouse.getHouseNumber(), relatedHouse.getHouseNumber());
    }

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
