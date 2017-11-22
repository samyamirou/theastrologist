package com.theastrologist.controller;

import com.theastrologist.service.ThemeService;
import com.theastrologist.service.TransitPeriodService;
import com.theastrologist.domain.Degree;
import com.theastrologist.domain.SkyPosition;
import com.theastrologist.domain.transitperiod.TransitPeriods;
import com.theastrologist.external.geoloc.GeoResult;
import com.theastrologist.external.geoloc.GeolocException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Samy on 16/09/2015.
 */
@RestController
@Api(value = "/transitperiod", tags = "Transit period", description = "Transit periods for a theme")
public class TransitPeriodController extends AbstractController {

	@Autowired
	private ThemeService themeService;

	@Autowired
	private TransitPeriodService transitPeriodService;

	@ApiOperation(value = "Transit period", produces = "application/json")
	@GetMapping(value = "/{natalDate}/{latitude:.+}/{longitude:.+}/transitperiod/{startDate}/{endDate}")
	public TransitPeriods getTransitPeriod(
			@ApiParam(value = "Natal date and time. ISO Datetime format, ex : 2018-01-22T22:04:19", required = true) @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) String natalDate,
			@PathVariable double latitude,
			@PathVariable double longitude,
			@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String startDate,
			@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String endDate) {
		return getTransitPeriods(natalDate, latitude, longitude, startDate, endDate);
	}

	@ApiOperation(value = "Transit period", produces = "application/json")
	@GetMapping(value = "/{natalDate}/{address}/transitperiod/{startDate}/{endDate}")
	public TransitPeriods getTransitPeriodWithAddress(
			@ApiParam(value = "Natal date and time. ISO Datetime format, ex : 2018-01-22T22:04:19", required = true) @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) String natalDate,
			@ApiParam(value = "Natal location. Ex : '75015, FR', '1600 Amphitheatre Pkwy, Mountain View, CA 94043'", required = true) @PathVariable String address,
			@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String startDate,
			@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String endDate) throws GeolocException {

		GeoResult geoResult = queryForGeoloc(address);
		double latitude = geoResult.getGeometry().getLocation().getLat();
		double longitude = geoResult.getGeometry().getLocation().getLng();
		return getTransitPeriods(natalDate, latitude, longitude, startDate, endDate);
	}

	private TransitPeriods getTransitPeriods(String natalDate, double latitude, double longitude, String startDate,
											 String endDate) {
		DateTime parsedNatalDate = timeService.parseDateTime(natalDate, latitude, longitude);
		DateTime parsedStartDate = new DateTime(startDate);
		DateTime parsedEndDate = new DateTime(endDate);
		Degree parsedLatitude = new Degree(latitude);
		Degree parsedLongitude = new Degree(longitude);
		SkyPosition natalPosition = themeService
				.getSkyPosition(parsedNatalDate, parsedLatitude, parsedLongitude);
		return transitPeriodService
				.createTransitPeriod(natalPosition, parsedStartDate, parsedEndDate, parsedLatitude, parsedLongitude);
	}
}
