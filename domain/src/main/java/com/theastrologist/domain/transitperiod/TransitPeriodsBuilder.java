package com.theastrologist.domain.transitperiod;

import com.google.common.base.Predicate;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.theastrologist.domain.House;
import com.theastrologist.domain.Planet;
import com.theastrologist.domain.aspect.Aspect;
import org.joda.time.DateTime;

import java.util.Map;
import java.util.SortedSet;

/**
 * Created by Samy on 15/09/2015.
 */
public class TransitPeriodsBuilder {

	Map<Planet, SortedSet<PlanetTransitPeriod>> planetMap = Maps.newHashMap();
	Map<Planet, SortedSet<HouseTransitPeriod>> houseMap = Maps.newHashMap();

	private DateTime previousTime = null;
	private DateTime currentTime = null;

	Map<Planet, HouseTransitPeriod> previousPeriod = Maps.newHashMap();
	Map<Planet, HouseTransitPeriod> periodOfPreviousTime = Maps.newHashMap();

	public void appendPlanetTransit(Planet natalPlanet, Planet planetInTransit, Aspect transitType, House masteredHouse, House secondMasteredHouse) {
		if (currentTime == null) {
			throw new IllegalStateException("Cannot add transit if period not set");
		}

		if (planetMap.containsKey(planetInTransit)) {
			// on récupère le dernier élément et on le compare avec l'élément actuel
			SortedSet<PlanetTransitPeriod> planetTransitPeriods = planetMap.get(planetInTransit);

			PlanetTransitPeriod lastPlanetTransitPeriod = findLastPlanetTransitPeriod(natalPlanet, transitType,
					planetTransitPeriods);

			if (lastPlanetTransitPeriod != null) {
				// On met à jour l'ancienne période de transit pour qu'elle se fusionne avec la nouvelle
				lastPlanetTransitPeriod.setEndDate(currentTime);
			} else {

				// Sinon on en crée une nouvelle mais on l'ajoute à la liste
				PlanetTransitPeriod newPeriod = new PlanetTransitPeriod(planetInTransit, natalPlanet, transitType,
						currentTime, masteredHouse, secondMasteredHouse);
				planetTransitPeriods.add(newPeriod);
			}
		} else {
			insertNewPlanetTransitPeriod(planetInTransit, natalPlanet, transitType, masteredHouse, secondMasteredHouse);
		}
	}

	public void appendHouseTransit(House currentHouse, Planet planetInTransit) {
		if (currentTime == null) {
			throw new IllegalStateException("Cannot add transit if period not set");
		}

		HouseTransitPeriod currentPeriod = null;
		if (houseMap.containsKey(planetInTransit)) {

			// on récupère le dernier élément et on le compare avec l'élément actuel
			SortedSet<HouseTransitPeriod> houseTransitPeriods = houseMap.get(planetInTransit);

			HouseTransitPeriod lastPeriod = houseTransitPeriods.last();

			if (lastPeriod != null) {
				House lastHouse = lastPeriod.getNatalHouse();
				HouseTransitPeriod backPeriod;
				HouseTransitPeriod forwardPeriod;
				if (lastHouse == currentHouse) {
					// On met à jour l'ancienne période de transit pour qu'elle se fusionne avec la nouvelle
					forwardPeriod = lastPeriod;
					backPeriod = previousPeriod.getOrDefault(planetInTransit, null);
					currentPeriod = lastPeriod;
				} else {
					if (currentHouse.getPreviousHouse() == lastHouse ||
							(planetInTransit == Planet.NOEUD_NORD_MOYEN && currentHouse.getNextHouse() == lastHouse)) {
						// Ici on est dans le cas du passage à une nouvelle période
						backPeriod = lastPeriod;
						backPeriod.setEndDate(currentTime);
						forwardPeriod = insertNewHousePeriod(currentHouse, planetInTransit, houseTransitPeriods);
						forwardPeriod.setBeforeTransitionStartDate(currentTime);
						previousPeriod.put(planetInTransit, backPeriod);
						currentPeriod = forwardPeriod;
					} else if (currentHouse.getNextHouse() == lastHouse) {
						// Ici on est dans un cas de rétrogradation, on est encore en transition
						forwardPeriod = lastPeriod;
						DateTime startDate = forwardPeriod.getStartDate();
						forwardPeriod.setStartDate(currentTime);
						if (previousPeriod.containsKey(planetInTransit)) {
							backPeriod = previousPeriod.get(planetInTransit);
						} else {
							// Ici on est dans le cas où il n'y a pas de previous et qu'on rétrograde
							backPeriod = insertNewHousePeriod(currentHouse, planetInTransit, houseTransitPeriods);
							forwardPeriod.setBeforeTransitionStartDate(startDate);
							backPeriod.setStartDate(startDate);
							backPeriod.setEndDate(startDate);
							backPeriod.setAfterTransitionEndDate(currentTime);
						}
						previousPeriod.put(planetInTransit, backPeriod);
						currentPeriod = backPeriod;
					} else {
						throw new IllegalStateException(
								"too big gap between houses : current house = " + currentHouse + ", last house = " + lastHouse + ", planet in transit = " + planetInTransit);
					}
				}

				// Si la maison d'avant existe, on vérifie ses dates
				//if (periodOfPreviousTime != currentPeriod && backPeriod != null &&
				//	backPeriod.getNatalHouse() == currentHouse.getPreviousHouse()) {
				if (periodOfPreviousTime.get(planetInTransit) != currentPeriod && backPeriod != null) {
					// A chaque fois qu'on saute une étape, on met à jour la date de transition
					backPeriod.setAfterTransitionEndDate(currentTime);
					if (backPeriod.getNatalHouse() == currentHouse.getPreviousHouse() ||
							(planetInTransit == Planet.NOEUD_NORD_MOYEN &&
									backPeriod.getNatalHouse() == currentHouse.getNextHouse())) {
						// Uniquement quand on saute une étape
						forwardPeriod.setStartDate(currentTime);
					}
				}
				forwardPeriod.setEndDate(currentTime);
			} else {
				// Sinon on en crée une nouvelle mais on l'ajoute à la liste
				currentPeriod = insertNewHousePeriod(currentHouse, planetInTransit, houseTransitPeriods);
			}
		} else {
			currentPeriod = insertNewHouseTransitPeriodList(planetInTransit, currentHouse);
		}
		periodOfPreviousTime.put(planetInTransit, currentPeriod);
	}

	private HouseTransitPeriod insertNewHousePeriod(House currentHouse, Planet planetInTransit,
	                                                SortedSet<HouseTransitPeriod> houseTransitPeriods) {
		HouseTransitPeriod newPeriod = new HouseTransitPeriod(planetInTransit, currentHouse, currentTime);
		houseTransitPeriods.add(newPeriod);
		return newPeriod;
	}

	private void insertNewPlanetTransitPeriod(Planet planetInTransit, Planet natalPlanet, Aspect transitType, House masteredHouse, House secondMasteredHouse) {
		PlanetTransitPeriod period = new PlanetTransitPeriod(planetInTransit, natalPlanet, transitType, currentTime, masteredHouse, secondMasteredHouse);
		SortedSet<PlanetTransitPeriod> sortedPeriods = Sets.newTreeSet();
		sortedPeriods.add(period);
		planetMap.put(planetInTransit, sortedPeriods);
	}

	private HouseTransitPeriod insertNewHouseTransitPeriodList(Planet planetInTransit, House natalHouse) {
		HouseTransitPeriod period = new HouseTransitPeriod(planetInTransit, natalHouse, currentTime);
		SortedSet<HouseTransitPeriod> sortedPeriods = Sets.newTreeSet();
		sortedPeriods.add(period);
		houseMap.put(planetInTransit, sortedPeriods);
		return period;
	}

	private PlanetTransitPeriod findLastPlanetTransitPeriod(final Planet natalPlanet, final Aspect transitType,
	                                                        SortedSet<PlanetTransitPeriod> planetTransitPeriods) {
		PlanetTransitPeriod planetTransitPeriod = null;
		SortedSet<PlanetTransitPeriod> filteredSet = Sets
				.filter(planetTransitPeriods, new Predicate<PlanetTransitPeriod>() {
					public boolean apply(PlanetTransitPeriod transitPeriod) {
						return transitPeriod.getNatalPlanet() == natalPlanet &&
								transitPeriod.getEndDate().equals(previousTime) &&
								transitType == transitPeriod.getAspect();
					}
				});

		if (filteredSet.size() > 0) {
			planetTransitPeriod = filteredSet.last();
		}
		return planetTransitPeriod;
	}

	public void startNewPeriod(DateTime dateTime) {
		endPeriod();
		currentTime = dateTime;
	}

	private void endPeriod() {
		previousTime = currentTime;
	}

	public TransitPeriods build() {
		endPeriod();
		TransitPeriods returnedValue = new TransitPeriods();
		returnedValue.setPlanetPeriods(planetMap);
		returnedValue.setHousePeriods(houseMap);
		return returnedValue;
	}

	public void cleanTransitsWithNoLength(Planet planetInTransit) {
		SortedSet<PlanetTransitPeriod> planetTransitPeriods = planetMap.get(planetInTransit);
		SortedSet<PlanetTransitPeriod> newPeriods = Sets
				.filter(planetTransitPeriods, new Predicate<PlanetTransitPeriod>() {
					public boolean apply(PlanetTransitPeriod transitPeriod) {
						return !transitPeriod.getStartDate().equals(transitPeriod.getEndDate());
					}
				});
		planetMap.put(planetInTransit, newPeriods);
	}
}
