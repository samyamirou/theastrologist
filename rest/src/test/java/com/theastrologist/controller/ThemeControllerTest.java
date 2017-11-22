package com.theastrologist.controller;


import com.theastrologist.domain.Degree;
import com.theastrologist.util.TimeService;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.jayway.jsonpath.matchers.JsonPathMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;


/**
 * Classes de test pour tester les URL de type "48.6456630/2.4104510/1985-01-04T11:20:00+01:00/theme/"
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
//@ActiveProfiles("test")
//@WebMvcTest(ThemeController.class)
public class ThemeControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private TimeService timeService;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testSimpleTheme() throws Exception {
		double latitude = new Degree(48, 39).getBaseDegree();
		double longitude = new Degree(2, 25).getBaseDegree();
		String dateTime = "1985-01-04T11:20:00";

		Mockito.when(timeService.queryGoogleForTimezone(anyDouble(), anyDouble(), anyLong()))
			   .thenReturn(DateTimeZone.forID("Europe/Paris"));

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("/{datetime}/{latitude}/{longitude}/theme",
					 dateTime, latitude, longitude)
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String body = result.getResponse().getContentAsString();
		MockHttpServletResponse response = result.getResponse();

		assertThat(response.getStatus(), equalTo(HttpStatus.OK.value()));
		assertThat(body, hasJsonPath("positions.MERCURE.sign", equalTo("SAGITTAIRE")));
		assertThat(body, hasJsonPath("positions.LUNE.house", equalTo("III")));
		assertThat(body, hasJsonPath("positions.ASCENDANT.sign", equalTo("POISSONS")));
	}

	@Test
	public void testSimpleDateFormat() throws Exception {
		Mockito.when(timeService.queryGoogleForTimezone(anyDouble(), anyDouble(), anyLong()))
			   .thenReturn(DateTimeZone.forID("Europe/Paris"));

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("/{datetime}/{latitude}/{longitude}/theme",
					 "1985-01-04T11:20:00",
					 new Degree(48, 39).getBaseDegree(),
					 new Degree(2, 25).getBaseDegree())
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String body = result.getResponse().getContentAsString();
		MockHttpServletResponse response = result.getResponse();

		// Ce test vérifie que la date est bien prise en compte et que le format est correct
		assertThat(response.getStatus(), equalTo(HttpStatus.OK.value()));
		assertThat(body, hasJsonPath("date", equalTo("1985-01-04T11:20:00+01:00")));

	}

	@Test
	public void testSimpleLatitude() throws Exception {
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("/{datetime}/{latitude}/{longitude}/theme",
					 "1985-01-04T11:20:00",
					 new Degree(32, 39).getBaseDegree(),
					 new Degree(4, 5).getBaseDegree())
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String body = result.getResponse().getContentAsString();
		MockHttpServletResponse response = result.getResponse();

		assertThat(response.getStatus(), equalTo(HttpStatus.OK.value()));
		assertThat(body, hasJsonPath("latitude.degree", equalTo(32)));
		assertThat(body, hasJsonPath("latitude.minutes", equalTo(39)));
	}

	@Test
	public void testSimpleLatitudeAutreFormat() throws Exception {
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("/{datetime}/{latitude}/{longitude}/theme",
					 "1985-01-04T11:20:00",
					 48.6456630, 2.4104510)
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String body = result.getResponse().getContentAsString();
		MockHttpServletResponse response = result.getResponse();

		assertThat(response.getStatus(), equalTo(HttpStatus.OK.value()));
		// Ce test vérifie que la latitude est bien prise en compte
		assertThat(body, hasJsonPath("latitude.degree", equalTo(48)));
		assertThat(body, hasJsonPath("latitude.minutes", equalTo(38)));
	}

	@Test
	public void testSimpleLongitude() throws Exception {
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("/{datetime}/{latitude}/{longitude}/theme",
					 "1985-01-04T11:20:00",
					 new Degree(12, 59).getBaseDegree(),
					 new Degree(4, 41).getBaseDegree())
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String body = result.getResponse().getContentAsString();
		MockHttpServletResponse response = result.getResponse();

		assertThat(response.getStatus(), equalTo(HttpStatus.OK.value()));
		// Ce test vérifie que la longitude est bien prise en compte
		assertThat(body, hasJsonPath("longitude.degree", equalTo(4)));
		assertThat(body, hasJsonPath("longitude.minutes", equalTo(41)));
	}

	@Test
	public void testSimpleLongitudeAutreFormat() throws Exception {
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("/{datetime}/{latitude}/{longitude}/theme",
					 "1985-01-04T11:20:00",
					 48.6456630, 2.4104510)
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String body = result.getResponse().getContentAsString();
		MockHttpServletResponse response = result.getResponse();

		assertThat(response.getStatus(), equalTo(HttpStatus.OK.value()));
		// Ce test vérifie que la longitude est bien prise en compte
		assertThat(body, hasJsonPath("longitude.degree", equalTo(2)));
		assertThat(body, hasJsonPath("longitude.minutes", equalTo(24)));
	}

	@Test
	public void testWrongDate() throws Exception {

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("/{datetime}/{latitude}/{longitude}/theme",
					 "Mauvaise date",
					 new Degree(48, 39).getBaseDegree(), new Degree(2, 25).getBaseDegree())
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();

		assertThat(response.getStatus(), equalTo(HttpStatus.BAD_REQUEST.value()));
		assertThat(response.getErrorMessage(), equalTo("Wrong date format"));

	}

	@Test
	public void testWrongLatitude() throws Exception {
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("/{datetime}/{latitude}/{longitude}/theme",
					 "1985-01-04T11:20:00",
					 "truc",
					 new Degree(2, 25).getBaseDegree())
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		assertThat(response.getStatus(), equalTo(HttpStatus.BAD_REQUEST.value()));
	}

	@Test
	public void testWrongLongitude() throws Exception {

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("/{datetime}/{latitude}/{longitude}/theme",
					 "1985-01-04T11:20:00",
					 new Degree(2, 25).getBaseDegree(),
					 "truc")
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();

		assertThat(response.getStatus(), equalTo(HttpStatus.BAD_REQUEST.value()));
	}

	@Test
	public void testSimpleThemeWithAddressParis() throws Exception {
		String dateTime = "1985-01-04T11:20:00";

		Mockito.when(timeService.queryGoogleForTimezone(anyDouble(), anyDouble(), anyLong()))
			   .thenReturn(DateTimeZone.forID("Europe/Paris"));

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("/{datetime}/{address}/theme", dateTime, "Ris-Orangis")
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String body = result.getResponse().getContentAsString();
		MockHttpServletResponse response = result.getResponse();

		assertThat(response.getStatus(), equalTo(HttpStatus.OK.value()));
		assertThat(body, hasJsonPath("address", equalTo("91130 Ris-Orangis, France")));
		assertThat(body, hasJsonPath("positions.MERCURE.sign", equalTo("SAGITTAIRE")));
		assertThat(body, hasJsonPath("positions.LUNE.house", equalTo("III")));
		assertThat(body, hasJsonPath("positions.ASCENDANT.sign", equalTo("POISSONS")));
	}

	// Apparemment Google ne renvoie plus d'erreur en cas d'ambiguité
	@Test
	@Ignore
	public void testTooManyResults() throws Exception {
		String dateTime = "1985-01-04T11:20:00";

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("/{datetime}/{address}/theme", dateTime, "Chi")
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();

		assertThat(response.getStatus(), equalTo(HttpStatus.BAD_REQUEST.value()));
		assertThat(response.getErrorMessage(), equalTo("Too many results for geoloc"));
	}

	@Test
	public void testNoResult() throws Exception {
		String dateTime = "1985-01-04T11:20:00";

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("/{datetime}/{address}/theme", dateTime, "Choudavoir")
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();

		assertThat(response.getStatus(), equalTo(HttpStatus.BAD_REQUEST.value()));
		assertThat(response.getErrorMessage(), equalTo("No result returned by Google API"));
	}
}