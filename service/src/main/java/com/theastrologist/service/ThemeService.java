package com.theastrologist.service;

import com.theastrologist.domain.Degree;
import com.theastrologist.domain.SkyPosition;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ThemeService {

	private static final Logger LOG = Logger.getLogger(ThemeService.class);

	@Autowired
	private AspectService aspectService;

	@Autowired
	private Swieph swieph;

	public ThemeService() {}

	public SkyPosition getSkyPosition(DateTime dateTime, Degree latitude, Degree longitude) {
		SkyPosition skyPosition = new SkyPosition(dateTime, latitude, longitude);
		LOG.debug("Calculating sky position for Date = " + dateTime + ", with Latitude = " + latitude +
				  " and Longitude = " + longitude);
		skyPosition.calculate(swieph.value());
		skyPosition.setAspects(aspectService.createAspectsForSkyPosition(skyPosition));

		return skyPosition;
	}
}
