package com.theastrologist.controller;

import com.theastrologist.util.TimeService;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.jayway.jsonpath.matchers.JsonPathMatchers.hasJsonPath;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

/**
 * Created by Samy on 15/05/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
//@ActiveProfiles("test")
//@WebMvcTest(RevolutionController.class)
public class RevolutionControllerTest {

	private final String TEST_NATAL_DATE = "1985-01-04T11:20:00";
	private final String TEST_REV_SOLAR_DATE = "2016-03-12";
	private final String TEST_REV_LUNAR_DATE = "2017-05-12";
	private final double NATAL_LATITUDE = 48.64566;
	private final double NATAL_LONGITUDE = 2.41045;
	private final double PARIS_LATITUDE = 48.862725;
	private final double PARIS_LONGITUDE = 2.28759;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private TimeService timeService;

	@BeforeClass
	public static void setUpClass() throws Exception {
		MockitoAnnotations.initMocks(RevolutionControllerTest.class);
	}

	@Before
	public void setUp() throws Exception {
		reset(timeService);
	}

	@Test
	public void testMockito() {
		Mockito.doReturn(DateTimeZone.forID("Europe/Paris")).when(timeService)
			   .queryGoogleForTimezone(anyDouble(), anyDouble(), anyLong());
		assertThat(timeService.queryGoogleForTimezone(0L, 0L, 0L), equalTo(DateTimeZone.forID("Europe/Paris")));
		assertThat(timeService.queryGoogleForTimezone(0L, 0L, 1L), equalTo(DateTimeZone.forID("Europe/Paris")));
		verify(timeService, times(2)).queryGoogleForTimezone(anyDouble(), anyDouble(), anyLong());
	}

	@Test
	public void testSimpleSolarRevolution() throws Exception {
		Mockito.doReturn(DateTimeZone.forID("Europe/Paris")).when(timeService)
			   .queryGoogleForTimezone(anyDouble(), anyDouble(), anyLong());

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("/{natal_date}/{natal_latitude}/{natal_longitude}/revolution/solar/{from_date}/{anniversary_latitude}/{anniversary_longitude}",
					 TEST_NATAL_DATE,
					 NATAL_LATITUDE,
					 NATAL_LONGITUDE,
					 TEST_REV_SOLAR_DATE,
					 PARIS_LATITUDE,
					 PARIS_LONGITUDE);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String body = result.getResponse().getContentAsString();
		MockHttpServletResponse response = result.getResponse();

		assertThat(response.getStatus(), equalTo(HttpStatus.OK.value()));
		assertThat(body, hasJsonPath("date", equalTo("2017-01-04T05:41:02+01:00")));
		assertThat(body, hasJsonPath("positions.SOLEIL.sign", equalTo("CAPRICORNE")));
		assertThat(body, hasJsonPath("positions.SOLEIL.degreeInSign.degree", equalTo(14)));
		assertThat(body, hasJsonPath("positions.SOLEIL.degreeInSign.minutes", equalTo(0)));
		assertThat(body, hasJsonPath("positions.ASCENDANT.sign", equalTo("SAGITTAIRE")));
		assertThat(body, hasJsonPath("positions.ASCENDANT.degreeInSign.degree", equalTo(2)));

		verify(timeService, times(2)).queryGoogleForTimezone(anyDouble(), anyDouble(), anyLong());
	}

	@Test
	public void testSimpleSolarRevolutionWithNatalAddress() throws Exception {
		Mockito.doReturn(DateTimeZone.forID("Europe/Paris")).when(timeService)
			   .queryGoogleForTimezone(anyDouble(), anyDouble(), anyLong());

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("/{natal_date}/{natal_address}/revolution/solar/{from_date}/{anniversary_latitude}/{anniversary_longitude}",
					 TEST_NATAL_DATE,
					 "Ris-Orangis",
					 TEST_REV_SOLAR_DATE,
					 PARIS_LATITUDE,
					 PARIS_LONGITUDE);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String body = result.getResponse().getContentAsString();
		MockHttpServletResponse response = result.getResponse();

		assertThat(response.getStatus(), equalTo(HttpStatus.OK.value()));

		assertThat(body, hasJsonPath("date", equalTo("2017-01-04T05:41:02+01:00")));
		assertThat(body, hasJsonPath("positions.SOLEIL.sign", equalTo("CAPRICORNE")));
		assertThat(body, hasJsonPath("positions.SOLEIL.degreeInSign.degree", equalTo(14)));
		assertThat(body, hasJsonPath("positions.SOLEIL.degreeInSign.minutes", equalTo(0)));
		assertThat(body, hasJsonPath("positions.ASCENDANT.sign", equalTo("SAGITTAIRE")));
		assertThat(body, hasJsonPath("positions.ASCENDANT.degreeInSign.degree", equalTo(2)));

		verify(timeService, times(2)).queryGoogleForTimezone(anyDouble(), anyDouble(), anyLong());
	}

	@Test
	public void testSimpleSolarRevolutionWithAnniversaryAddress() throws Exception {
		Mockito.doReturn(DateTimeZone.forID("Europe/Paris")).when(timeService)
			   .queryGoogleForTimezone(anyDouble(), anyDouble(), anyLong());

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("/{natal_date}/{natal_latitude}/{natal_longitude}/revolution/solar/{from_date}/{anniversary_address}",
					 TEST_NATAL_DATE,
					 NATAL_LATITUDE,
					 NATAL_LONGITUDE,
					 TEST_REV_SOLAR_DATE,
					 "Paris");

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String body = result.getResponse().getContentAsString();
		MockHttpServletResponse response = result.getResponse();

		assertThat(response.getStatus(), equalTo(HttpStatus.OK.value()));

		assertThat(body, hasJsonPath("date", equalTo("2017-01-04T05:41:02+01:00")));
		assertThat(body, hasJsonPath("positions.SOLEIL.sign", equalTo("CAPRICORNE")));
		assertThat(body, hasJsonPath("positions.SOLEIL.degreeInSign.degree", equalTo(14)));
		assertThat(body, hasJsonPath("positions.SOLEIL.degreeInSign.minutes", equalTo(0)));
		assertThat(body, hasJsonPath("positions.ASCENDANT.sign", equalTo("SAGITTAIRE")));
		assertThat(body, hasJsonPath("positions.ASCENDANT.degreeInSign.degree", equalTo(2)));

		verify(timeService, times(2)).queryGoogleForTimezone(anyDouble(), anyDouble(), anyLong());
	}

	@Test
	public void testSimpleSolarRevolutionWithBothAddress() throws Exception {
		Mockito.doReturn(DateTimeZone.forID("Europe/Paris")).when(timeService)
			   .queryGoogleForTimezone(anyDouble(), anyDouble(), anyLong());

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("/{natal_date}/{natal_address}/revolution/solar/{from_date}/{anniversary_address}",
				TEST_NATAL_DATE,
				"Ris-Orangis",
				TEST_REV_SOLAR_DATE,
				"Paris");

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String body = result.getResponse().getContentAsString();
		MockHttpServletResponse response = result.getResponse();

		assertThat(response.getStatus(), equalTo(HttpStatus.OK.value()));
				assertThat(body, hasJsonPath("date", equalTo("2017-01-04T05:41:02+01:00")));
				assertThat(body, hasJsonPath("positions.SOLEIL.sign", equalTo("CAPRICORNE")));
				assertThat(body, hasJsonPath("positions.SOLEIL.degreeInSign.degree", equalTo(14)));
				assertThat(body, hasJsonPath("positions.SOLEIL.degreeInSign.minutes", equalTo(0)));
				assertThat(body, hasJsonPath("positions.ASCENDANT.sign", equalTo("SAGITTAIRE")));
				assertThat(body, hasJsonPath("positions.ASCENDANT.degreeInSign.degree", equalTo(2)));

		verify(timeService, times(2)).queryGoogleForTimezone(anyDouble(), anyDouble(), anyLong());
	}

	@Test
	public void testSimpleSolarRevolutionDate() throws Exception {
		Mockito.doReturn(DateTimeZone.forID("Europe/Paris")).when(timeService)
			   .queryGoogleForTimezone(anyDouble(), anyDouble(), anyLong());

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("/{natal_date}/{natal_latitude}/{natal_longitude}/revolution/solar/{from_date}/date",
				TEST_NATAL_DATE,
				NATAL_LATITUDE,
				NATAL_LONGITUDE,
				TEST_REV_SOLAR_DATE);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String body = result.getResponse().getContentAsString();
		MockHttpServletResponse response = result.getResponse();

		assertThat(response.getStatus(), equalTo(HttpStatus.OK.value()));
				assertThat(body, hasJsonPath("date", equalTo("2017-01-04T04:41:02Z")));
	}

	@Test
	public void testSimpleSolarRevolutionDateWithAddress() throws Exception {
		Mockito.doReturn(DateTimeZone.forID("Europe/Paris")).when(timeService)
			   .queryGoogleForTimezone(anyDouble(), anyDouble(), anyLong());

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("/{natal_date}/{address}/revolution/solar/{from_date}/date",
				TEST_NATAL_DATE,
				"Ris-Orangis",
				TEST_REV_SOLAR_DATE);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String body = result.getResponse().getContentAsString();
		MockHttpServletResponse response = result.getResponse();

		assertThat(response.getStatus(), equalTo(HttpStatus.OK.value()));
				assertThat(body, hasJsonPath("date", equalTo("2017-01-04T04:41:02Z")));
	}

	@Test
	public void testSimpleLunarRevolution() throws Exception {
		Mockito.doReturn(DateTimeZone.forID("Europe/Paris")).when(timeService)
			   .queryGoogleForTimezone(anyDouble(), anyDouble(), anyLong());

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("/{natal_date}/{natal_latitude}/{natal_longitude}/revolution/lunar/{from_date}/{anniversary_latitude}/{anniversary_longitude}",
				TEST_NATAL_DATE,
				NATAL_LATITUDE,
				NATAL_LONGITUDE,
				TEST_REV_LUNAR_DATE,
				PARIS_LATITUDE,
				PARIS_LONGITUDE);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String body = result.getResponse().getContentAsString();
		MockHttpServletResponse response = result.getResponse();

		assertThat(response.getStatus(), equalTo(HttpStatus.OK.value()));
				assertThat(body, hasJsonPath("date", equalTo("2017-05-26T08:33:39+02:00")));
				assertThat(body, hasJsonPath("positions.LUNE.sign", equalTo("GEMEAUX")));
				assertThat(body, hasJsonPath("positions.LUNE.degreeInSign.degree", equalTo(11)));
				assertThat(body, hasJsonPath("positions.LUNE.degreeInSign.minutes", equalTo(40)));
				assertThat(body, hasJsonPath("positions.ASCENDANT.sign", equalTo("CANCER")));
				assertThat(body, hasJsonPath("positions.ASCENDANT.degreeInSign.degree", equalTo(12)));

		verify(timeService, times(2)).queryGoogleForTimezone(anyDouble(), anyDouble(), anyLong());
	}

	@Test
	public void testSimpleLunarRevolutionWithNatalAddress() throws Exception {
		Mockito.doReturn(DateTimeZone.forID("Europe/Paris")).when(timeService)
			   .queryGoogleForTimezone(anyDouble(), anyDouble(), anyLong());

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("/{natal_date}/{natal_address}/revolution/lunar/{from_date}/{anniversary_latitude}/{anniversary_longitude}",
				TEST_NATAL_DATE,
				"Ris-Orangis",
				TEST_REV_LUNAR_DATE,
				PARIS_LATITUDE,
				PARIS_LONGITUDE);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String body = result.getResponse().getContentAsString();
		MockHttpServletResponse response = result.getResponse();

		assertThat(response.getStatus(), equalTo(HttpStatus.OK.value()));
				assertThat(body, hasJsonPath("date", equalTo("2017-05-26T08:33:39+02:00")));
				assertThat(body, hasJsonPath("positions.LUNE.sign", equalTo("GEMEAUX")));
				assertThat(body, hasJsonPath("positions.LUNE.degreeInSign.degree", equalTo(11)));
				assertThat(body, hasJsonPath("positions.LUNE.degreeInSign.minutes", equalTo(40)));
				assertThat(body, hasJsonPath("positions.ASCENDANT.sign", equalTo("CANCER")));
				assertThat(body, hasJsonPath("positions.ASCENDANT.degreeInSign.degree", equalTo(12)));

		verify(timeService, times(2)).queryGoogleForTimezone(anyDouble(), anyDouble(), anyLong());
	}

	@Test
	public void testSimpleLunarRevolutionWithAnniversaryAddress() throws Exception {
		Mockito.doReturn(DateTimeZone.forID("Europe/Paris")).when(timeService)
			   .queryGoogleForTimezone(anyDouble(), anyDouble(), anyLong());

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("/{natal_date}/{natal_latitude}/{natal_longitude}/revolution/lunar/{from_date}/{anniversary_address}",
				TEST_NATAL_DATE,
				NATAL_LATITUDE,
				NATAL_LONGITUDE,
				TEST_REV_LUNAR_DATE,
				"Paris");

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String body = result.getResponse().getContentAsString();
		MockHttpServletResponse response = result.getResponse();

		assertThat(response.getStatus(), equalTo(HttpStatus.OK.value()));
				assertThat(body, hasJsonPath("date", equalTo("2017-05-26T08:33:39+02:00")));
				assertThat(body, hasJsonPath("positions.LUNE.sign", equalTo("GEMEAUX")));
				assertThat(body, hasJsonPath("positions.LUNE.degreeInSign.degree", equalTo(11)));
				assertThat(body, hasJsonPath("positions.LUNE.degreeInSign.minutes", equalTo(40)));
				assertThat(body, hasJsonPath("positions.ASCENDANT.sign", equalTo("CANCER")));
				assertThat(body, hasJsonPath("positions.ASCENDANT.degreeInSign.degree", equalTo(12)));

		verify(timeService, times(2)).queryGoogleForTimezone(anyDouble(), anyDouble(), anyLong());
	}

	@Test
	public void testSimpleLunarRevolutionWithBothAddress() throws Exception {
		Mockito.doReturn(DateTimeZone.forID("Europe/Paris")).when(timeService)
			   .queryGoogleForTimezone(anyDouble(), anyDouble(), anyLong());

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("/{natal_date}/{natal_address}/revolution/lunar/{from_date}/{anniversary_address}",
				TEST_NATAL_DATE,
				"Ris-Orangis",
				TEST_REV_LUNAR_DATE,
				"Paris");

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String body = result.getResponse().getContentAsString();
		MockHttpServletResponse response = result.getResponse();

		assertThat(response.getStatus(), equalTo(HttpStatus.OK.value()));
				assertThat(body, hasJsonPath("date", equalTo("2017-05-26T08:33:39+02:00")));
				assertThat(body, hasJsonPath("positions.LUNE.sign", equalTo("GEMEAUX")));
				assertThat(body, hasJsonPath("positions.LUNE.degreeInSign.degree", equalTo(11)));
				assertThat(body, hasJsonPath("positions.LUNE.degreeInSign.minutes", equalTo(40)));
				assertThat(body, hasJsonPath("positions.ASCENDANT.sign", equalTo("CANCER")));
				assertThat(body, hasJsonPath("positions.ASCENDANT.degreeInSign.degree", equalTo(12)));

		verify(timeService, times(2)).queryGoogleForTimezone(anyDouble(), anyDouble(), anyLong());
	}

	@Test
	public void testSimpleLunarRevolutionDate() throws Exception {
		Mockito.doReturn(DateTimeZone.forID("Europe/Paris")).when(timeService)
			   .queryGoogleForTimezone(anyDouble(), anyDouble(), anyLong());

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("/{natal_date}/{natal_latitude}/{natal_longitude}/revolution/lunar/{from_date}/date",
				TEST_NATAL_DATE,
				NATAL_LATITUDE,
				NATAL_LONGITUDE,
				TEST_REV_LUNAR_DATE);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String body = result.getResponse().getContentAsString();
		MockHttpServletResponse response = result.getResponse();

		assertThat(response.getStatus(), equalTo(HttpStatus.OK.value()));
				assertThat(body, hasJsonPath("date", equalTo("2017-05-26T06:33:39Z")));
	}

	@Test
	public void testSimpleLunarRevolutionDateWithAddress() throws Exception {
		Mockito.doReturn(DateTimeZone.forID("Europe/Paris")).when(timeService)
			   .queryGoogleForTimezone(anyDouble(), anyDouble(), anyLong());

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("/{natal_date}/{address}/revolution/lunar/{from_date}/date",
				TEST_NATAL_DATE,
				"Ris-Orangis",
				TEST_REV_LUNAR_DATE);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String body = result.getResponse().getContentAsString();
		MockHttpServletResponse response = result.getResponse();

		assertThat(response.getStatus(), equalTo(HttpStatus.OK.value()));
				assertThat(body, hasJsonPath("date", equalTo("2017-05-26T06:33:39Z")));
	}
}