package com.theastrologist.service;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.theastrologist.domain.Planet;
import com.theastrologist.domain.PlanetPosition;
import com.theastrologist.domain.SkyPosition;
import com.theastrologist.domain.aspect.AspectPosition;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.SortedMap;

@Service
public class AspectService {

	public AspectService() {}

	/**
	 * Calcul des aspects sur une carte du ciel
	 *
	 * @param skyPosition la carte du ciel de référence
	 * @return une map contenant par planète une map avec correspondance entre l'aspect et la planète comparée
	 */
	public SortedMap<Planet, SortedMap<Planet, AspectPosition>> createAspectsForSkyPosition(SkyPosition skyPosition) {

		return createAspectsForComparison(skyPosition, skyPosition, false);
	}

	/**
	 * Calcul des aspects pour une synastrie
	 *
	 * @param skyPosition           la carte du ciel servant de référence
	 * @param skyPositionComparison la carte du ciel à comparer
	 * @return une map contenant par planète une map avec correspondance entre l'aspect et la planète comparée
	 */
	private SortedMap<Planet, SortedMap<Planet, AspectPosition>> createAspectsForComparison(SkyPosition skyPosition,
																							SkyPosition skyPositionComparison,
																							boolean transit) {

		SortedMap<Planet, SortedMap<Planet, AspectPosition>> aspectMapForPlanet = Maps.newTreeMap();

		for (Planet planet : Planet.values()) {
			PlanetPosition planetPosition = skyPosition.getPlanetPosition(planet);
			SortedMap<Planet, AspectPosition> aspectMap;
			if (!aspectMapForPlanet.containsKey(planet)) {
				aspectMap = Maps.newTreeMap();
				aspectMapForPlanet.put(planet, aspectMap);
			} else {
				aspectMap = aspectMapForPlanet.get(planet);
			}

			List<Planet> planets = transit ? Planet.getTransitPlanets() : Lists.newArrayList(Planet.values());
			for (Planet planetComparison : planets) {
				if (planet != planetComparison || skyPosition != skyPositionComparison) {
					PlanetPosition planetComparisonPosition = skyPositionComparison.getPlanetPosition(planetComparison);
					AspectPosition aspectPosition;

					if (transit) {
						aspectPosition = AspectPosition.createTransitPosition(planet, planetComparison, planetPosition,
																			  planetComparisonPosition);
					} else {
						aspectPosition = AspectPosition.createAspectPosition(planet, planetComparison, planetPosition,
																			 planetComparisonPosition);
					}

					if (aspectPosition != null) {
						aspectMap.put(planetComparison, aspectPosition);
					}
				}
			}
		}

		return aspectMapForPlanet;

	}


	/**
	 * Calcul des aspects pour un transit (orbes différentes)
	 *
	 * @param skyPosition     la carte du ciel servant de référence
	 * @param transitPosition la carte du ciel à comparer
	 * @return une map contenant par planète natale une map avec correspondance entre l'aspect et la planète en transit
	 */
	public SortedMap<Planet, SortedMap<Planet, AspectPosition>> createAspectsForTransit(SkyPosition skyPosition,
																						SkyPosition transitPosition) {
		return createAspectsForComparison(skyPosition, transitPosition, true);
	}

	/**
	 * Calcul des aspects pour un transit (orbes différentes)
	 *
	 * @param skyPosition     la carte du ciel servant de référence
	 * @param skyPositionComparison la carte du ciel à comparer
	 * @return une map contenant par planète en transit une map avec correspondance entre l'aspect et la planète comparée
	 */
	public SortedMap<Planet, SortedMap<Planet, AspectPosition>> createAspectsForSynastry(SkyPosition skyPosition,
																						 SkyPosition skyPositionComparison) {
		return createAspectsForComparison(skyPosition, skyPositionComparison, false);
	}
}
