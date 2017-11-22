package com.theastrologist.controller;

import com.theastrologist.service.ThemeService;
import com.theastrologist.domain.Degree;
import com.theastrologist.domain.SkyPosition;
import com.theastrologist.external.geoloc.*;
import io.swagger.annotations.*;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by SAM on 16/11/2014.
 */
@RestController
@RequestMapping("/{datetime}")
@Api(value = "/theme", tags = "Theme", description = "Astrological theme")
public class ThemeController extends AbstractController {

	@ApiOperation(value = "Calculate astral chart", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successfully calculated"),
			@ApiResponse(code = 400, message = "Wrong date format, or wrong latitude / longitude numeric format")
	})
	@GetMapping(value = "/{latitude:.+}/{longitude:.+}/theme")
	public SkyPosition getTheme(
			@ApiParam(value = "Theme date and time. ISO Datetime format, ex : 2018-01-22T22:04:19", required = true) @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) String datetime,
			@ApiParam(value = "Theme location latitude", required = true) @PathVariable double latitude,
			@ApiParam(value = "Theme location longitude", required = true) @PathVariable double longitude) {
		return getSkyPosition(datetime, latitude, longitude, null);
	}

	@ApiOperation(value = "Calculate astral chart", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successfully calculated"),
			@ApiResponse(code = 400, message = "Multiple location found for this address, No location found for this address or Wrong date format")})
	@GetMapping(value = "/{address}/theme")
	public ResponseEntity<SkyPosition> getTheme(
			@ApiParam(value = "Theme date and time. ISO Datetime format, ex : 2018-01-22T22:04:19", required = true) @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) String datetime,
			@ApiParam(value = "Theme location. Ex : '75015, FR', '1600 Amphitheatre Pkwy, Mountain View, CA 94043'", required = true) @PathVariable String address)
			throws GeolocException {

		GeoResult geoResult = queryForGeoloc(address);
		double latitude = geoResult.getGeometry().getLocation().getLat();
		double longitude = geoResult.getGeometry().getLocation().getLng();

		SkyPosition skyPosition = getSkyPosition(datetime, latitude, longitude, geoResult.getFormatted_address());
		return new ResponseEntity<SkyPosition>(skyPosition, HttpStatus.OK);
	}
}
