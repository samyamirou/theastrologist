package com.theastrologist.domain.transitperiod;

import com.theastrologist.domain.Planet;

import java.util.Map;
import java.util.SortedSet;

/**
 * Created by Samy on 16/09/2015.
 */
public class TransitPeriods {
	private Map<Planet, SortedSet<PlanetTransitPeriod>> planetPeriods;
	private Map<Planet, SortedSet<HouseTransitPeriod>> housePeriods;

	public Map<Planet, SortedSet<PlanetTransitPeriod>> getPlanetPeriods() {
		return planetPeriods;
	}

	public void setPlanetPeriods(
			Map<Planet, SortedSet<PlanetTransitPeriod>> planetPeriods) {
		this.planetPeriods = planetPeriods;
	}

	public Map<Planet, SortedSet<HouseTransitPeriod>> getHousePeriods() {
		return housePeriods;
	}

	public void setHousePeriods(
			Map<Planet, SortedSet<HouseTransitPeriod>> housePeriods) {
		this.housePeriods = housePeriods;
	}
}
