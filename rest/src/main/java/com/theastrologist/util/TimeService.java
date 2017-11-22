package com.theastrologist.util;

import com.theastrologist.controller.exception.WrongDateRestException;
import com.theastrologist.external.GoogleRestException;
import com.theastrologist.external.timezone.TimezoneResponse;
import com.theastrologist.external.timezone.TimezoneRestClient;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriUtils;

import java.io.UnsupportedEncodingException;

/**
 * Created by Samy on 16/09/2015.
 */
@Service
public class TimeService {
	private static final Logger LOGGER = Logger.getLogger(TimeService.class);

	public DateTime parseDateTime(String datetime, double latitude, double longitude)
			throws WrongDateRestException {
		LocalDateTime parsedDate = toLocalDateTime(datetime);
		DateTime dateTime = parsedDate.toDateTime(DateTimeZone.UTC);
		DateTimeZone dateTimeZone = getTimezone(dateTime, latitude, longitude);
		if (dateTimeZone != null) {
			dateTime = parsedDate.toDateTime(dateTimeZone);
		}
		return dateTime;
	}

	private DateTimeZone getTimezone(DateTime dateTime, double latitude, double longitude) {
		long millis = dateTime.getMillis();
		return queryGoogleForTimezone(latitude, longitude, millis);
	}

	private LocalDateTime toLocalDateTime(String datetime) throws WrongDateRestException {
		LocalDateTime parsedDate = null;
		try {
			String decodedDateTime = UriUtils.decode(datetime, "UTF-8");
			parsedDate = LocalDateTime.parse(decodedDateTime);
		} catch (IllegalArgumentException e) {
			throw new WrongDateRestException(datetime, e);
		}
		return parsedDate;
	}

	public DateTime convertUTDateTime(DateTime utcDatetime, double latitude, double longitude)
			throws WrongDateRestException {
		DateTimeZone dateTimeZone = getTimezone(utcDatetime.toDateTime(DateTimeZone.UTC), latitude, longitude);
		return utcDatetime.withZone(dateTimeZone);
	}

	public DateTimeZone queryGoogleForTimezone(double latitude, double longitude, long millis) {
		String s = Long.toString(millis);
		long timestamp;
		timestamp = s.length() > 12 ? Long.parseLong(s.substring(0, 11)): millis;

		TimezoneRestClient client = new TimezoneRestClient(latitude, longitude, timestamp);
		TimezoneResponse response = null;
		DateTimeZone dateTimeZone = null;
		try {
			response = client.getTimezone();
			dateTimeZone = DateTimeZone.forID(response.timeZoneId);
		} catch (GoogleRestException e) {
			LOGGER.warn("Google Timezone API exception, using UTC instead", e);
		}
		return dateTimeZone;
	}
}
