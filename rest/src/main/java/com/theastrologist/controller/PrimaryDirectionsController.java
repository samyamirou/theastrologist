package com.theastrologist.controller;

import com.theastrologist.service.PrimaryDirectionsService;
import com.theastrologist.service.ThemeService;
import com.theastrologist.domain.Degree;
import com.theastrologist.domain.SkyPosition;
import com.theastrologist.domain.primarydirection.PrimaryDirection;
import com.theastrologist.external.geoloc.GeoResult;
import com.theastrologist.external.geoloc.GeolocException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.SortedSet;

/**
 * Created by Samy on 15/05/2017.
 */
@RestController
@Api(value = "/primaryDirections", tags = "Primary Directions", description = "Primary directions")
public class PrimaryDirectionsController extends AbstractController {

	@Autowired
	private ThemeService themeService;

	@Autowired
	private PrimaryDirectionsService primaryDirectionService;

	private SkyPosition calculateSkyPosition(DateTime natalDate, double natalLatitude, double natalLongitude) {
		Degree latitudeDegree = new Degree(natalLatitude);
		Degree longitudeDegree = new Degree(natalLongitude);
		return themeService.getSkyPosition(natalDate, latitudeDegree, longitudeDegree);
	}

	private SkyPosition getNatalTheme(String natalDate, double natalLatitude, double natalLongitude) {
		DateTime natalDateTime = timeService.parseDateTime(natalDate, natalLatitude, natalLongitude);
		return calculateSkyPosition(natalDateTime, natalLatitude, natalLongitude);
	}


	@ApiOperation(value = "Primary directions", produces = "application/json")
	@GetMapping(value = "/{natalDate}/{natalLatitude:.+}/{natalLongitude:.+}/primaryDirections")
	public SortedSet<PrimaryDirection> getPrimaryDirections(
			@ApiParam(value = "Natal date and time. ISO Datetime format, ex : 2018-01-22T22:04:19", required = true) @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) String natalDate,
			@ApiParam(value = "Natal location latitude", required = true) @PathVariable double natalLatitude,
			@ApiParam(value = "Natal location longitude", required = true) @PathVariable double natalLongitude) {
		SkyPosition natalTheme = getNatalTheme(natalDate, natalLatitude, natalLongitude);

		return primaryDirectionService.getPrimaryDirections(natalTheme);
	}

	@ApiOperation(value = "Primary directions", produces = "application/json")
	@GetMapping(value = "/{natalDate}/{natalAddress}/primaryDirections")
	public SortedSet<PrimaryDirection> getPrimaryDirections(
			@ApiParam(value = "Natal date and time. ISO Datetime format, ex : 2018-01-22T22:04:19", required = true) @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) String natalDate,
			@ApiParam(value = "Natal location. Ex : '75015, FR', '1600 Amphitheatre Pkwy, Mountain View, CA 94043'", required = true) @PathVariable String natalAddress) throws GeolocException {

		GeoResult geoResult = queryForGeoloc(natalAddress);
		double natalLatitude = geoResult.getGeometry().getLocation().getLat();
		double natalLongitude = geoResult.getGeometry().getLocation().getLng();

		SkyPosition natalTheme = getNatalTheme(natalDate, natalLatitude, natalLongitude);
		return primaryDirectionService.getPrimaryDirections(natalTheme);
	}
}
