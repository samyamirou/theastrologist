package com.theastrologist.service;

import com.theastrologist.ServiceTestConfiguration;
import com.theastrologist.domain.*;
import com.theastrologist.domain.transitperiod.HouseTransitPeriod;
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
public class HouseTransitPeriodTest {

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
	public void testMapTransitOneHouse() throws Exception {
		// Given
		TransitPeriodsBuilder builder = new TransitPeriodsBuilder();
		Degree asDegree = new Degree(341, 46);

		Planet planetInTransit = Planet.URANUS;
		PlanetPosition planetInTransitPosition = PlanetPosition.createPlanetPosition(new Degree(17, 48), asDegree);

		DateTime dateTime = DateTime.parse("2015-10-29");

		// When
		builder.startNewPeriod(dateTime);
		builder.appendHouseTransit(House.III, planetInTransit);
		TransitPeriods build = builder.build();

		// Then
		Map<Planet, SortedSet<HouseTransitPeriod>> housePeriods = build.getHousePeriods();
		assertThat(housePeriods, hasKey(Planet.URANUS));
		SortedSet<HouseTransitPeriod> houseTransitPeriodList = housePeriods.get(Planet.URANUS);

		assertThat(houseTransitPeriodList, notNullValue());
		assertThat(houseTransitPeriodList, hasSize(1));

		HouseTransitPeriod firstHouse = houseTransitPeriodList.first();
		assertThat(firstHouse, notNullValue());
		assertThat(firstHouse.getNatalHouse(), is(House.III));
		assertThat(firstHouse.getStartDate(), equalTo(dateTime));
		assertThat(firstHouse.getEndDate(), equalTo(dateTime));
	}

	@Test
	public void testMapTransitTwoHousesMerge() throws Exception {
		// Given
		TransitPeriodsBuilder builder = new TransitPeriodsBuilder();
		Degree asDegree = new Degree(341, 46);

		Planet planetInTransit = Planet.SATURNE;

		DateTime dateTime = DateTime.parse("2015-08-15");
		builder.startNewPeriod(dateTime);
		builder.appendHouseTransit(House.IX, planetInTransit);

		DateTime secondDateTime = DateTime.parse("2015-09-15");
		builder.startNewPeriod(secondDateTime);
		builder.appendHouseTransit(House.IX, planetInTransit);

		// When
		TransitPeriods build = builder.build();
		Map<Planet, SortedSet<HouseTransitPeriod>> map = build.getHousePeriods();

		// Then
		assertThat(map, hasKey(Planet.SATURNE));
		SortedSet<HouseTransitPeriod> houseTransitPeriodList = map.get(Planet.SATURNE);

		assertThat(houseTransitPeriodList, notNullValue());
		assertThat(houseTransitPeriodList, hasSize(1));

		HouseTransitPeriod second = houseTransitPeriodList.last();
		assertThat(second, notNullValue());
		assertThat(second.getStartDate(), equalTo(dateTime));
		assertThat(second.getEndDate(), equalTo(secondDateTime));
	}

	@Test
	public void testMassHousesMerge() throws Exception {
		// Given
		TransitPeriodsBuilder builder = new TransitPeriodsBuilder();

		Planet planetInTransit = Planet.MARS;

		builder.startNewPeriod(DateTime.parse("2014-04-30"));
		builder.appendHouseTransit(House.VIII, planetInTransit);

		builder.startNewPeriod(DateTime.parse("2014-05-07"));
		builder.appendHouseTransit(House.VIII, planetInTransit);

		builder.startNewPeriod(DateTime.parse("2014-05-14"));
		builder.appendHouseTransit(House.VII, planetInTransit);

		builder.startNewPeriod(DateTime.parse("2014-05-21"));
		builder.appendHouseTransit(House.VII, planetInTransit);

		builder.startNewPeriod(DateTime.parse("2014-05-28"));
		builder.appendHouseTransit(House.VIII, planetInTransit);

		// When
		TransitPeriods build = builder.build();
		Map<Planet, SortedSet<HouseTransitPeriod>> map = build.getHousePeriods();

		// Then
		assertThat(map, hasKey(Planet.MARS));
		SortedSet<HouseTransitPeriod> houseTransitPeriodList = map.get(Planet.MARS);

		assertThat(houseTransitPeriodList, notNullValue());
		assertThat(houseTransitPeriodList, hasSize(2));

		HouseTransitPeriod first = houseTransitPeriodList.first();
		assertThat(first, notNullValue());
		assertThat(first.getBeforeTransitionStartDate(), equalTo(null));
		assertThat(first.getStartDate(), equalTo(DateTime.parse("2014-04-30")));
		assertThat(first.getEndDate(), equalTo(DateTime.parse("2014-04-30")));
		assertThat(first.getAfterTransitionEndDate(), equalTo(DateTime.parse("2014-05-28")));

		HouseTransitPeriod second = houseTransitPeriodList.last();
		assertThat(second, notNullValue());
		assertThat(second.getBeforeTransitionStartDate(), equalTo(DateTime.parse("2014-04-30")));
		assertThat(second.getStartDate(), equalTo(DateTime.parse("2014-05-28")));
		assertThat(second.getEndDate(), equalTo(DateTime.parse("2014-05-28")));
		assertThat(second.getAfterTransitionEndDate(), equalTo(null));
	}

	@Test
	public void testMapTransitTwoHousesDifferent() throws Exception {
		// Given
		TransitPeriodsBuilder builder = new TransitPeriodsBuilder();
		Degree asDegree = new Degree(341, 46);

		Planet planetInTransit = Planet.SOLEIL;

		DateTime dateTime = DateTime.parse("2015-08-15");
		builder.startNewPeriod(dateTime);
		builder.appendHouseTransit(House.VI, planetInTransit);

		DateTime secondDateTime = DateTime.parse("2015-10-15");
		builder.startNewPeriod(secondDateTime);
		builder.appendHouseTransit(House.VII, planetInTransit);

		// When
		TransitPeriods build = builder.build();
		Map<Planet, SortedSet<HouseTransitPeriod>> map = build.getHousePeriods();


		// Then
		assertThat(map, hasKey(Planet.SOLEIL));
		SortedSet<HouseTransitPeriod> houseTransitPeriodList = map.get(Planet.SOLEIL);

		assertThat(houseTransitPeriodList, notNullValue());
		assertThat(houseTransitPeriodList, hasSize(2));

		HouseTransitPeriod first = houseTransitPeriodList.first();
		assertThat(first, notNullValue());
		assertThat(first.getStartDate(), equalTo(dateTime));
		assertThat(first.getEndDate(), equalTo(secondDateTime));

		HouseTransitPeriod second = houseTransitPeriodList.last();
		assertThat(second, notNullValue());
		assertThat(second.getStartDate(), equalTo(secondDateTime));
		assertThat(second.getEndDate(), equalTo(secondDateTime));
	}

	@Test
	public void testMapTransitTransitions() throws Exception {
		// Given
		TransitPeriodsBuilder builder = new TransitPeriodsBuilder();

		Planet planetInTransit = Planet.SATURNE;

		DateTime firstDateTime = DateTime.parse("2015-12-15");
		builder.startNewPeriod(firstDateTime);
		builder.appendHouseTransit(House.IX, planetInTransit);

		DateTime secondDateTime = DateTime.parse("2016-01-15");
		builder.startNewPeriod(secondDateTime);
		builder.appendHouseTransit(House.X, planetInTransit);

		DateTime thirdDateTime = DateTime.parse("2016-02-15");
		builder.startNewPeriod(thirdDateTime);
		builder.appendHouseTransit(House.X, planetInTransit);

		DateTime fourthDateTime = DateTime.parse("2016-07-15");
		builder.startNewPeriod(fourthDateTime);
		builder.appendHouseTransit(House.IX, planetInTransit);

		DateTime fifthDateTime = DateTime.parse("2016-10-15");
		builder.startNewPeriod(fifthDateTime);
		builder.appendHouseTransit(House.X, planetInTransit);

		DateTime sixthDateTime = DateTime.parse("2016-12-15");
		builder.startNewPeriod(sixthDateTime);
		builder.appendHouseTransit(House.X, planetInTransit);

		// When
		TransitPeriods build = builder.build();
		Map<Planet, SortedSet<HouseTransitPeriod>> map = build.getHousePeriods();

		// Then
		assertThat(map, hasKey(Planet.SATURNE));
		SortedSet<HouseTransitPeriod> houseTransitPeriodList = map.get(Planet.SATURNE);

		assertThat(houseTransitPeriodList, notNullValue());
		assertThat(houseTransitPeriodList, hasSize(2));

		HouseTransitPeriod first = houseTransitPeriodList.first();
		assertThat(first, notNullValue());
		assertThat(first.getBeforeTransitionStartDate(), equalTo(null));
		assertThat(first.getStartDate(), equalTo(firstDateTime));
		assertThat(first.getEndDate(), equalTo(secondDateTime));
		assertThat(first.getAfterTransitionEndDate(), equalTo(fifthDateTime));

		HouseTransitPeriod second = houseTransitPeriodList.last();
		assertThat(second, notNullValue());
		assertThat(second.getBeforeTransitionStartDate(), equalTo(secondDateTime));
		assertThat(second.getStartDate(), equalTo(fifthDateTime));
		assertThat(second.getEndDate(), equalTo(sixthDateTime));
		assertThat(second.getAfterTransitionEndDate(), equalTo(null));
	}

	@Test
	public void testMapTransitTransitionsNoeuds() throws Exception {
		// Given
		TransitPeriodsBuilder builder = new TransitPeriodsBuilder();

		Planet planetInTransit = Planet.NOEUD_NORD_MOYEN;

		DateTime firstDateTime = DateTime.parse("2015-01-08");
		builder.startNewPeriod(firstDateTime);
		builder.appendHouseTransit(House.VIII, planetInTransit);

		DateTime secondDateTime = DateTime.parse("2015-02-08");
		builder.startNewPeriod(secondDateTime);
		builder.appendHouseTransit(House.VIII, planetInTransit);

		DateTime thirdDateTime = DateTime.parse("2015-03-08");
		builder.startNewPeriod(thirdDateTime);
		builder.appendHouseTransit(House.VII, planetInTransit);

		DateTime fourthDateTime = DateTime.parse("2015-04-08");
		builder.startNewPeriod(fourthDateTime);
		builder.appendHouseTransit(House.VII, planetInTransit);

		// When
		TransitPeriods build = builder.build();
		Map<Planet, SortedSet<HouseTransitPeriod>> map = build.getHousePeriods();

		// Then
		assertThat(map, hasKey(Planet.NOEUD_NORD_MOYEN));
		SortedSet<HouseTransitPeriod> houseTransitPeriodList = map.get(Planet.NOEUD_NORD_MOYEN);

		assertThat(houseTransitPeriodList, notNullValue());
		assertThat(houseTransitPeriodList, hasSize(2));

		HouseTransitPeriod first = houseTransitPeriodList.first();
		assertThat(first, notNullValue());
		assertThat(first.getBeforeTransitionStartDate(), equalTo(null));
		assertThat(first.getStartDate(), equalTo(firstDateTime));
		assertThat(first.getEndDate(), equalTo(thirdDateTime));
		assertThat(first.getAfterTransitionEndDate(), equalTo(thirdDateTime));

		HouseTransitPeriod second = houseTransitPeriodList.last();
		assertThat(second, notNullValue());
		assertThat(second.getBeforeTransitionStartDate(), equalTo(thirdDateTime));
		assertThat(second.getStartDate(), equalTo(thirdDateTime));
		assertThat(second.getEndDate(), equalTo(fourthDateTime));
		assertThat(second.getAfterTransitionEndDate(), equalTo(null));
	}

	@Test
	public void testFullTransitPeriod() throws Exception {
		// Given
		SkyPosition natalPosition = themeService.getSkyPosition(TEST_NATAL_DATE, LATITUDE, LONGITUDE);

		DateTime startDate = DateTime.parse("2012-01-01");
		DateTime endDate = DateTime.parse("2018-01-01");

		// When
		TransitPeriods build = transitPeriodService
				.createTransitPeriod(natalPosition, startDate, endDate, LATITUDE, LONGITUDE);
		Map<Planet, SortedSet<HouseTransitPeriod>> map = build.getHousePeriods();

		// Then
		assertThat(map, hasKey(Planet.PLUTON));
		SortedSet<HouseTransitPeriod> houseTransitPeriods = map.get(Planet.PLUTON);

		assertThat(houseTransitPeriods, notNullValue());
		assertThat(houseTransitPeriods, not(emptyIterableOf(HouseTransitPeriod.class)));

		HouseTransitPeriod first = houseTransitPeriods.first();
		assertThat(first, notNullValue());
		assertThat(first.getNatalHouse(), is(House.X));
		assertThat(first.getBeforeTransitionStartDate(), equalTo(null));
		assertThat(first.getStartDate(), equalTo(DateTime.parse("2012-01-01")));
		assertThat(first.getEndDate(), equalTo(DateTime.parse("2014-01-19")));
		assertThat(first.getAfterTransitionEndDate(), equalTo(DateTime.parse("2014-11-23")));

		HouseTransitPeriod last = houseTransitPeriods.last();
		assertThat(last, notNullValue());
		assertThat(last.getNatalHouse(), is(House.XI));
		assertThat(last.getBeforeTransitionStartDate(), equalTo(DateTime.parse("2014-01-19")));
		assertThat(last.getStartDate(), equalTo(DateTime.parse("2014-11-23")));
		assertThat(last.getEndDate(), equalTo(endDate));
		assertThat(last.getAfterTransitionEndDate(), equalTo(null));

		assertThat(map, not(hasKey(Planet.SOLEIL)));
		assertThat(map, not(hasKey(Planet.LUNE)));
		assertThat(map, not(hasKey(Planet.MERCURE)));
		assertThat(map, not(hasKey(Planet.VENUS)));

		assertThat(map, hasKey(Planet.NOEUD_NORD_MOYEN));
		assertThat(map.get(Planet.NOEUD_NORD_MOYEN), hasSize(5));

		assertThat(map, hasKey(Planet.MARS));
		assertThat(map.get(Planet.MARS), hasSize(40));

		assertThat(map, hasKey(Planet.JUPITER));
		assertThat(map.get(Planet.JUPITER), hasSize(8));
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
}
