package com.theastrologist.service;

import com.theastrologist.ServiceTestConfiguration;
import com.theastrologist.domain.Degree;
import com.theastrologist.domain.Sign;
import com.theastrologist.domain.SkyPosition;
import com.theastrologist.util.CalcUtil;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 * Created by Samy on 11/05/2017.
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {ServiceTestConfiguration.class})
public class SolarRevolutionServiceTest {
	private final DateTime TEST_NATAL_DATE = new DateTime(1985, 1, 4, 11, 20, CalcUtil.DATE_TIME_ZONE);
	private final DateTime TEST_REV_DATE = new DateTime(2016, 3, 12, 05, 36, CalcUtil.DATE_TIME_ZONE);
	private final Degree NATAL_LATITUDE = new Degree(48, 39);
	private final Degree NATAL_LONGITUDE = new Degree(2, 25);
	private final Degree PARIS_LATITUDE = new Degree(48.862725);
	private final Degree PARIS_LONGITUDE = new Degree(2.28759);
	private final Degree BAMAKO_LATITUDE = new Degree(12.617098);
	private final Degree BAMAKO_LONGITUDE = new Degree(-7.9810845);
	private SkyPosition testSkyPosition;
	//private final double NATAL_LATITUDE = 48.6456630;
	//private final double NATAL_LONGITUDE = 2.4104510;

	@Autowired
	private ThemeService themeService;

	@Autowired
	private RevolutionService revolutionService;

	@Before
	public void setup() {
		testSkyPosition = themeService.getSkyPosition(TEST_NATAL_DATE, NATAL_LATITUDE, NATAL_LONGITUDE);
	}

	@Test
	public void testSolarRevolutionUTSAM2016() throws Exception {
		DateTime solarRevolutionTime = revolutionService
				.getSolarRevolutionUT(testSkyPosition, TEST_REV_DATE.minusYears(1));

		assertThat(solarRevolutionTime, notNullValue());
		assertThat(solarRevolutionTime.toString(), equalTo("2016-01-04T22:53:55.113Z"));
	}

	@Test
	public void testSolarRevolutionSAM2016() throws Exception {
		SkyPosition solarRevolution = revolutionService
				.getSolarRevolution(testSkyPosition, TEST_REV_DATE.minusYears(1), PARIS_LATITUDE, PARIS_LONGITUDE);

		assertThat(solarRevolution, notNullValue());
		assertThat(solarRevolution.getAscendantPosition().getSign(), is(Sign.VIERGE));
	}

	@Test
	public void testSolarRevolutionUTSAM2017() throws Exception {
		DateTime solarRevolutionTime = revolutionService
				.getSolarRevolutionUT(testSkyPosition, TEST_REV_DATE);

		assertThat(solarRevolutionTime, notNullValue());
		assertThat(solarRevolutionTime.toString(), equalTo("2017-01-04T04:41:02.952Z"));
	}

	@Test
	public void testSolarRevolutionSAM2017() throws Exception {
		SkyPosition solarRevolution = revolutionService
				.getSolarRevolution(testSkyPosition, TEST_REV_DATE, PARIS_LATITUDE, PARIS_LONGITUDE);

		assertThat(solarRevolution, notNullValue());
		assertThat(solarRevolution.getAscendantPosition().getDegreeInSign().getDegree(), equalTo(2));
		assertThat(solarRevolution.getAscendantPosition().getSign(), is(Sign.SAGITTAIRE));
	}

	@Test
	public void testSolarRevolutionSAM2017Bamako() throws Exception {
		SkyPosition solarRevolution = revolutionService
				.getSolarRevolution(testSkyPosition, TEST_REV_DATE, BAMAKO_LATITUDE, BAMAKO_LONGITUDE);

		assertThat(solarRevolution, notNullValue());
		assertThat(solarRevolution.getAscendantPosition().getDegreeInSign().getDegree(), equalTo(12));
		assertThat(solarRevolution.getAscendantPosition().getSign(), is(Sign.SAGITTAIRE));
	}

	@Test
	public void testSolarRevolutionUTSAM2018() throws Exception {
		DateTime solarRevolutionTime = revolutionService
				.getSolarRevolutionUT(testSkyPosition, TEST_REV_DATE.plusYears(1));

		assertThat(solarRevolutionTime, notNullValue());
		assertThat(solarRevolutionTime.toString(), equalTo("2018-01-04T10:33:27.131Z"));
	}

	@Test
	public void testSolarRevolutionSAM2018() throws Exception {
		SkyPosition solarRevolution = revolutionService
				.getSolarRevolution(testSkyPosition, TEST_REV_DATE.plusYears(1), PARIS_LATITUDE, PARIS_LONGITUDE);

		assertThat(solarRevolution, notNullValue());
		assertThat(solarRevolution.getAscendantPosition().getDegreeInSign().getDegree(), equalTo(18));
		assertThat(solarRevolution.getAscendantPosition().getSign(), is(Sign.POISSONS));
	}

	@Test
	public void testSolarRevolutionSAM2018Bamako() throws Exception {
		SkyPosition solarRevolution = revolutionService
				.getSolarRevolution(testSkyPosition, TEST_REV_DATE.plusYears(1), BAMAKO_LATITUDE, BAMAKO_LONGITUDE);

		assertThat(solarRevolution, notNullValue());
		assertThat(solarRevolution.getAscendantPosition().getDegreeInSign().getDegree(), equalTo(11));
		assertThat(solarRevolution.getAscendantPosition().getSign(), is(Sign.POISSONS));
	}
}