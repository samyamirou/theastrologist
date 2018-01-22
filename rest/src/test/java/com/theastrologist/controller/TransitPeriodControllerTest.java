package com.theastrologist.controller;

import com.google.common.collect.Maps;
import com.google.common.primitives.Longs;

import com.theastrologist.domain.Degree;
import com.theastrologist.util.ControllerUtil;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.response.MockMvcResponse;
import org.apache.http.client.utils.URIBuilder;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.*;
import static io.restassured.module.mockmvc.matcher.RestAssuredMockMvcMatchers.*;
import static org.easymock.EasyMock.*;
import static org.easymock.EasyMock.replay;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;

/**
 * Created by Samy on 16/09/2015.
 */
public class TransitPeriodControllerTest {
	private ControllerUtil controllerUtil;
	private TransitPeriodController transitPeriodController;

	@Before
	public void setUp() throws Exception {
		transitPeriodController = new TransitPeriodController();
		controllerUtil = createMockBuilder(ControllerUtil.class).addMockedMethod("queryGoogleForTimezone").createMock();
		transitPeriodController.setControllerUtil(controllerUtil);

		RestAssuredMockMvc.standaloneSetup(transitPeriodController);
	}

	@Test
	public void testRequest() throws URISyntaxException {

		expect(controllerUtil.queryGoogleForTimezone(anyDouble(), anyDouble(), anyLong()))
				.andReturn(DateTimeZone.forID("Europe/Paris"));
		replay(controllerUtil);

		MockMvcResponse response = get("/{natalDate}/{latitude:.+}/{longitude:.+}/transitperiod/{startDate}/{endDate}",
									   "1985-01-04T11:20:00",
									   Double.toString(new Degree(48, 39).getBaseDegree()),
									   Double.toString(new Degree(2, 25).getBaseDegree()),
									   "2014-01-01T11:20:00",
									   "2016-01-01T11:20:00");

		response.then().statusCode(200)
				.body("planetPeriods.PLUTON", hasSize(4))
				.body("housePeriods.NOEUD_NORD_MOYEN", hasSize(2))
				.body("housePeriods.MARS", hasSize(14));

		verify(controllerUtil);
	}

	@Test
	public void testRequestCompleterPeriodes() throws URISyntaxException {

		expect(controllerUtil.queryGoogleForTimezone(anyDouble(), anyDouble(), anyLong()))
				.andReturn(DateTimeZone.forID("Europe/Paris"));
		replay(controllerUtil);

		MockMvcResponse response = get("/{natalDate}/{latitude:.+}/{longitude:.+}/transitperiod/{startDate}/{endDate}",
									   "1985-01-04T11:20:00",
									   Double.toString(new Degree(48, 39).getBaseDegree()),
									   Double.toString(new Degree(2, 25).getBaseDegree()),
									   "2014-01-01T11:20:00",
									   "2016-01-01T11:20:00");

		response.then().statusCode(200)
				.body("planetPeriods.PLUTON", hasSize(4))
				.body("housePeriods.NOEUD_NORD_MOYEN", hasSize(2))
				.body("housePeriods.MARS", hasSize(14));

		verify(controllerUtil);
	}

	@Test
	public void testRequestShortDates() throws URISyntaxException {

		expect(controllerUtil.queryGoogleForTimezone(anyDouble(), anyDouble(), anyLong()))
				.andReturn(DateTimeZone.forID("Europe/Paris"));
		replay(controllerUtil);

		MockMvcResponse response = get("/{natalDate}/{latitude:.+}/{longitude:.+}/transitperiod/{startDate}/{endDate}",
									   "1985-01-04T11:20:00",
									   Double.toString(new Degree(48, 39).getBaseDegree()),
									   Double.toString(new Degree(2, 25).getBaseDegree()),
									   "2014-01-01",
									   "2016-01-01");

		response.then().statusCode(200).body("planetPeriods.PLUTON", hasSize(4));

		verify(controllerUtil);
	}

	@Test
	public void testTransitPeriodRequestWithAddress() {

		MockMvcResponse response = get("/{natalDate}/{address}/transitperiod/{startDate}/{endDate}",
									   "1985-01-04T11:20:00", "Ris-Orangis", "2014-01-01", "2016-01-01");

		response.then().statusCode(200)
				.body("planetPeriods.PLUTON", hasSize(5))
				.body("housePeriods.NOEUD_NORD_MOYEN", hasSize(2))
				.body("housePeriods.MARS", hasSize(14));
	}
}
