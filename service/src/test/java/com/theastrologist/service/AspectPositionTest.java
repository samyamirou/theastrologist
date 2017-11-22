package com.theastrologist.service;

import com.theastrologist.ServiceTestConfiguration;
import com.theastrologist.domain.Degree;
import com.theastrologist.domain.Planet;
import com.theastrologist.domain.PlanetPosition;
import com.theastrologist.domain.SkyPosition;
import com.theastrologist.domain.aspect.Aspect;
import com.theastrologist.domain.aspect.AspectPosition;
import org.hamcrest.Matchers;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;
import java.util.SortedMap;
import java.util.TimeZone;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {ServiceTestConfiguration.class})
public class AspectPositionTest {

    private Degree asDegree = new Degree(341, 46);
    private PlanetPosition asPosition = PlanetPosition.createPlanetPosition(asDegree, asDegree);

    public static final DateTimeZone DATE_TIME_ZONE = DateTimeZone.forTimeZone(TimeZone.getTimeZone("Europe/Paris"));
    private final DateTime TEST_DATE = new DateTime(1985, 1, 4, 11, 20, DATE_TIME_ZONE);
    private final DateTime TEST_DATE_COMP = new DateTime(1953, 8, 25, 21, 45, DATE_TIME_ZONE);
    private final DateTime TEST_DATE_TRANSIT = new DateTime(2015, 6, 23, 20, 30, DATE_TIME_ZONE);
    private final DateTime TEST_DATE_SYNASTRY = new DateTime(1984, 1, 15, 16, 00, DATE_TIME_ZONE);
    private final Degree LATITUDE = new Degree(48, 39);
    private final Degree LONGITUDE = new Degree(2, 25);

    @Autowired
    private ThemeService themeService;

    @Autowired
    private AspectService aspectService;

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testSextileSunAsc() throws Exception {
        Degree sunDegree = new Degree(284, 0);
        PlanetPosition sunPosition = PlanetPosition.createPlanetPosition(sunDegree, asDegree);
        AspectPosition aspectPosition = AspectPosition.createAspectPosition(Planet.SOLEIL, Planet.ASCENDANT, sunPosition, asPosition);

        assertThat(aspectPosition, notNullValue());
        assertThat(aspectPosition.getAspect(), Matchers.is(Aspect.SEXTILE));
        assertThat(aspectPosition.getOrbDelta().getDegree(), equalTo(-2));
        assertThat(aspectPosition.getOrbDelta().getMinutes(), equalTo(-13));
    }

    @Test
    public void testConjAscMars() throws Exception {
        Degree marsDegree = new Degree(337, 45);
        PlanetPosition marsPosition = PlanetPosition.createPlanetPosition(marsDegree, asDegree);
        AspectPosition aspectPosition = AspectPosition.createAspectPosition(Planet.ASCENDANT, Planet.MARS, asPosition, marsPosition);

        assertThat(aspectPosition, notNullValue());
        assertThat(aspectPosition.getAspect(), is(Aspect.CONJONCTION));
        assertThat(aspectPosition.getOrbDelta().getDegree(), equalTo(-4));
        assertThat(aspectPosition.getOrbDelta().getMinutes(), equalTo(0));
    }

    @Test
    public void testConjMarsAsc() throws Exception {
        Degree marsDegree = new Degree(337, 45);
        PlanetPosition marsPosition = PlanetPosition.createPlanetPosition(marsDegree, asDegree);
        AspectPosition aspectPosition = AspectPosition.createAspectPosition(Planet.MARS, Planet.ASCENDANT, marsPosition, asPosition);

        assertThat(aspectPosition, notNullValue());
        assertThat(aspectPosition.getAspect(), is(Aspect.CONJONCTION));
        assertThat(aspectPosition.getOrbDelta().getDegree(), equalTo(4));
        assertThat(aspectPosition.getOrbDelta().getMinutes(), equalTo(1));
    }

    @Test
    public void testCarreLuneAsc() throws Exception {
        Degree luneDegree = new Degree(71, 41);
        PlanetPosition lunePosition = PlanetPosition.createPlanetPosition(luneDegree, asDegree);
        AspectPosition aspectPosition = AspectPosition.createAspectPosition(Planet.LUNE, Planet.ASCENDANT, lunePosition, asPosition);

        assertThat(aspectPosition, notNullValue());
        assertThat(aspectPosition.getAspect(), is(Aspect.CARRE));
        assertThat(aspectPosition.getOrbDelta().getDegree(), equalTo(0));
        assertThat(aspectPosition.getOrbDelta().getMinutes(), equalTo(-4));
    }

    @Test
    public void testCarreVenusSaturne() throws Exception {
        Degree venusDegree = new Degree(330, 10);
        Degree saturneDegree = new Degree(235, 3);
        PlanetPosition venusPosition = PlanetPosition.createPlanetPosition(venusDegree, asDegree);
        PlanetPosition saturnePosition = PlanetPosition.createPlanetPosition(saturneDegree, asDegree);
        AspectPosition aspectPosition = AspectPosition.createAspectPosition(Planet.VENUS, Planet.SATURNE, venusPosition, saturnePosition);

        assertThat(aspectPosition, notNullValue());
        assertThat(aspectPosition.getAspect(), is(Aspect.CARRE));
        assertThat(aspectPosition.getOrbDelta().getDegree(), equalTo(-5));
        assertThat(aspectPosition.getOrbDelta().getMinutes(), equalTo(-6));
    }

    @Test
    public void testFullAspectMap() throws Exception {
        SkyPosition testSkyPosition = themeService.getSkyPosition(TEST_DATE, LATITUDE, LONGITUDE);
        SortedMap<Planet, SortedMap<Planet, AspectPosition>> aspectsForSkyPosition = aspectService.createAspectsForSkyPosition(testSkyPosition);

        assertThat(aspectsForSkyPosition, notNullValue());

        Map<Planet, AspectPosition> planetAspectPositionMap = aspectsForSkyPosition.get(Planet.MILIEU_DU_CIEL);

        assertThat(planetAspectPositionMap, notNullValue());

        assertThat(planetAspectPositionMap, hasKey(Planet.MERCURE));

        AspectPosition aspectPosition = planetAspectPositionMap.get(Planet.MERCURE);
        assertThat(aspectPosition, notNullValue());
        assertThat(aspectPosition.getAspect(), is(Aspect.CONJONCTION));
        assertThat(aspectPosition.getPlanet(), is(Planet.MILIEU_DU_CIEL));
        assertThat(aspectPosition.getPlanetComparison(), is(Planet.MERCURE));
        assertThat(aspectPosition.getOrbDelta().getDegree(), equalTo(0));
        assertThat(aspectPosition.getOrbDelta().getMinutes(), equalTo(-50));
    }

    @Test
    public void testAspectMapUnsuccessful() throws Exception {
        SkyPosition testSkyPosition = themeService.getSkyPosition(TEST_DATE, LATITUDE, LONGITUDE);
        SortedMap<Planet, SortedMap<Planet, AspectPosition>> aspectsForSkyPosition = aspectService.createAspectsForSkyPosition(testSkyPosition);

        assertThat(aspectsForSkyPosition, notNullValue());

        SortedMap<Planet, AspectPosition> planetAspectPositionMap = aspectsForSkyPosition.get(Planet.MILIEU_DU_CIEL);

        assertThat(planetAspectPositionMap, notNullValue());

        assertThat(planetAspectPositionMap, not(hasKey(Planet.SOLEIL)));
        assertThat(planetAspectPositionMap, not(hasKey(Planet.PART_DE_FORTUNE)));
    }

    @Test
    public void testSynastryConjAscLune() throws Exception {
        SkyPosition testSkyPosition = themeService.getSkyPosition(TEST_DATE, LATITUDE, LONGITUDE);
        SkyPosition testSkyPositionComparison = themeService.getSkyPosition(TEST_DATE_COMP, LATITUDE, LONGITUDE);
        SortedMap<Planet, SortedMap<Planet, AspectPosition>> synastry =
                aspectService.createAspectsForSynastry(testSkyPosition, testSkyPositionComparison);

        assertThat(synastry, notNullValue());

        Map<Planet, AspectPosition> planetAspectPositionMap = synastry.get(Planet.ASCENDANT);

        assertThat(planetAspectPositionMap, notNullValue());

        assertThat(planetAspectPositionMap, hasKey(Planet.LUNE));

        AspectPosition aspectPosition = planetAspectPositionMap.get(Planet.LUNE);
        assertThat(aspectPosition, notNullValue());
        assertThat(aspectPosition.getAspect(), is(Aspect.CONJONCTION));
        assertThat(aspectPosition.getPlanet(), is(Planet.ASCENDANT));
        assertThat(aspectPosition.getPlanetComparison(), is(Planet.LUNE));
        assertThat(aspectPosition.getOrbDelta().getDegree(), equalTo(4));
        assertThat(aspectPosition.getOrbDelta().getMinutes(), equalTo(57));
    }

    @Test
    public void testSynastryConjLuneLune() throws Exception {
        SkyPosition testSkyPosition = themeService.getSkyPosition(TEST_DATE, LATITUDE, LONGITUDE);
        SkyPosition testSkyPositionComparison = themeService.getSkyPosition(TEST_DATE_SYNASTRY, LATITUDE, LONGITUDE);
        SortedMap<Planet, SortedMap<Planet, AspectPosition>> synastry =
                aspectService.createAspectsForSynastry(testSkyPosition, testSkyPositionComparison);

        assertThat(synastry, notNullValue());

        Map<Planet, AspectPosition> planetAspectPositionMap = synastry.get(Planet.LUNE);

        assertThat(planetAspectPositionMap, notNullValue());

        assertThat(planetAspectPositionMap, hasKey(Planet.LUNE));

        AspectPosition aspectPosition = planetAspectPositionMap.get(Planet.LUNE);
        assertThat(aspectPosition, notNullValue());
        assertThat(aspectPosition.getAspect(), is(Aspect.CONJONCTION));
        assertThat(aspectPosition.getPlanet(), is(Planet.LUNE));
        assertThat(aspectPosition.getPlanetComparison(), is(Planet.LUNE));
        assertThat(aspectPosition.getOrbDelta().getDegree(), equalTo(3));
        assertThat(aspectPosition.getOrbDelta().getMinutes(), equalTo(11));
    }

    @Test
    public void testTransitConjNormale() throws Exception {
        SkyPosition testSkyPosition = themeService.getSkyPosition(TEST_DATE, LATITUDE, LONGITUDE);
        SkyPosition testSkyPositionComparison = themeService.getSkyPosition(TEST_DATE_TRANSIT, LATITUDE, LONGITUDE);
        SortedMap<Planet, SortedMap<Planet, AspectPosition>> transit =
                aspectService.createAspectsForTransit(testSkyPosition, testSkyPositionComparison);

        assertThat(transit, notNullValue());

        SortedMap<Planet, AspectPosition> planetAspectPositionMap = transit.get(Planet.ASCENDANT);

        assertThat(planetAspectPositionMap, notNullValue());

        assertThat(planetAspectPositionMap, hasKey(Planet.NEPTUNE));

        AspectPosition aspectPosition = planetAspectPositionMap.get(Planet.NEPTUNE);
        assertThat(aspectPosition, notNullValue());
        assertThat(aspectPosition.getAspect(), is(Aspect.CONJONCTION));
        assertThat(aspectPosition.getPlanet(), is(Planet.ASCENDANT));
        assertThat(aspectPosition.getPlanetComparison(), is(Planet.NEPTUNE));
        assertThat(aspectPosition.getOrbDelta().getDegree(), equalTo(-1));
        assertThat(aspectPosition.getOrbDelta().getMinutes(), equalTo(-59));
    }

    @Test
    public void testTransitPasConjDansOrbe() throws Exception {
        SkyPosition testSkyPosition = themeService.getSkyPosition(TEST_DATE, LATITUDE, LONGITUDE);
        SkyPosition testSkyPositionComparison = themeService.getSkyPosition(TEST_DATE_TRANSIT, LATITUDE, LONGITUDE);
        SortedMap<Planet, SortedMap<Planet, AspectPosition>> transit =
                aspectService.createAspectsForTransit(testSkyPosition, testSkyPositionComparison);

        assertThat(transit, notNullValue());

        Map<Planet, AspectPosition> planetAspectPositionMap = transit.get(Planet.LILITH_MOYENNE);

        assertThat(planetAspectPositionMap, notNullValue());
        assertThat(planetAspectPositionMap, not(hasKey(Planet.URANUS)));
    }

    @Test
    public void testTransitConjSurMemePlanete() throws Exception {
        SkyPosition testSkyPosition = themeService.getSkyPosition(TEST_DATE, LATITUDE, LONGITUDE);
        SkyPosition testSkyPositionComparison = themeService.getSkyPosition(TEST_DATE_TRANSIT, LATITUDE, LONGITUDE);
        SortedMap<Planet, SortedMap<Planet, AspectPosition>> transit =
                aspectService.createAspectsForTransit(testSkyPosition, testSkyPositionComparison);

        assertThat(transit, notNullValue());

        SortedMap<Planet, AspectPosition> planetAspectPositionMap = transit.get(Planet.SATURNE);

        assertThat(planetAspectPositionMap, notNullValue());

        assertThat(planetAspectPositionMap, hasKey(Planet.SATURNE));

        AspectPosition aspectPosition = planetAspectPositionMap.get(Planet.SATURNE);
        assertThat(aspectPosition, notNullValue());
        assertThat(aspectPosition.getAspect(), is(Aspect.CONJONCTION));
        assertThat(aspectPosition.getPlanet(), is(Planet.SATURNE));
        assertThat(aspectPosition.getPlanetComparison(), is(Planet.SATURNE));
        assertThat(aspectPosition.getOrbDelta().getDegree(), equalTo(4));
        assertThat(aspectPosition.getOrbDelta().getMinutes(), equalTo(24));
    }
}