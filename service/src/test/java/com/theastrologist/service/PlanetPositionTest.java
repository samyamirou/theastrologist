package com.theastrologist.service;

import com.theastrologist.ServiceTestConfiguration;
import com.theastrologist.domain.*;
import com.theastrologist.util.CalcUtil;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {ServiceTestConfiguration.class})
public class PlanetPositionTest {

	private final DateTime TEST_DATE = new DateTime(1985, 1, 4, 11, 20, CalcUtil.DATE_TIME_ZONE);
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
	public void testGetSunPosition() throws Exception {
		PlanetPosition planetPosition = testSkyPosition.getPlanetPosition(Planet.SOLEIL);
		assertThat(planetPosition, notNullValue());

		Degree degree = planetPosition.getDegree();
		Degree degreeInSign = planetPosition.getDegreeInSign();
		Degree degreeInHouse = planetPosition.getDegreeInHouse();


		assertThat(degree.getDegree(), equalTo(284));
		assertThat(degree.getMinutes(), equalTo(0));

		assertThat(planetPosition.getSign(), equalTo(Sign.CAPRICORNE));
		assertThat(degreeInSign.getDegree(), equalTo(14));
		assertThat(degreeInSign.getMinutes(), equalTo(0));


		assertThat(planetPosition.getHouse(), equalTo(House.XI));
		assertThat(degreeInHouse.getDegree(), equalTo(2));
		assertThat(degreeInHouse.getMinutes(), equalTo(13));

		SignDecan decanInSign = planetPosition.getDecanInSign();
		HouseDecan decanInHouse = planetPosition.getDecanInHouse();

		assertThat(decanInSign.getRelatedSign(), equalTo(Sign.TAUREAU));
		assertThat(decanInSign.getDecanNumber(), equalTo(2));

		assertThat(decanInHouse.getRelatedHouse(), equalTo(House.XI));
		assertThat(decanInHouse.getDecanNumber(), equalTo(1));
	}

	@Test
	public void testGetMoonPosition() throws Exception {
		PlanetPosition planetPosition = testSkyPosition.getPlanetPosition(Planet.LUNE);
		assertThat(planetPosition, notNullValue());

		Degree degree = planetPosition.getDegree();
		Degree degreeInSign = planetPosition.getDegreeInSign();
		Degree degreeInHouse = planetPosition.getDegreeInHouse();


		assertThat(degree.getDegree(), equalTo(71));
		assertThat(degree.getMinutes(), equalTo(41));

		assertThat(planetPosition.getSign(), equalTo(Sign.GEMEAUX));
		assertThat(degreeInSign.getDegree(), equalTo(11));
		assertThat(degreeInSign.getMinutes(), equalTo(41));

		assertThat(planetPosition.getHouse(), equalTo(House.III));
		assertThat(degreeInHouse.getDegree(), equalTo(29));
		assertThat(degreeInHouse.getMinutes(), equalTo(54));

		SignDecan decanInSign = planetPosition.getDecanInSign();
		HouseDecan decanInHouse = planetPosition.getDecanInHouse();

		assertThat(decanInSign.getDecanNumber(), equalTo(2));
		assertThat(decanInSign.getRelatedSign(), equalTo(Sign.BALANCE));

		assertThat(decanInHouse.getDecanNumber(), equalTo(3));
		assertThat(decanInHouse.getRelatedHouse(), equalTo(House.XI));
	}

	@Test
	public void testGetMercuryPosition() throws Exception {
		PlanetPosition planetPosition = testSkyPosition.getPlanetPosition(Planet.MERCURE);
		assertThat(planetPosition, notNullValue());

		Degree degree = planetPosition.getDegree();
		Degree degreeInSign = planetPosition.getDegreeInSign();
		Degree degreeInHouse = planetPosition.getDegreeInHouse();


		assertThat(degree.getDegree(), equalTo(261));
		assertThat(degree.getMinutes(), equalTo(14));

		assertThat(planetPosition.getSign(), equalTo(Sign.SAGITTAIRE));
		assertThat(degreeInSign.getDegree(), equalTo(21));
		assertThat(degreeInSign.getMinutes(), equalTo(14));

		assertThat(planetPosition.getHouse(), equalTo(House.X));
		assertThat(degreeInHouse.getDegree(), equalTo(9));
		assertThat(degreeInHouse.getMinutes(), equalTo(27));

		SignDecan decanInSign = planetPosition.getDecanInSign();
		HouseDecan decanInHouse = planetPosition.getDecanInHouse();

		assertThat(decanInSign.getDecanNumber(), equalTo(3));
		assertThat(decanInSign.getRelatedSign(), equalTo(Sign.LION));

		assertThat(decanInHouse.getDecanNumber(), equalTo(1));
		assertThat(decanInHouse.getRelatedHouse(), equalTo(House.X));
	}

	@Test
	public void testGetVenusPosition() throws Exception {
		PlanetPosition planetPosition = testSkyPosition.getPlanetPosition(Planet.VENUS);
		assertThat(planetPosition, notNullValue());

		Degree degree = planetPosition.getDegree();
		Degree degreeInSign = planetPosition.getDegreeInSign();
		Degree degreeInHouse = planetPosition.getDegreeInHouse();


		assertThat(degree.getDegree(), equalTo(330));
		assertThat(degree.getMinutes(), equalTo(10));

		assertThat(planetPosition.getSign(), equalTo(Sign.POISSONS));
		assertThat(degreeInSign.getDegree(), equalTo(0));
		assertThat(degreeInSign.getMinutes(), equalTo(10));

		assertThat(planetPosition.getHouse(), equalTo(House.XII));
		assertThat(degreeInHouse.getDegree(), equalTo(18));
		assertThat(degreeInHouse.getMinutes(), equalTo(24));

		SignDecan decanInSign = planetPosition.getDecanInSign();
		HouseDecan decanInHouse = planetPosition.getDecanInHouse();

		assertThat(decanInSign.getDecanNumber(), equalTo(1));
		assertThat(decanInSign.getRelatedSign(), equalTo(Sign.POISSONS));

		assertThat(decanInHouse.getDecanNumber(), equalTo(2));
		assertThat(decanInHouse.getRelatedHouse(), equalTo(House.IV));
	}

	@Test
	public void testGetMarsPosition() throws Exception {
		PlanetPosition planetPosition = testSkyPosition.getPlanetPosition(Planet.MARS);
		assertThat(planetPosition, notNullValue());

		Degree degree = planetPosition.getDegree();
		Degree degreeInSign = planetPosition.getDegreeInSign();
		Degree degreeInHouse = planetPosition.getDegreeInHouse();


		assertThat(degree.getDegree(), equalTo(337));
		assertThat(degree.getMinutes(), equalTo(45));

		assertThat(planetPosition.getSign(), equalTo(Sign.POISSONS));
		assertThat(degreeInSign.getDegree(), equalTo(7));
		assertThat(degreeInSign.getMinutes(), equalTo(45));

		assertThat(planetPosition.getHouse(), equalTo(House.XII));
		assertThat(degreeInHouse.getDegree(), equalTo(25));
		assertThat(degreeInHouse.getMinutes(), equalTo(58));

		SignDecan decanInSign = planetPosition.getDecanInSign();
		HouseDecan decanInHouse = planetPosition.getDecanInHouse();

		assertThat(decanInSign.getDecanNumber(), equalTo(1));
		assertThat(decanInSign.getRelatedSign(), equalTo(Sign.POISSONS));

		assertThat(decanInHouse.getDecanNumber(), equalTo(3));
		assertThat(decanInHouse.getRelatedHouse(), equalTo(House.VIII));
	}

	@Test
	public void testGetJupiterPosition() throws Exception {
		PlanetPosition planetPosition = testSkyPosition.getPlanetPosition(Planet.JUPITER);
		assertThat(planetPosition, notNullValue());

		Degree degree = planetPosition.getDegree();
		Degree degreeInSign = planetPosition.getDegreeInSign();
		Degree degreeInHouse = planetPosition.getDegreeInHouse();


		assertThat(degree.getDegree(), equalTo(292));
		assertThat(degree.getMinutes(), equalTo(15));

		assertThat(planetPosition.getSign(), equalTo(Sign.CAPRICORNE));
		assertThat(degreeInSign.getDegree(), equalTo(22));
		assertThat(degreeInSign.getMinutes(), equalTo(15));

		assertThat(planetPosition.getHouse(), equalTo(House.XI));
		assertThat(degreeInHouse.getDegree(), equalTo(10));
		assertThat(degreeInHouse.getMinutes(), equalTo(28));

		SignDecan decanInSign = planetPosition.getDecanInSign();
		HouseDecan decanInHouse = planetPosition.getDecanInHouse();

		assertThat(decanInSign.getDecanNumber(), equalTo(3));
		assertThat(decanInSign.getRelatedSign(), equalTo(Sign.VIERGE));

		assertThat(decanInHouse.getDecanNumber(), equalTo(2));
		assertThat(decanInHouse.getRelatedHouse(), equalTo(House.III));
	}

	@Test
	public void testGetSaturnePosition() throws Exception {
		PlanetPosition planetPosition = testSkyPosition.getPlanetPosition(Planet.SATURNE);
		assertThat(planetPosition, notNullValue());

		Degree degree = planetPosition.getDegree();
		Degree degreeInSign = planetPosition.getDegreeInSign();
		Degree degreeInHouse = planetPosition.getDegreeInHouse();


		assertThat(degree.getDegree(), equalTo(235));
		assertThat(degree.getMinutes(), equalTo(3));

		assertThat(planetPosition.getSign(), equalTo(Sign.SCORPION));
		assertThat(degreeInSign.getDegree(), equalTo(25));
		assertThat(degreeInSign.getMinutes(), equalTo(3));

		assertThat(planetPosition.getHouse(), equalTo(House.IX));
		assertThat(degreeInHouse.getDegree(), equalTo(13));
		assertThat(degreeInHouse.getMinutes(), equalTo(16));

		SignDecan decanInSign = planetPosition.getDecanInSign();
		HouseDecan decanInHouse = planetPosition.getDecanInHouse();

		assertThat(decanInSign.getDecanNumber(), equalTo(3));
		assertThat(decanInSign.getRelatedSign(), equalTo(Sign.CANCER));

		assertThat(decanInHouse.getDecanNumber(), equalTo(2));
		assertThat(decanInHouse.getRelatedHouse(), equalTo(House.I));
	}

	@Test
	public void testGetUranusPosition() throws Exception {
		PlanetPosition planetPosition = testSkyPosition.getPlanetPosition(Planet.URANUS);
		assertThat(planetPosition, notNullValue());

		Degree degree = planetPosition.getDegree();
		Degree degreeInSign = planetPosition.getDegreeInSign();
		Degree degreeInHouse = planetPosition.getDegreeInHouse();


		assertThat(degree.getDegree(), equalTo(255));
		assertThat(degree.getMinutes(), equalTo(33));

		assertThat(planetPosition.getSign(), equalTo(Sign.SAGITTAIRE));
		assertThat(degreeInSign.getDegree(), equalTo(15));
		assertThat(degreeInSign.getMinutes(), equalTo(33));

		assertThat(planetPosition.getHouse(), equalTo(House.X));
		assertThat(degreeInHouse.getDegree(), equalTo(3));
		assertThat(degreeInHouse.getMinutes(), equalTo(46));

		SignDecan decanInSign = planetPosition.getDecanInSign();
		HouseDecan decanInHouse = planetPosition.getDecanInHouse();

		assertThat(decanInSign.getDecanNumber(), equalTo(2));
		assertThat(decanInSign.getRelatedSign(), equalTo(Sign.BELIER));

		assertThat(decanInHouse.getDecanNumber(), equalTo(1));
		assertThat(decanInHouse.getRelatedHouse(), equalTo(House.X));
	}

	@Test
	public void testGetNeptunePosition() throws Exception {
		PlanetPosition planetPosition = testSkyPosition.getPlanetPosition(Planet.NEPTUNE);
		assertThat(planetPosition, notNullValue());

		Degree degree = planetPosition.getDegree();
		Degree degreeInSign = planetPosition.getDegreeInSign();
		Degree degreeInHouse = planetPosition.getDegreeInHouse();


		assertThat(degree.getDegree(), equalTo(271));
		assertThat(degree.getMinutes(), equalTo(36));

		assertThat(planetPosition.getSign(), equalTo(Sign.CAPRICORNE));
		assertThat(degreeInSign.getDegree(), equalTo(1));
		assertThat(degreeInSign.getMinutes(), equalTo(36));

		assertThat(planetPosition.getHouse(), equalTo(House.X));
		assertThat(degreeInHouse.getDegree(), equalTo(19));
		assertThat(degreeInHouse.getMinutes(), equalTo(49));

		SignDecan decanInSign = planetPosition.getDecanInSign();
		HouseDecan decanInHouse = planetPosition.getDecanInHouse();

		assertThat(decanInSign.getDecanNumber(), equalTo(1));
		assertThat(decanInSign.getRelatedSign(), equalTo(Sign.CAPRICORNE));

		assertThat(decanInHouse.getDecanNumber(), equalTo(2));
		assertThat(decanInHouse.getRelatedHouse(), equalTo(House.II));
	}

	@Test
	public void testGetPlutonPosition() throws Exception {
		PlanetPosition planetPosition = testSkyPosition.getPlanetPosition(Planet.PLUTON);
		assertThat(planetPosition, notNullValue());

		Degree degree = planetPosition.getDegree();
		Degree degreeInSign = planetPosition.getDegreeInSign();
		Degree degreeInHouse = planetPosition.getDegreeInHouse();


		assertThat(degree.getDegree(), equalTo(214));
		assertThat(degree.getMinutes(), equalTo(25));

		assertThat(planetPosition.getSign(), equalTo(Sign.SCORPION));
		assertThat(degreeInSign.getDegree(), equalTo(4));
		assertThat(degreeInSign.getMinutes(), equalTo(25));

		assertThat(planetPosition.getHouse(), equalTo(House.VIII));
		assertThat(degreeInHouse.getDegree(), equalTo(22));
		assertThat(degreeInHouse.getMinutes(), equalTo(38));

		SignDecan decanInSign = planetPosition.getDecanInSign();
		HouseDecan decanInHouse = planetPosition.getDecanInHouse();

		assertThat(decanInSign.getDecanNumber(), equalTo(1));
		assertThat(decanInSign.getRelatedSign(), equalTo(Sign.SCORPION));

		assertThat(decanInHouse.getDecanNumber(), equalTo(3));
		assertThat(decanInHouse.getRelatedHouse(), equalTo(House.IV));
	}

	@Test
	public void testGetNoeudNordPosition() throws Exception {
		PlanetPosition planetPosition = testSkyPosition.getPlanetPosition(Planet.NOEUD_NORD_MOYEN);
		assertThat(planetPosition, notNullValue());

		Degree degree = planetPosition.getDegree();
		Degree degreeInSign = planetPosition.getDegreeInSign();
		Degree degreeInHouse = planetPosition.getDegreeInHouse();


		assertThat(degree.getDegree(), equalTo(54));
		assertThat(degree.getMinutes(), equalTo(57));

		assertThat(planetPosition.getSign(), equalTo(Sign.TAUREAU));
		assertThat(degreeInSign.getDegree(), equalTo(24));
		assertThat(degreeInSign.getMinutes(), equalTo(57));

		assertThat(planetPosition.getHouse(), equalTo(House.III));
		assertThat(degreeInHouse.getDegree(), equalTo(13));
		assertThat(degreeInHouse.getMinutes(), equalTo(11));

		SignDecan decanInSign = planetPosition.getDecanInSign();
		HouseDecan decanInHouse = planetPosition.getDecanInHouse();

		assertThat(decanInSign.getDecanNumber(), equalTo(3));
		assertThat(decanInSign.getRelatedSign(), equalTo(Sign.CAPRICORNE));

		assertThat(decanInHouse.getDecanNumber(), equalTo(2));
		assertThat(decanInHouse.getRelatedHouse(), equalTo(House.VII));
	}

	@Test
	public void testGetNoeudSudPosition() throws Exception {
		PlanetPosition planetPosition = testSkyPosition.getPlanetPosition(Planet.NOEUD_SUD_MOYEN);
		assertThat(planetPosition, notNullValue());

		Degree degree = planetPosition.getDegree();
		Degree degreeInSign = planetPosition.getDegreeInSign();
		Degree degreeInHouse = planetPosition.getDegreeInHouse();


		assertThat(degree.getDegree(), equalTo(234));
		assertThat(degree.getMinutes(), equalTo(57));

		assertThat(planetPosition.getSign(), equalTo(Sign.SCORPION));
		assertThat(degreeInSign.getDegree(), equalTo(24));
		assertThat(degreeInSign.getMinutes(), equalTo(57));

		assertThat(planetPosition.getHouse(), equalTo(House.IX));
		assertThat(degreeInHouse.getDegree(), equalTo(13));
		assertThat(degreeInHouse.getMinutes(), equalTo(11));

		SignDecan decanInSign = planetPosition.getDecanInSign();
		HouseDecan decanInHouse = planetPosition.getDecanInHouse();

		assertThat(decanInSign.getDecanNumber(), equalTo(3));
		assertThat(decanInSign.getRelatedSign(), equalTo(Sign.CANCER));

		assertThat(decanInHouse.getDecanNumber(), equalTo(2));
		assertThat(decanInHouse.getRelatedHouse(), equalTo(House.I));
	}

	@Test
	public void testGetLilithPosition() throws Exception {
		PlanetPosition planetPosition = testSkyPosition.getPlanetPosition(Planet.LILITH_MOYENNE);
		assertThat(planetPosition, notNullValue());

		Degree degree = planetPosition.getDegree();
		Degree degreeInSign = planetPosition.getDegreeInSign();
		Degree degreeInHouse = planetPosition.getDegreeInHouse();


		assertThat(degree.getDegree(), equalTo(13));
		assertThat(degree.getMinutes(), equalTo(31));

		assertThat(planetPosition.getSign(), equalTo(Sign.BELIER));
		assertThat(degreeInSign.getDegree(), equalTo(13));
		assertThat(degreeInSign.getMinutes(), equalTo(31));

		assertThat(planetPosition.getHouse(), equalTo(House.II));
		assertThat(degreeInHouse.getDegree(), equalTo(1));
		assertThat(degreeInHouse.getMinutes(), equalTo(44));

		SignDecan decanInSign = planetPosition.getDecanInSign();
		HouseDecan decanInHouse = planetPosition.getDecanInHouse();

		assertThat(decanInSign.getDecanNumber(), equalTo(2));
		assertThat(decanInSign.getRelatedSign(), equalTo(Sign.LION));

		assertThat(decanInHouse.getDecanNumber(), equalTo(1));
		assertThat(decanInHouse.getRelatedHouse(), equalTo(House.II));
	}

	@Test
	public void testGetPartDeFortunePosition() throws Exception {
		PlanetPosition planetPosition = testSkyPosition.getPlanetPosition(Planet.PART_DE_FORTUNE);
		assertThat(planetPosition, notNullValue());

		Degree degree = planetPosition.getDegree();
		Degree degreeInSign = planetPosition.getDegreeInSign();
		Degree degreeInHouse = planetPosition.getDegreeInHouse();


		assertThat(degree.getDegree(), equalTo(129));
		assertThat(degree.getMinutes(), equalTo(27));

		assertThat(planetPosition.getSign(), equalTo(Sign.LION));
		assertThat(degreeInSign.getDegree(), equalTo(9));
		assertThat(degreeInSign.getMinutes(), equalTo(27));

		assertThat(planetPosition.getHouse(), equalTo(House.V));
		assertThat(degreeInHouse.getDegree(), equalTo(27));
		assertThat(degreeInHouse.getMinutes(), equalTo(40));

		SignDecan decanInSign = planetPosition.getDecanInSign();
		HouseDecan decanInHouse = planetPosition.getDecanInHouse();

		assertThat(decanInSign.getDecanNumber(), equalTo(1));
		assertThat(decanInSign.getRelatedSign(), equalTo(Sign.LION));

		assertThat(decanInHouse.getDecanNumber(), equalTo(3));
		assertThat(decanInHouse.getRelatedHouse(), equalTo(House.I));
	}

	@Test
	public void testGetMasteredHouses() throws Exception {
		assertThat(testSkyPosition.getMasteredHouse(Planet.SOLEIL), equalTo(House.VI));
		assertThat(testSkyPosition.getSecondMasteredHouse(Planet.SOLEIL), equalTo(null));

		assertThat(testSkyPosition.getMasteredHouse(Planet.LUNE), equalTo(House.V));
		assertThat(testSkyPosition.getSecondMasteredHouse(Planet.LUNE), equalTo(null));

		assertThat(testSkyPosition.getMasteredHouse(Planet.MERCURE), equalTo(House.IV));
		assertThat(testSkyPosition.getSecondMasteredHouse(Planet.MERCURE), equalTo(House.VII));

		assertThat(testSkyPosition.getMasteredHouse(Planet.VENUS), equalTo(House.III));
		assertThat(testSkyPosition.getSecondMasteredHouse(Planet.VENUS), equalTo(House.VIII));

		assertThat(testSkyPosition.getMasteredHouse(Planet.MARS), equalTo(House.II));
		assertThat(testSkyPosition.getSecondMasteredHouse(Planet.MARS), equalTo(House.IX));

		assertThat(testSkyPosition.getMasteredHouse(Planet.JUPITER), equalTo(House.I));
		assertThat(testSkyPosition.getSecondMasteredHouse(Planet.JUPITER), equalTo(House.X));

		assertThat(testSkyPosition.getMasteredHouse(Planet.SATURNE), equalTo(House.XI));
		assertThat(testSkyPosition.getSecondMasteredHouse(Planet.SATURNE), equalTo(House.XII));

		assertThat(testSkyPosition.getMasteredHouse(Planet.URANUS), equalTo(House.XI));
		assertThat(testSkyPosition.getSecondMasteredHouse(Planet.URANUS), equalTo(House.XII));

		assertThat(testSkyPosition.getMasteredHouse(Planet.NEPTUNE), equalTo(House.I));
		assertThat(testSkyPosition.getSecondMasteredHouse(Planet.NEPTUNE), equalTo(House.X));

		assertThat(testSkyPosition.getMasteredHouse(Planet.PLUTON), equalTo(House.II));
		assertThat(testSkyPosition.getSecondMasteredHouse(Planet.PLUTON), equalTo(House.IX));

		assertThat(testSkyPosition.getMasteredHouse(Planet.ASCENDANT), equalTo(null));
		assertThat(testSkyPosition.getSecondMasteredHouse(Planet.ASCENDANT), equalTo(null));

		assertThat(testSkyPosition.getMasteredHouse(Planet.LILITH_MOYENNE), equalTo(null));
		assertThat(testSkyPosition.getSecondMasteredHouse(Planet.LILITH_MOYENNE), equalTo(null));
	}
}