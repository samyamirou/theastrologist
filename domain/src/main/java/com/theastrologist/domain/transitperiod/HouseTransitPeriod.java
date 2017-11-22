package com.theastrologist.domain.transitperiod;

import com.google.gson.annotations.JsonAdapter;
import com.theastrologist.domain.DateTimeJSONAdapter;
import com.theastrologist.domain.House;
import com.theastrologist.domain.Planet;
import org.joda.time.DateTime;

/**
 * Created by SAM on 20/07/2015.
 */
public class HouseTransitPeriod implements Comparable {
	transient Planet transitPlanet;
	House natalHouse;
	@JsonAdapter(DateTimeJSONAdapter.class)
	DateTime beforeTransitionStartDate;
	@JsonAdapter(DateTimeJSONAdapter.class)
	DateTime startDate;
	@JsonAdapter(DateTimeJSONAdapter.class)
	DateTime endDate;
	@JsonAdapter(DateTimeJSONAdapter.class)
	DateTime afterTransitionEndDate;

	public HouseTransitPeriod(Planet transitPlanet, House natalHouse, DateTime startDate) {
		this(transitPlanet, natalHouse, startDate, startDate);
	}

	public HouseTransitPeriod(Planet transitPlanet, House natalHouse, DateTime startDate, DateTime endDate) {
		this.transitPlanet = transitPlanet;
		this.natalHouse = natalHouse;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public House getNatalHouse() {
		return natalHouse;
	}

	public Planet getTransitPlanet() {
		return transitPlanet;
	}

	public DateTime getStartDate() {
		return startDate;
	}

	public DateTime getEndDate() {
		return endDate;
	}

	public void setEndDate(DateTime endDate) {
		this.endDate = endDate;
	}

	public void setTransitPlanet(Planet transitPlanet) {
		this.transitPlanet = transitPlanet;
	}

	public void setNatalHouse(House natalHouse) {
		this.natalHouse = natalHouse;
	}

	public DateTime getBeforeTransitionStartDate() {
		return beforeTransitionStartDate;
	}

	public void setBeforeTransitionStartDate(DateTime beforeTransitionStartDate) {
		this.beforeTransitionStartDate = beforeTransitionStartDate;
	}

	public void setStartDate(DateTime startDate) {
		this.startDate = startDate;
	}

	public DateTime getAfterTransitionEndDate() {
		return afterTransitionEndDate;
	}

	public void setAfterTransitionEndDate(DateTime afterTransitionEndDate) {
		this.afterTransitionEndDate = afterTransitionEndDate;
	}

	public int compareTo(Object o) {
		int returnedValue;
		if (o == null || !(o instanceof HouseTransitPeriod)) {
			returnedValue = 1;
		} else {
			HouseTransitPeriod obj = (HouseTransitPeriod) o;
			// D'abord on compare les dates comme moyen de comparaison
			returnedValue = startDate.compareTo(obj.startDate);

			// Puis on compare les planètes ensemble
			if(returnedValue == 0) {
				returnedValue = natalHouse.compareTo(obj.natalHouse);
			}

			if(returnedValue == 0) {
				returnedValue = transitPlanet.compareTo(obj.transitPlanet);
			}
		}
		return returnedValue;
	}
}
