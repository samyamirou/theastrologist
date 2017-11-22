package com.theastrologist.util;

import com.theastrologist.controller.RevolutionController;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

/**
 * Created by Samy on 27/01/2016.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
//@ActiveProfiles("test")
//@WebMvcTest(RevolutionController.class)
public class TimeServiceTest {

	@Autowired
	private TimeService timeService;

	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		reset(timeService);
	}

	@Test
	public void convertDateTimeFr() throws Exception {
		doReturn(DateTimeZone.forID("Europe/Paris")).when(timeService).queryGoogleForTimezone(anyDouble(), anyDouble(), anyLong());

		DateTime dateTime = timeService
				.convertUTDateTime(new DateTime("1985-01-04T11:20:00.000Z"), 48.64566300000001, 2.410451);
		assertThat(dateTime, notNullValue());
		assertThat(dateTime, hasToString("1985-01-04T12:20:00.000+01:00"));
	}

	@Test
	public void convertDateTimeFrEte() throws Exception {
		when(timeService.queryGoogleForTimezone(anyDouble(), anyDouble(), anyLong()))
				.thenReturn(DateTimeZone.forID("Europe/Paris"));

		DateTime dateTime = timeService
				.convertUTDateTime(new DateTime("1979-08-15T21:30:00.000Z"), 48.64566300000001, 2.410451);
		assertThat(dateTime, notNullValue());
		assertThat(dateTime, hasToString("1979-08-15T23:30:00.000+02:00"));
	}

	@Test
	public void parseDateTimeFr() throws Exception {
		when(timeService.queryGoogleForTimezone(anyDouble(), anyDouble(), anyLong()))
				.thenReturn(DateTimeZone.forID("Europe/Paris"));

		DateTime dateTime = timeService.parseDateTime("1985-01-04T11:20", 48.64566300000001, 2.410451);
		assertThat(dateTime, notNullValue());
		assertThat(dateTime, hasToString("1985-01-04T11:20:00.000+01:00"));
	}

	@Test
	public void parseDateTimeFrEte() throws Exception {
		when(timeService.queryGoogleForTimezone(anyDouble(), anyDouble(), anyLong()))
				.thenReturn(DateTimeZone.forID("Europe/Paris"));

		DateTime dateTime = timeService.parseDateTime("1979-08-15T21:30", 48.64566300000001, 2.410451);
		assertThat(dateTime, notNullValue());
		assertThat(dateTime, hasToString("1979-08-15T21:30:00.000+02:00"));
	}

	@Test
	public void parseDateTimeChili() throws Exception {
		when(timeService.queryGoogleForTimezone(anyDouble(), anyDouble(), anyLong()))
				.thenReturn(DateTimeZone.forID("America/Santiago"));

		DateTime dateTime = timeService.parseDateTime("1962-04-07T1:15", -26.3902488, -70.0475302);
		assertThat(dateTime, notNullValue());
		assertThat(dateTime, hasToString("1962-04-07T01:15:00.000-04:00"));
	}

	@Test
	public void parseDateTimeColombie() throws Exception {
		when(timeService.queryGoogleForTimezone(anyDouble(), anyDouble(), anyLong()))
				.thenReturn(DateTimeZone.forOffsetHours(-5));

		DateTime dateTime = timeService.parseDateTime("1979-03-24T17:00", 4.710988599999999, -74.072092);
		assertThat(dateTime, notNullValue());
		assertThat(dateTime, hasToString("1979-03-24T17:00:00.000-05:00"));
	}
}