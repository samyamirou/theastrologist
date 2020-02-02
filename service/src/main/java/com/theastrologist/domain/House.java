package com.theastrologist.domain;


import swisseph.SweConst;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SAM on 16/11/2014.
 */
public enum House implements SkyArtefact {
	I(1, SweConst.SE_HOUSE1),
	II(2, SweConst.SE_HOUSE2),
	III(3, SweConst.SE_HOUSE3),
	IV(4, SweConst.SE_HOUSE4),
	V(5, SweConst.SE_HOUSE5),
	VI(6, SweConst.SE_HOUSE6),
	VII(7, SweConst.SE_HOUSE7),
	VIII(8, SweConst.SE_HOUSE8),
	IX(9, SweConst.SE_HOUSE9),
	X(10, SweConst.SE_HOUSE10),
	XI(11, SweConst.SE_HOUSE11),
	XII(12, SweConst.SE_HOUSE12);

	private int houseNumber;
	private final int sweConst;

	public int getSweConst() {
		return sweConst;
	}

	public int getHouseNumber() {
		return houseNumber;
	}

	House(int houseNumber, int sweConst) {
		this.houseNumber = houseNumber;
		this.sweConst = sweConst;
	}

	private static List<House> houses = new ArrayList<House>(12);

	static {
		for (House house : House.values()) {
			houses.add(house.houseNumber - 1, house);
		}
	}

	public static House getHouse(int i) {
		return houses.get(i - 1);
	}

	public boolean isMasterPlanet(Planet planet) {
		return Sign.getSign(houseNumber).isMasterPlanet(planet);
	}

	public boolean isExaltedPlanet(Planet planet) {
		return Sign.getSign(houseNumber).isExaltedPlanet(planet);
	}

	public boolean isExilPlanet(Planet planet) {
		return Sign.getSign(houseNumber).isExilPlanet(planet);
	}

	public boolean isChutePlanet(Planet planet) {
		return Sign.getSign(houseNumber).isChutePlanet(planet);
	}

	public House getNextHouse() {
		House returnedValue;
		if (this == House.XII) {
			returnedValue = House.I;
		} else {
			returnedValue = getHouse(this.houseNumber + 1);
		}
		return returnedValue;
	}

	public House getPreviousHouse() {
		House returnedValue;
		if (this == House.I) {
			returnedValue = House.XII;
		} else {
			returnedValue = getHouse(this.houseNumber - 1);
		}
		return returnedValue;
	}
}
