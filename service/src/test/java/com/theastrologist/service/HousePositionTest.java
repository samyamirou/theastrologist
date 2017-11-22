package com.theastrologist.service;

import com.theastrologist.ServiceTestConfiguration;
import com.theastrologist.domain.*;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.TimeZone;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {ServiceTestConfiguration.class})
public class HousePositionTest {

    public static final DateTimeZone DATE_TIME_ZONE = DateTimeZone.forTimeZone(TimeZone.getTimeZone("Europe/Paris"));
    private final DateTime TEST_DATE = new DateTime(1985, 1, 4, 11, 20, DATE_TIME_ZONE);
    private final Degree LATITUDE = new Degree(48, 39);
    private final Degree LONGITUDE = new Degree(2, 25);
    private SkyPosition testSkyPosition;
    //private final double LATITUDE = 48.6456630;
    //private final double LONGITUDE = 2.4104510;

    @Autowired
    private ThemeService themeService;

    @Before
    public void setup() {
        testSkyPosition = themeService.getSkyPosition(TEST_DATE, LATITUDE, LONGITUDE);
    }

    @Test
    public void testGetAsPosition() throws Exception {
        PlanetPosition asPosition = testSkyPosition.getPlanetPosition(Planet.ASCENDANT);
        assertThat(asPosition, notNullValue());

        Degree degree = asPosition.getDegree();
        Degree degreeInSign = asPosition.getDegreeInSign();
        Degree degreeInHouse = asPosition.getDegreeInHouse();


        assertThat(degree.getDegree(), equalTo(341));
        assertThat(degree.getMinutes(), equalTo(46));

        assertThat(asPosition.getSign(), equalTo(Sign.POISSONS));
        assertThat(degreeInSign.getDegree(), equalTo(11));
        assertThat(degreeInSign.getMinutes(), equalTo(46));

        assertThat(asPosition.getHouse(), equalTo(House.I));
        assertThat(degreeInHouse.getDegree(), equalTo(0));
        assertThat(degreeInHouse.getMinutes(), equalTo(0));
    }

    @Test
    public void testGetAsPositionDecan() throws Exception {
        PlanetPosition asPosition = testSkyPosition.getPlanetPosition(Planet.ASCENDANT);
        assertThat(asPosition, notNullValue());

        SignDecan decanInSign = asPosition.getDecanInSign();
        HouseDecan decanInHouse = asPosition.getDecanInHouse();

        assertThat(asPosition.getSign(), equalTo(Sign.POISSONS));
        assertThat(decanInSign.getDecanNumber(), equalTo(2));
        assertThat(decanInSign.getRelatedSign(), equalTo(Sign.CANCER));

        assertThat(asPosition.getHouse(), equalTo(House.I));
        assertThat(decanInHouse.getDecanNumber(), equalTo(1));
        assertThat(decanInHouse.getRelatedHouse(), equalTo(House.I));
    }

    @Test
    public void testGetMcPosition() throws Exception {
        PlanetPosition asPosition = testSkyPosition.getPlanetPosition(Planet.MILIEU_DU_CIEL);
        assertThat(asPosition, notNullValue());

        Degree degree = asPosition.getDegree();
        Degree degreeInSign = asPosition.getDegreeInSign();
        Degree degreeInHouse = asPosition.getDegreeInHouse();

        assertThat(degree.getDegree(), equalTo(262));
        assertThat(degree.getMinutes(), equalTo(5));

        assertThat(asPosition.getSign(), equalTo(Sign.SAGITTAIRE));
        assertThat(degreeInSign.getDegree(), equalTo(22));
        assertThat(degreeInSign.getMinutes(), equalTo(5));

        assertThat(asPosition.getHouse(), equalTo(House.X));
        assertThat(degreeInHouse.getDegree(), equalTo(10));
        assertThat(degreeInHouse.getMinutes(), equalTo(18));
    }

    @Test
    public void testGetMcPositionDecan() throws Exception {
        PlanetPosition asPosition = testSkyPosition.getPlanetPosition(Planet.MILIEU_DU_CIEL);
        assertThat(asPosition, notNullValue());

        SignDecan decanInSign = asPosition.getDecanInSign();
        HouseDecan decanInHouse = asPosition.getDecanInHouse();

        assertThat(asPosition.getSign(), equalTo(Sign.SAGITTAIRE));
        assertThat(decanInSign.getDecanNumber(), equalTo(3));
        assertThat(decanInSign.getRelatedSign(), equalTo(Sign.LION));

        assertThat(asPosition.getHouse(), equalTo(House.X));
        assertThat(decanInHouse.getDecanNumber(), equalTo(2));
        assertThat(decanInHouse.getRelatedHouse(), equalTo(House.II));
    }

    @Test
    public void testGetHousePositions() throws Exception {
        HousePosition firstHousePosition = testSkyPosition.getHousePosition(House.I);
        assertThat(firstHousePosition, notNullValue());
        assertThat(firstHousePosition.getStartSign(), equalTo(Sign.POISSONS));
        assertThat(firstHousePosition.getStartCuspInSign().getDegree(), equalTo(11));
        assertThat(firstHousePosition.getEndSign(), equalTo(Sign.BELIER));
        assertThat(firstHousePosition.getEndCuspInSign().getDegree(), equalTo(11));

        HousePosition elevenHousePosition = testSkyPosition.getHousePosition(House.XI);
        assertThat(elevenHousePosition, notNullValue());
        assertThat(elevenHousePosition.getStartSign(), equalTo(Sign.CAPRICORNE));
        assertThat(elevenHousePosition.getStartCuspInSign().getDegree(), equalTo(11));
        assertThat(elevenHousePosition.getEndSign(), equalTo(Sign.VERSEAU));
        assertThat(elevenHousePosition.getEndCuspInSign().getDegree(), equalTo(11));

        HousePosition twelveHousePosition = testSkyPosition.getHousePosition(House.XII);
        assertThat(twelveHousePosition, notNullValue());
        assertThat(twelveHousePosition.getStartSign(), equalTo(Sign.VERSEAU));
        assertThat(twelveHousePosition.getStartCuspInSign().getDegree(), equalTo(11));
        assertThat(twelveHousePosition.getEndSign(), equalTo(Sign.POISSONS));
        assertThat(twelveHousePosition.getEndCuspInSign().getDegree(), equalTo(11));
    }

    @Test
    public void testGetHousePositionsDecan() throws Exception {
        HousePosition firstHousePosition = testSkyPosition.getHousePosition(House.I);
        assertThat(firstHousePosition, notNullValue());

        assertThat(firstHousePosition.getStartSign(), equalTo(Sign.POISSONS));
        SignDecan startCuspDecan = firstHousePosition.getStartCuspDecan();
        assertThat(startCuspDecan.getRelatedSign(), equalTo(Sign.CANCER));
        assertThat(startCuspDecan.getDecanNumber(), equalTo(2));

        assertThat(firstHousePosition.getEndSign(), equalTo(Sign.BELIER));
        SignDecan endCuspDecan = firstHousePosition.getEndCuspDecan();
        assertThat(endCuspDecan.getRelatedSign(), equalTo(Sign.LION));
        assertThat(endCuspDecan.getDecanNumber(), equalTo(2));
    }
}