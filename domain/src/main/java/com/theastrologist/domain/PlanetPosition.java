package com.theastrologist.domain;

import com.theastrologist.util.CalcUtil;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "PlanetPositions")
public class PlanetPosition {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", columnDefinition = "VARCHAR(255)")
    private UUID id;

    @Enumerated(EnumType.STRING)
    private Sign sign;

    @Embedded
    @AttributeOverride(name="baseDegree",column=@Column(name="degree"))
    private Degree degree;

    @Embedded
    @AttributeOverride(name="baseDegree",column=@Column(name="degreeInSign"))
    private Degree degreeInSign;

    @Embedded
    private SignDecan decanInSign;

    @Enumerated(EnumType.STRING)
    private House house;

    @Enumerated(EnumType.STRING)
    private Degree degreeInHouse;

    @Embedded
    private HouseDecan decanInHouse;

    private boolean retrograde = false;

    public PlanetPosition() {
    }

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
