package com.theastrologist.service;

import com.theastrologist.ServiceTestConfiguration;
import com.theastrologist.domain.Degree;
import com.theastrologist.domain.Planet;
import com.theastrologist.domain.PlanetPosition;
import com.theastrologist.domain.SkyPosition;
import com.theastrologist.domain.aspect.Aspect;
import com.theastrologist.domain.aspect.AspectPosition;
import com.theastrologist.domain.transitperiod.PlanetTransitPeriod;
import com.theastrologist.domain.transitperiod.TransitPeriods;
import com.theastrologist.domain.transitperiod.TransitPeriodsBuilder;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Iterator;
import java.util.Map;
import java.util.SortedSet;
import java.util.TimeZone;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.junit.MatcherAssert.assertThat;

/**
 * Created by SAM on 30/06/2015.
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {ServiceTestConfiguration.class})
public class PlanetTransitPeriodTest {

	private Degree asDegree = new Degree(341, 46);
	private PlanetPosition asPosition = PlanetPosition.createPlanetPosition(asDegree, asDegree);

	public static final DateTimeZone DATE_TIME_ZONE = DateTimeZone.forTimeZone(TimeZone.getTimeZone("Europe/Paris"));
	private final DateTime TEST_NATAL_DATE = new DateTime(1985, 1, 4, 11, 20, DATE_TIME_ZONE);
	private final DateTime TEST_DATE_TODAY = new DateTime(2015, 6, 23, 20, 30, DATE_TIME_ZONE);
	private final Degree LATITUDE = new Degree(48, 39);
	private final Degree LONGITUDE = new Degree(2, 25);

	@Autowired
    private ThemeService themeService;

	@Autowired
	private TransitPeriodService transitPeriodService;

	@Before
	public void setUp() throws Exception {

	}

	@Test
	public void testMapTransitOnePlanet() throws Exception {
		// Given
		TransitPeriodsBuilder builder = new TransitPeriodsBuilder();
		Degree asDegree = new Degree(341, 46);

		Planet planetInTransit = Planet.URANUS;
		PlanetPosition planetInTransitPosition = PlanetPosition.createPlanetPosition(new Degree(17, 48), asDegree);

		Planet natalPlanet = Planet.LILITH_MOYENNE;
		PlanetPosition natalPlanetPosition = PlanetPosition.createPlanetPosition(new Degree(13, 31), asDegree);
		DateTime dateTime = DateTime.parse("2015-10-29");

		AspectPosition aspectPosition = AspectPosition
				.createTransitPosition(natalPlanet, planetInTransit, natalPlanetPosition, planetInTransitPosition);

		// When
		builder.startNewPeriod(dateTime);
		builder.appendPlanetTransit(natalPlanet, planetInTransit, aspectPosition.getAspect(), null, null);
		TransitPeriods build = builder.build();

		// Then
		Map<Planet, SortedSet<PlanetTransitPeriod>> planetPeriods = build.getPlanetPeriods();
		assertThat(planetPeriods, hasKey(Planet.URANUS));
		SortedSet<PlanetTransitPeriod> planetTransitPeriodList = planetPeriods.get(Planet.URANUS);

		assertThat(planetTransitPeriodList, notNullValue());
		assertThat(planetTransitPeriodList, hasSize(1));

		PlanetTransitPeriod first = planetTransitPeriodList.first();
		assertThat(first, notNullValue());
		assertThat(first.getStartDate(), equalTo(dateTime));
		assertThat(first.getEndDate(), equalTo(dateTime));
	}

	@Test
	public void testMapTransitTwoPlanetsMerge() throws Exception {
		// Given
		TransitPeriodsBuilder builder = new TransitPeriodsBuilder();
		Degree asDegree = new Degree(341, 46);

		Planet natalPlanet = Planet.SATURNE;
		PlanetPosition natalPlanetPosition = PlanetPosition.createPlanetPosition(new Degree(235, 3), asDegree);
		Planet planetInTransit = Planet.SATURNE;

		PlanetPosition planetInTransitPosition = PlanetPosition.createPlanetPosition(new Degree(238, 25), asDegree);
		DateTime dateTime = DateTime.parse("2015-08-15");
		AspectPosition aspectPosition = AspectPosition
				.createTransitPosition(natalPlanet, planetInTransit, natalPlanetPosition, planetInTransitPosition);

		builder.startNewPeriod(dateTime);
		builder.appendPlanetTransit(planetInTransit, natalPlanet, aspectPosition.getAspect(), null, null);

		PlanetPosition planetInTransitSecondPosition = PlanetPosition
				.createPlanetPosition(new Degree(239, 50), asDegree);
		DateTime secondDateTime = DateTime.parse("2015-09-15");
		AspectPosition aspectSecondPosition = AspectPosition
				.createTransitPosition(natalPlanet, planetInTransit, natalPlanetPosition,
									   planetInTransitSecondPosition);

		builder.startNewPeriod(secondDateTime);
		builder.appendPlanetTransit(planetInTransit, natalPlanet, aspectSecondPosition.getAspect(), null, null);

		// When
		TransitPeriods build = builder.build();
		Map<Planet, SortedSet<PlanetTransitPeriod>> map = build.getPlanetPeriods();


		// Then
		assertThat(map, hasKey(Planet.SATURNE));
		SortedSet<PlanetTransitPeriod> planetTransitPeriodList = map.get(Planet.SATURNE);

		assertThat(planetTransitPeriodList, notNullValue());
		assertThat(planetTransitPeriodList, hasSize(1));

		PlanetTransitPeriod second = planetTransitPeriodList.last();
		assertThat(second, notNullValue());
		assertThat(second.getStartDate(), equalTo(dateTime));
		assertThat(second.getEndDate(), equalTo(secondDateTime));
	}

	@Test
	public void testMapTransitTwoElementsDifferentAspect() throws Exception {
		// Given
		TransitPeriodsBuilder builder = new TransitPeriodsBuilder();
		Degree asDegree = new Degree(341, 46);

		Planet natalPlanet = Planet.MERCURE;
		PlanetPosition natalPlanetPosition = PlanetPosition.createPlanetPosition(new Degree(261, 14), asDegree);
		Planet planetInTransit = Planet.SOLEIL;

		PlanetPosition planetInTransitPosition = PlanetPosition.createPlanetPosition(new Degree(172, 4), asDegree);
		DateTime dateTime = DateTime.parse("2015-09-15");
		AspectPosition aspectPosition = AspectPosition
				.createTransitPosition(natalPlanet, planetInTransit, natalPlanetPosition, planetInTransitPosition);

		builder.startNewPeriod(dateTime);
		builder.appendPlanetTransit(natalPlanet, planetInTransit, aspectPosition.getAspect(), null, null);

		PlanetPosition planetInTransitSecondPosition = PlanetPosition
				.createPlanetPosition(new Degree(201, 36), asDegree);
		DateTime secondDateTime = DateTime.parse("2015-10-15");
		AspectPosition aspectSecondPosition = AspectPosition
				.createTransitPosition(natalPlanet, planetInTransit, natalPlanetPosition,
									   planetInTransitSecondPosition);

		builder.startNewPeriod(secondDateTime);
		builder.appendPlanetTransit(natalPlanet, planetInTransit, aspectSecondPosition.getAspect(), null, null);

		// When
		TransitPeriods build = builder.build();
		Map<Planet, SortedSet<PlanetTransitPeriod>> map = build.getPlanetPeriods();


		// Then
		assertThat(map, hasKey(Planet.SOLEIL));
		SortedSet<PlanetTransitPeriod> planetTransitPeriodList = map.get(Planet.SOLEIL);

		assertThat(planetTransitPeriodList, notNullValue());
		assertThat(planetTransitPeriodList, hasSize(2));

		PlanetTransitPeriod first = planetTransitPeriodList.first();
		assertThat(first, notNullValue());
		assertThat(first.getStartDate(), equalTo(dateTime));
		assertThat(first.getEndDate(), equalTo(dateTime));

		PlanetTransitPeriod second = planetTransitPeriodList.last();
		assertThat(second, notNullValue());
		assertThat(second.getStartDate(), equalTo(secondDateTime));
		assertThat(second.getEndDate(), equalTo(secondDateTime));
	}

	@Test
	public void testFullTransitPeriod() throws Exception {
		// Given
		SkyPosition natalPosition = themeService.getSkyPosition(TEST_NATAL_DATE, LATITUDE, LONGITUDE);

		DateTime startDate = DateTime.parse("2014-01-01");
		DateTime endDate = DateTime.parse("2016-01-01");

		// When
		TransitPeriods build = transitPeriodService
				.createTransitPeriod(natalPosition, startDate, endDate, LATITUDE, LONGITUDE);
		Map<Planet, SortedSet<PlanetTransitPeriod>> map = build.getPlanetPeriods();

		// Then
		assertThat(map, hasKey(Planet.PLUTON));
		SortedSet<PlanetTransitPeriod> planetTransitPeriodList = map.get(Planet.PLUTON);

		assertThat(planetTransitPeriodList, notNullValue());
		assertThat(planetTransitPeriodList, not(emptyIterableOf(PlanetTransitPeriod.class)));

		Iterator<PlanetTransitPeriod> iterator = planetTransitPeriodList.iterator();
		iterator.next();
		PlanetTransitPeriod second = iterator.next();
		assertThat(second, notNullValue());
		assertThat(second.getNatalPlanet(), is(Planet.SOLEIL));
		assertThat(second.getAspect(), is(Aspect.CONJONCTION));
		assertThat(second.getStartDate(), equalTo(startDate));
		assertThat(second.getEndDate(), equalTo(endDate));

		assertThat(map, not(hasKey(Planet.SOLEIL)));
		assertThat(map, not(hasKey(Planet.LUNE)));
		assertThat(map, not(hasKey(Planet.MERCURE)));
		assertThat(map, not(hasKey(Planet.VENUS)));
	}

	@Test
	public void testSupprTwoElementsNoLength() throws Exception {
		// Given
		SkyPosition natalPosition = themeService.getSkyPosition(TEST_NATAL_DATE, LATITUDE, LONGITUDE);

		DateTime startDate = DateTime.parse("2016-08-01");
		DateTime endDate = DateTime.parse("2016-09-10");

		// When
		TransitPeriods build = transitPeriodService
				.createTransitPeriod(natalPosition, startDate, endDate, LATITUDE, LONGITUDE);
		Map<Planet, SortedSet<PlanetTransitPeriod>> map = build.getPlanetPeriods();

		// Then
		assertThat(map, hasKey(Planet.JUPITER));
		SortedSet<PlanetTransitPeriod> planetTransitPeriodList = map.get(Planet.JUPITER);

		assertThat(planetTransitPeriodList, notNullValue());
		assertThat(planetTransitPeriodList, not(emptyIterableOf(PlanetTransitPeriod.class)));
		assertThat(planetTransitPeriodList, hasSize(6));
	}

	@Test
	public void testTransitPeriodWithMasteredHouses() throws Exception {
		// Given
		SkyPosition natalPosition = themeService.getSkyPosition(TEST_NATAL_DATE, LATITUDE, LONGITUDE);

		DateTime startDate = DateTime.parse("2014-01-01");
		DateTime endDate = DateTime.parse("2016-01-01");

		// When
		TransitPeriods build = transitPeriodService
				.createTransitPeriod(natalPosition, startDate, endDate, LATITUDE, LONGITUDE);
		Map<Planet, SortedSet<PlanetTransitPeriod>> map = build.getPlanetPeriods();

		// Then
		assertThat(map, hasKey(Planet.MARS));
		SortedSet<PlanetTransitPeriod> planetTransitPeriodList = map.get(Planet.MARS);

		for (PlanetTransitPeriod planetTransitPeriod : planetTransitPeriodList) {
			assertThat(planetTransitPeriod.getMasteredHouse(), equalTo(natalPosition.getMasteredHouse(planetTransitPeriod.getNatalPlanet())));
			assertThat(planetTransitPeriod.getSecondMasteredHouse(), equalTo(natalPosition.getSecondMasteredHouse(planetTransitPeriod.getNatalPlanet())));
		}
	}
}
