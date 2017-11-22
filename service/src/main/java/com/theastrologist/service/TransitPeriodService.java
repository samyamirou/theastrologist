package com.theastrologist.service;

import com.theastrologist.domain.*;
import com.theastrologist.domain.aspect.AspectPosition;
import com.theastrologist.domain.transitperiod.TransitPeriodsBuilder;
import com.theastrologist.domain.transitperiod.TransitPeriods;
import com.theastrologist.util.CalcUtil;
import org.apache.log4j.Logger;
import org.joda.time.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.SortedMap;

@Service
public class TransitPeriodService {

	private static final Logger LOG = Logger.getLogger(TransitPeriodService.class);
	private static final ReadablePeriod PERIOD_TO_ADD = Weeks.ONE;
	private static final ReadablePeriod PERIOD_TO_ADD_FIN = Days.ONE;

	@Autowired
	private AspectService aspectService;

	@Autowired
	private ThemeService themeService;

	public TransitPeriodService() {}

	public TransitPeriods createTransitPeriod(SkyPosition natalTheme, DateTime startDate, DateTime endDate,
											  Degree latitude, Degree longitude) {
		TransitPeriodsBuilder builder = new TransitPeriodsBuilder();
		DateTime currentDate = startDate;
		currentDate = fillPeriods(currentDate, natalTheme, endDate, latitude, longitude, builder, PERIOD_TO_ADD);

		// A la fin, on vérifie si on colle sur la vraie date de fin
		if (currentDate.isBefore(endDate)) {
			// Et on finit les quelques jours qui manquent
			currentDate = fillPeriods(currentDate, natalTheme, endDate, latitude, longitude, builder, PERIOD_TO_ADD_FIN);
		}

		// A la fin, une fois tous les transits ajoutés pour cette planète, on repasse sur les objets
		// Pour supprimer ceux qui n'ont pas de durée
		for (Planet planet : Planet.getTransitPlanets()) {
			builder.cleanTransitsWithNoLength(planet);
		}
		return builder.build();
	}

	private DateTime fillPeriods(DateTime currentDate, SkyPosition natalTheme, DateTime endDate, Degree latitude,
								 Degree longitude, TransitPeriodsBuilder builder, ReadablePeriod periodToAdd) {
		while (currentDate.isBefore(endDate) || currentDate.isEqual(endDate)) {
			SkyPosition currentSkyPosition = themeService.getSkyPosition(currentDate, latitude, longitude);
			builder.startNewPeriod(currentDate);

			appendPlanetPeriods(natalTheme, builder, currentSkyPosition);
			appendHousePeriods(natalTheme, builder, currentSkyPosition);

			DateTime plus = currentDate.plus(periodToAdd);
			if (! plus.isAfter(endDate)) {
				currentDate = plus;
			} else {
				break;
			}
		}
		return currentDate;
	}

	private void appendHousePeriods(SkyPosition natalTheme, TransitPeriodsBuilder builder,
									SkyPosition currentSkyPosition) {
		Degree ascendantDegree = natalTheme.getPlanetPosition(Planet.ASCENDANT).getDegree();
		for (Planet planetInTransit : Planet.getTransitPlanets()) {
			PlanetPosition planetPosition = currentSkyPosition.getPlanetPosition(planetInTransit);
			Degree degree = planetPosition.getDegree();
			House houseInTheme = CalcUtil.getHouse(degree, ascendantDegree);
			builder.appendHouseTransit(houseInTheme, planetInTransit);
		}
	}

	private void appendPlanetPeriods(SkyPosition natalTheme, TransitPeriodsBuilder builder,
									 SkyPosition currentSkyPosition) {
		SortedMap<Planet, SortedMap<Planet, AspectPosition>> aspectsForTransit = aspectService
				.createAspectsForTransit(natalTheme, currentSkyPosition);

		for (Planet natalPlanet : aspectsForTransit.keySet()) {
			SortedMap<Planet, AspectPosition> aspectsForPlanet = aspectsForTransit.get(natalPlanet);
			for (Planet planetInTransit : aspectsForPlanet.keySet()) {
				AspectPosition aspectPosition = aspectsForPlanet.get(planetInTransit);
				House masteredHouse = natalTheme.getMasteredHouse(natalPlanet);
				House secondMasteredHouse = natalTheme.getSecondMasteredHouse(natalPlanet);
				builder.appendPlanetTransit(natalPlanet, planetInTransit, aspectPosition.getAspect(), masteredHouse, secondMasteredHouse);
			}
		}
	}
}
