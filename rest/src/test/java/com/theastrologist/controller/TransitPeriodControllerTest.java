package com.theastrologist.controller;

import com.theastrologist.RestApplication;
import com.theastrologist.config.WebTestConfiguration;
import com.theastrologist.data.DataConfiguration;
import com.theastrologist.domain.Degree;
import com.theastrologist.service.config.ServiceConfiguration;
import com.theastrologist.util.TimeService;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.jayway.jsonpath.matchers.JsonPathMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by Samy on 16/09/2015.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TransitPeriodControllerTest {
	@Autowired
	private TimeService timeService;

	@Autowired
	private MockMvc mockMvc;

	@BeforeClass
	public static void setUpClass() throws Exception {
		MockitoAnnotations.initMocks(TransitPeriodController.class);
	}

	@Before
	public void setUp() throws Exception {
		reset(timeService);
	}

	@Test
	public void testRequest() throws Exception {
		when(timeService.queryGoogleForTimezone(anyDouble(), anyDouble(), anyLong()))
				.thenReturn(DateTimeZone.forID("Europe/Paris"));

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("/{natalDate}/{latitude:.+}/{longitude:.+}/transitperiod/{startDate}/{endDate}",
					 "1985-01-04T11:20:00",
					 Double.toString(new Degree(48, 39).getBaseDegree()),
					 Double.toString(new Degree(2, 25).getBaseDegree()),
					 "2014-01-01T11:20:00",
					 "2016-01-01T11:20:00");

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String body = result.getResponse().getContentAsString();
		MockHttpServletResponse response = result.getResponse();

		assertThat(response.getStatus(), equalTo(HttpStatus.OK.value()));
		assertThat(body, hasJsonPath("planetPeriods.PLUTON", hasSize(4)));
		assertThat(body, hasJsonPath("housePeriods.NOEUD_NORD_MOYEN", hasSize(2)));
		assertThat(body, hasJsonPath("housePeriods.MARS", hasSize(14)));
	}

	@Test
	public void testRequestCompleterPeriodes() throws Exception {
		when(timeService.queryGoogleForTimezone(anyDouble(), anyDouble(), anyLong()))
				.thenReturn(DateTimeZone.forID("Europe/Paris"));

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("/{natalDate}/{latitude:.+}/{longitude:.+}/transitperiod/{startDate}/{endDate}",
					 "1985-01-04T11:20:00",
					 Double.toString(new Degree(48, 39).getBaseDegree()),
					 Double.toString(new Degree(2, 25).getBaseDegree()),
					 "2014-01-01T11:20:00",
					 "2016-01-01T11:20:00");

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String body = result.getResponse().getContentAsString();
		MockHttpServletResponse response = result.getResponse();

		assertThat(response.getStatus(), equalTo(HttpStatus.OK.value()));
		assertThat(body, hasJsonPath("planetPeriods.PLUTON", hasSize(4)));
		assertThat(body, hasJsonPath("housePeriods.NOEUD_NORD_MOYEN", hasSize(2)));
		assertThat(body, hasJsonPath("housePeriods.MARS", hasSize(14)));
	}

	@Test
	public void testRequestShortDates() throws Exception {
		when(timeService.queryGoogleForTimezone(anyDouble(), anyDouble(), anyLong()))
				.thenReturn(DateTimeZone.forID("Europe/Paris"));

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("/{natalDate}/{latitude:.+}/{longitude:.+}/transitperiod/{startDate}/{endDate}",
					 "1985-01-04T11:20:00",
					 Double.toString(new Degree(48, 39).getBaseDegree()),
					 Double.toString(new Degree(2, 25).getBaseDegree()),
					 "2014-01-01",
					 "2016-01-01");

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String body = result.getResponse().getContentAsString();
		MockHttpServletResponse response = result.getResponse();

		assertThat(response.getStatus(), equalTo(HttpStatus.OK.value()));
		assertThat(body, hasJsonPath("planetPeriods.PLUTON", hasSize(4)));
	}

	@Test
	public void testTransitPeriodRequestWithAddress() throws Exception {

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("/{natalDate}/{address}/transitperiod/{startDate}/{endDate}",
					 "1985-01-04T11:20:00", "Ris-Orangis", "2014-01-01", "2016-01-01");

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String body = result.getResponse().getContentAsString();
		MockHttpServletResponse response = result.getResponse();

		assertThat(response.getStatus(), equalTo(HttpStatus.OK.value()));
		assertThat(body, hasJsonPath("planetPeriods.PLUTON", hasSize(4)));
		assertThat(body, hasJsonPath("housePeriods.NOEUD_NORD_MOYEN", hasSize(2)));
		assertThat(body, hasJsonPath("housePeriods.MARS", hasSize(14)));
	}
}
