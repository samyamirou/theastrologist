package com.theastrologist.controller;

import com.theastrologist.service.DominantPlanetsService;
import com.theastrologist.service.ThemeService;
import com.theastrologist.domain.Degree;
import com.theastrologist.domain.SkyPosition;
import com.theastrologist.domain.planetvalue.DominantPlanets;
import com.theastrologist.external.geoloc.GeoResult;
import com.theastrologist.external.geoloc.GeolocException;
import io.swagger.annotations.*;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by SAM on 16/11/2014.
 */
@RestController
@RequestMapping("/{datetime}")
@Api(value = "/dominants", tags = "Dominant planets", description = "Dominant planets scores, in descendant order")
public class DominantPlanetsController extends AbstractController {

	@Autowired
    private ThemeService themeService;

	@Autowired
	private DominantPlanetsService dominantPlanetsService;

	private DominantPlanets getDominantPlanets(String datetime, double latitude, double longitude, String address) {
		DateTime parse = timeService.parseDateTime(datetime, latitude, longitude);
		Degree latitudeDegree = new Degree(latitude);
		Degree longitudeDegree = new Degree(longitude);
		SkyPosition skyPosition = themeService.getSkyPosition(parse, latitudeDegree, longitudeDegree);
		if (address != null) {
			skyPosition.setAddress(address);
		}

		DominantPlanets dominantPlanets = dominantPlanetsService.getDominantPlanets(skyPosition);

		return dominantPlanets;
	}

	@ApiOperation(value = "Calculate dominant planets scores, in descendant order", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successfully calculated"),
			@ApiResponse(code = 400, message = "Wrong date format, or wrong latitude / longitude numeric format")
	})
	@GetMapping(value = "/{latitude:.+}/{longitude:.+}/dominants")
	public DominantPlanets getDominantPlanets(
			@ApiParam(value = "Theme date and time. ISO Datetime format, ex : 2018-01-22T22:04:19", required = true) @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) String datetime,
			@ApiParam(value = "Theme location latitude", required = true) @PathVariable double latitude,
			@ApiParam(value = "Theme location longitude", required = true) @PathVariable double longitude) {
		return getDominantPlanets(datetime, latitude, longitude, null);
	}

	@ApiOperation(value = "Calculate dominant planets scores, in descendant order", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successfully calculated"),
			@ApiResponse(code = 400, message = "Multiple location found for this address, No location found for this address or Wrong date format")})
	@GetMapping(value = "/{address}/dominants")
	public ResponseEntity<DominantPlanets> getDominantPlanets(
			@ApiParam(value = "Theme date and time. ISO Datetime format, ex : 2018-01-22T22:04:19", required = true) @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) String datetime,
			@ApiParam(value = "Theme location. Ex : '75015, FR', '1600 Amphitheatre Pkwy, Mountain View, CA 94043'", required = true) @PathVariable String address)
			throws GeolocException {

		GeoResult geoResult = queryForGeoloc(address);
		double latitude = geoResult.getGeometry().getLocation().getLat();
		double longitude = geoResult.getGeometry().getLocation().getLng();

		DominantPlanets dominantPlanets = getDominantPlanets(datetime, latitude, longitude, geoResult.getFormatted_address());
		return new ResponseEntity<DominantPlanets>(dominantPlanets, HttpStatus.OK);
	}
}
