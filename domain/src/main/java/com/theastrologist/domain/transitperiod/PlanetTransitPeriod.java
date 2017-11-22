package com.theastrologist.domain.transitperiod;

import com.google.gson.annotations.JsonAdapter;
import com.theastrologist.domain.DateTimeJSONAdapter;
import com.theastrologist.domain.House;
import com.theastrologist.domain.Planet;
import com.theastrologist.domain.aspect.Aspect;
import org.joda.time.DateTime;

/**
 * Created by SAM on 20/07/2015.
 */
public class PlanetTransitPeriod implements Comparable {
	private transient Planet transitPlanet;
	private Planet natalPlanet;
	private Aspect aspect;
	@JsonAdapter(DateTimeJSONAdapter.class)
	private DateTime startDate;
	@JsonAdapter(DateTimeJSONAdapter.class)
	private DateTime endDate;
	private House masteredHouse;
	private House secondMasteredHouse;

	PlanetTransitPeriod(Planet transitPlanet, Planet natalPlanet, Aspect aspect, DateTime startDate, House masteredHouse, House secondMasteredHouse) {
		this(transitPlanet, natalPlanet, aspect, startDate, startDate, masteredHouse, secondMasteredHouse);
	}

	private PlanetTransitPeriod(Planet transitPlanet, Planet natalPlanet, Aspect aspect, DateTime startDate, DateTime endDate, House masteredHouse, House secondMasteredHouse) {
		this.transitPlanet = transitPlanet;
		this.natalPlanet = natalPlanet;
		this.aspect = aspect;
		this.startDate = startDate;
		this.endDate = endDate;
		this.masteredHouse = masteredHouse;
		this.secondMasteredHouse = secondMasteredHouse;
	}

	public Planet getNatalPlanet() {
		return natalPlanet;
	}

	public Planet getTransitPlanet() {
		return transitPlanet;
	}

	public Aspect getAspect() {
		return aspect;
	}

	public DateTime getStartDate() {
		return startDate;
	}

	public DateTime getEndDate() {
		return endDate;
	}

	void setEndDate(DateTime endDate) {
		this.endDate = endDate;
	}

	public House getMasteredHouse() {
		return masteredHouse;
	}

	public House getSecondMasteredHouse() {
		return secondMasteredHouse;
	}

	public int compareTo(Object o) {
		int returnedValue;
		if (!(o instanceof PlanetTransitPeriod)) {
			returnedValue = 1;
		} else {
			PlanetTransitPeriod obj = (PlanetTransitPeriod) o;
			// D'abord on compare les dates comme moyen de comparaison
			returnedValue = startDate.compareTo(obj.startDate);

			// Puis on compare les planètes ensemble
			if(returnedValue == 0) {
				returnedValue = natalPlanet.compareTo(obj.natalPlanet);
			}

			if(returnedValue == 0) {
				returnedValue = transitPlanet.compareTo(obj.transitPlanet);
			}
		}
		return returnedValue;
	}

}
