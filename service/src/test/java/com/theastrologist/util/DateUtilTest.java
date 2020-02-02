package com.theastrologist.util;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Duration;
import org.joda.time.Period;
import org.junit.Ignore;
import org.junit.Test;
import swisseph.SweDate;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 * Created by Samy on 03/10/2015.
 */
public class DateUtilTest {

	@Test
	public void testGetSweDateUTC() throws Exception {
		DateTime dateTime = DateTime.parse("1985-01-04T21:00:00+06:00");
		SweDate sweDateUTC = DateUtil.getSweDateUTC(dateTime);
		assertThat(sweDateUTC, notNullValue());
		assertThat((int) sweDateUTC.getHour(), equalTo(15));
	}

	@Ignore
	@Test
	public void testGetPrimaryDirectionArc() throws Exception {
		Period duration = DateUtil.convertPrimaryDirectionDuration(
				new Period(0,3,56,0));

		assertThat(duration.toStandardDays().getDays(), equalTo(365));
	}
}