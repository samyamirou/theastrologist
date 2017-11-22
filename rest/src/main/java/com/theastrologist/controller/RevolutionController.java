package com.theastrologist.controller;

import com.theastrologist.service.RevolutionService;
import com.theastrologist.service.ThemeService;
import com.theastrologist.domain.Degree;
import com.theastrologist.domain.DateTimeJson;
import com.theastrologist.domain.SkyPosition;
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
 * Created by Samy on 15/05/2017.
 */
@RestController
@Api(value = "/revolution", tags = "Revolution", description = "Solar and lunar revolutions")
public class RevolutionController extends AbstractController {

	@Autowired
	private ThemeService themeService;

	@Autowired
	private RevolutionService revolutionService;

	private SkyPosition calculateSkyPosition(DateTime natalDate, double natalLatitude, double natalLongitude) {
		Degree latitudeDegree = new Degree(natalLatitude);
		Degree longitudeDegree = new Degree(natalLongitude);
		return themeService.getSkyPosition(natalDate, latitudeDegree, longitudeDegree);
	}

	private SkyPosition getSolarRevolutionTheme(String fromDate, double anniversaryLatitude,
												double anniversaryLongitude,
												SkyPosition natalTheme) {
		DateTime fromDateTime = new DateTime(fromDate);
		DateTime solarRevolutionUT = revolutionService.getSolarRevolutionUT(natalTheme, fromDateTime);
		DateTime zonedDateTime = timeService
				.convertUTDateTime(solarRevolutionUT, anniversaryLatitude, anniversaryLongitude);
		return calculateSkyPosition(zonedDateTime, anniversaryLatitude, anniversaryLongitude);
	}

	private SkyPosition getNatalTheme(String natalDate, double natalLatitude, double natalLongitude) {
		DateTime natalDateTime = timeService.parseDateTime(natalDate, natalLatitude, natalLongitude);
		return calculateSkyPosition(natalDateTime, natalLatitude, natalLongitude);
	}

	private DateTimeJson getSolarDateTimeJson(String natalDate, double natalLatitude, double natalLongitude,
											  String fromDate) {
		SkyPosition natalTheme = getNatalTheme(natalDate, natalLatitude, natalLongitude);
		DateTime fromDateTime = new DateTime(fromDate);
		DateTime solarRevolutionUT = revolutionService.getSolarRevolutionUT(natalTheme, fromDateTime);
		return new DateTimeJson(solarRevolutionUT);
	}

	private DateTimeJson getLunarDateTimeJson(String natalDate, double natalLatitude, double natalLongitude,
											  String fromDate) {
		SkyPosition natalTheme = getNatalTheme(natalDate, natalLatitude, natalLongitude);

		DateTime fromDateTime = new DateTime(fromDate);
		DateTime lunarRevolutionUT = revolutionService.getLunarRevolutionUT(natalTheme, fromDateTime);
		return new DateTimeJson(lunarRevolutionUT);
	}

	private SkyPosition getLunarRevolutionTheme(String fromDate, double anniversaryLatitude,
												double anniversaryLongitude, SkyPosition natalTheme) {
		DateTime fromDateTime = new DateTime(fromDate);
		DateTime lunarRevolutionUT = revolutionService.getLunarRevolutionUT(natalTheme, fromDateTime);
		DateTime zonedDateTime = timeService
				.convertUTDateTime(lunarRevolutionUT, anniversaryLatitude, anniversaryLongitude);
		return calculateSkyPosition(zonedDateTime, anniversaryLatitude, anniversaryLongitude);
	}

	@ApiOperation(value = "Solar revolution", produces = "application/json")
	@GetMapping(value = "/{natalDate}/{natalLatitude:.+}/{natalLongitude:.+}/revolution/solar/{fromDate}/{anniversaryLatitude:.+}/{anniversaryLongitude:.+}")
	public SkyPosition getSolarRevolution(
			@ApiParam(value = "Natal date and time. ISO Datetime format, ex : 2018-01-22T22:04:19", required = true) @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) String natalDate,
			@ApiParam(value = "Natal location latitude", required = true) @PathVariable double natalLatitude,
			@ApiParam(value = "Natal location longitude", required = true) @PathVariable double natalLongitude,
			@ApiParam(value = "Date from which to calculate the revolution. ISO Date format, ex : 2018-01-22", required = true) @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String fromDate,
			@ApiParam(value = "Revolution moment latitude location", required = true) @PathVariable double anniversaryLatitude,
			@ApiParam(value = "Revolution moment longitude location", required = true) @PathVariable double anniversaryLongitude) {
		SkyPosition natalTheme = getNatalTheme(natalDate, natalLatitude, natalLongitude);

		return getSolarRevolutionTheme(fromDate, anniversaryLatitude,
									   anniversaryLongitude, natalTheme);
	}

	@ApiOperation(value = "Solar revolution", produces = "application/json")
	@GetMapping(value = "/{natalDate}/{natalLatitude:.+}/{natalLongitude:.+}/revolution/solar/{fromDate}/{anniversaryAddress}")
	public SkyPosition getSolarRevolutionWithAnniversaryAddress(
			@ApiParam(value = "Natal date and time. ISO Datetime format, ex : 2018-01-22T22:04:19", required = true) @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) String natalDate,
			@ApiParam(value = "Natal location latitude", required = true) @PathVariable double natalLatitude,
			@ApiParam(value = "Natal location longitude", required = true) @PathVariable double natalLongitude,
			@ApiParam(value = "Date from which to calculate the revolution. ISO Date format, ex : 2018-01-22", required = true) @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String fromDate,
			@ApiParam(value = "Revolution moment location. Ex : '75015, FR', '1600 Amphitheatre Pkwy, Mountain View, CA 94043'", required = true) @PathVariable String anniversaryAddress) throws GeolocException {
		SkyPosition natalTheme = getNatalTheme(natalDate, natalLatitude, natalLongitude);

		GeoResult anniversaryGeo = queryForGeoloc(anniversaryAddress);

		return getSolarRevolutionTheme(fromDate,
									   anniversaryGeo.getGeometry().getLocation().getLat(),
									   anniversaryGeo.getGeometry().getLocation().getLng(), natalTheme);
	}

	@ApiOperation(value = "Solar revolution", produces = "application/json")
	@GetMapping(value = "/{natalDate}/{natalAddress}/revolution/solar/{fromDate}/{anniversaryLatitude:.+}/{anniversaryLongitude:.+}")
	public SkyPosition getSolarRevolutionWithNatalAddress(
			@ApiParam(value = "Natal date and time. ISO Datetime format, ex : 2018-01-22T22:04:19", required = true) @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) String natalDate,
			@ApiParam(value = "Natal location. Ex : '75015, FR', '1600 Amphitheatre Pkwy, Mountain View, CA 94043'", required = true) @PathVariable String natalAddress,
			@ApiParam(value = "Date from which to calculate the revolution. ISO Date format, ex : 2018-01-22", required = true) @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String fromDate,
			@ApiParam(value = "Revolution moment latitude location", required = true) @PathVariable double anniversaryLatitude,
			@ApiParam(value = "Revolution moment longitude location", required = true) @PathVariable double anniversaryLongitude) throws GeolocException {

		GeoResult geoResult = queryForGeoloc(natalAddress);
		double natalLatitude = geoResult.getGeometry().getLocation().getLat();
		double natalLongitude = geoResult.getGeometry().getLocation().getLng();

		SkyPosition natalTheme = getNatalTheme(natalDate, natalLatitude, natalLongitude);

		return getSolarRevolutionTheme(fromDate, anniversaryLatitude, anniversaryLongitude, natalTheme);
	}

	@ApiOperation(value = "Solar revolution", produces = "application/json")
	@GetMapping(value = "/{natalDate}/{natalAddress}/revolution/solar/{fromDate}/{anniversaryAddress}")
	public SkyPosition getSolarRevolutionWithBothAddress(
			@ApiParam(value = "Natal date and time. ISO Datetime format, ex : 2018-01-22T22:04:19", required = true) @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) String natalDate,
			@ApiParam(value = "Natal location. Ex : '75015, FR', '1600 Amphitheatre Pkwy, Mountain View, CA 94043'", required = true) @PathVariable String natalAddress,
			@ApiParam(value = "Date from which to calculate the revolution. ISO Date format, ex : 2018-01-22", required = true) @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String fromDate,
			@ApiParam(value = "Revolution moment location. Ex : '75015, FR', '1600 Amphitheatre Pkwy, Mountain View, CA 94043'", required = true) @PathVariable String anniversaryAddress) throws GeolocException {

		GeoResult natalGeo = queryForGeoloc(natalAddress);

		SkyPosition natalTheme = getNatalTheme(natalDate,
											   natalGeo.getGeometry().getLocation().getLat(),
											   natalGeo.getGeometry().getLocation().getLng());

		GeoResult anniversaryGeo = queryForGeoloc(anniversaryAddress);

		return getSolarRevolutionTheme(fromDate,
									   anniversaryGeo.getGeometry().getLocation().getLat(),
									   anniversaryGeo.getGeometry().getLocation().getLng(), natalTheme);
	}

	@ApiOperation(value = "Solar revolution", produces = "application/json")
	@GetMapping(value = "/{natalDate}/{natalLatitude:.+}/{natalLongitude:.+}/revolution/solar/{fromDate}/date")
	public DateTimeJson getSolarRevolutionDate(
			@ApiParam(value = "Natal date and time. ISO Datetime format, ex : 2018-01-22T22:04:19", required = true) @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) String natalDate,
			@ApiParam(value = "Natal location latitude", required = true) @PathVariable double natalLatitude,
			@ApiParam(value = "Natal location longitude", required = true) @PathVariable double natalLongitude,
			@ApiParam(value = "Date from which to calculate the revolution. ISO Date format, ex : 2018-01-22", required = true) @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String fromDate) {

		return getSolarDateTimeJson(natalDate, natalLatitude, natalLongitude, fromDate);
	}

	@ApiOperation(value = "Solar revolution date", produces = "application/json")
	@GetMapping(value = "/{natalDate}/{address}/revolution/solar/{fromDate}/date")
	public DateTimeJson getSolarRevolutionDate(
			@ApiParam(value = "Natal date and time. ISO Datetime format, ex : 2018-01-22T22:04:19", required = true) @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) String natalDate,
			@ApiParam(value = "Natal location. Ex : '75015, FR', '1600 Amphitheatre Pkwy, Mountain View, CA 94043'", required = true) @PathVariable String address,
			@ApiParam(value = "Date from which to calculate the revolution. ISO Date format, ex : 2018-01-22", required = true) @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String fromDate)
			throws GeolocException {
		GeoResult geoResult = queryForGeoloc(address);
		double natalLatitude = geoResult.getGeometry().getLocation().getLat();
		double natalLongitude = geoResult.getGeometry().getLocation().getLng();
		return getSolarDateTimeJson(natalDate, natalLatitude, natalLongitude, fromDate);
	}

	@ApiOperation(value = "Lunar revolution", produces = "application/json")
	@GetMapping(value = "/{natalDate}/{natalLatitude:.+}/{natalLongitude:.+}/revolution/lunar/{fromDate}/{anniversaryLatitude:.+}/{anniversaryLongitude:.+}")
	public SkyPosition getLunarRevolution(
			@ApiParam(value = "Natal date and time. ISO Datetime format, ex : 2018-01-22T22:04:19", required = true) @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) String natalDate,
			@ApiParam(value = "Natal location latitude", required = true) @PathVariable double natalLatitude,
			@ApiParam(value = "Natal location longitude", required = true) @PathVariable double natalLongitude,
			@ApiParam(value = "Date from which to calculate the revolution. ISO Date format, ex : 2018-01-22", required = true) @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String fromDate,
			@ApiParam(value = "Revolution moment latitude location", required = true) @PathVariable double anniversaryLatitude,
			@ApiParam(value = "Revolution moment longitude location", required = true) @PathVariable double anniversaryLongitude) {
		SkyPosition natalTheme = getNatalTheme(natalDate, natalLatitude, natalLongitude);

		return getLunarRevolutionTheme(fromDate, anniversaryLatitude, anniversaryLongitude,
									   natalTheme);
	}

	@ApiOperation(value = "Lunar revolution", produces = "application/json")
	@GetMapping(value = "/{natalDate}/{natalAddress}/revolution/lunar/{fromDate}/{anniversaryLatitude:.+}/{anniversaryLongitude:.+}")
	public SkyPosition getLunarRevolutionWithNatalAddress(
			@ApiParam(value = "Natal date and time. ISO Datetime format, ex : 2018-01-22T22:04:19", required = true) @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) String natalDate,
			@ApiParam(value = "Natal location. Ex : '75015, FR', '1600 Amphitheatre Pkwy, Mountain View, CA 94043'", required = true) @PathVariable String natalAddress,
			@ApiParam(value = "Date from which to calculate the revolution. ISO Date format, ex : 2018-01-22", required = true) @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String fromDate,
			@ApiParam(value = "Revolution moment latitude location", required = true) @PathVariable double anniversaryLatitude,
			@ApiParam(value = "Revolution moment longitude location", required = true) @PathVariable double anniversaryLongitude) throws GeolocException {
		GeoResult natalGeo = queryForGeoloc(natalAddress);

		SkyPosition natalTheme = getNatalTheme(natalDate,
											   natalGeo.getGeometry().getLocation().getLat(),
											   natalGeo.getGeometry().getLocation().getLng());

		return getLunarRevolutionTheme(fromDate, anniversaryLatitude,
									   anniversaryLongitude, natalTheme);
	}

	@ApiOperation(value = "Lunar revolution", produces = "application/json")
	@GetMapping(value = "/{natalDate}/{natalLatitude:.+}/{natalLongitude:.+}/revolution/lunar/{fromDate}/{anniversaryAddress}")
	public SkyPosition getLunarRevolutionWithAnniversaryAddress(
			@ApiParam(value = "Natal date and time. ISO Datetime format, ex : 2018-01-22T22:04:19", required = true) @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) String natalDate,
			@ApiParam(value = "Natal location latitude", required = true) @PathVariable double natalLatitude,
			@ApiParam(value = "Natal location longitude", required = true) @PathVariable double natalLongitude,
			@ApiParam(value = "Date from which to calculate the revolution. ISO Date format, ex : 2018-01-22", required = true) @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String fromDate,
			@ApiParam(value = "Revolution moment location. Ex : '75015, FR', '1600 Amphitheatre Pkwy, Mountain View, CA 94043'", required = true) @PathVariable String anniversaryAddress) throws GeolocException {
		SkyPosition natalTheme = getNatalTheme(natalDate, natalLatitude, natalLongitude);

		GeoResult anniversaryGeo = queryForGeoloc(anniversaryAddress);

		return getLunarRevolutionTheme(fromDate, anniversaryGeo.getGeometry().getLocation().getLat(),
									   anniversaryGeo.getGeometry().getLocation().getLng(), natalTheme);
	}

	@ApiOperation(value = "Lunar revolution", produces = "application/json")
	@GetMapping(value = "/{natalDate}/{natalAddress}/revolution/lunar/{fromDate}/{anniversaryAddress}")
	public SkyPosition getLunarRevolutionWithBothAddress(
			@ApiParam(value = "Natal date and time. ISO Datetime format, ex : 2018-01-22T22:04:19", required = true) @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) String natalDate,
			@ApiParam(value = "Natal location. Ex : '75015, FR', '1600 Amphitheatre Pkwy, Mountain View, CA 94043'", required = true) @PathVariable String natalAddress,
			@ApiParam(value = "Date from which to calculate the revolution. ISO Date format, ex : 2018-01-22", required = true) @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String fromDate,
			@ApiParam(value = "Revolution moment location. Ex : '75015, FR', '1600 Amphitheatre Pkwy, Mountain View, CA 94043'", required = true) @PathVariable String anniversaryAddress) throws GeolocException {
		GeoResult natalGeo = queryForGeoloc(natalAddress);
		SkyPosition natalTheme = getNatalTheme(natalDate,
											   natalGeo.getGeometry().getLocation().getLat(),
											   natalGeo.getGeometry().getLocation().getLng());

		GeoResult anniversaryGeo = queryForGeoloc(anniversaryAddress);

		return getLunarRevolutionTheme(fromDate,
									   anniversaryGeo.getGeometry().getLocation().getLat(),
									   anniversaryGeo.getGeometry().getLocation().getLng(),
									   natalTheme);
	}

	@ApiOperation(value = "Lunar revolution date", produces = "application/json")
	@GetMapping(value = "/{natalDate}/{natalLatitude:.+}/{natalLongitude:.+}/revolution/lunar/{fromDate}/date")
	public DateTimeJson getLunarRevolutionDate(
			@ApiParam(value = "Natal date and time. ISO Datetime format, ex : 2018-01-22T22:04:19", required = true) @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) String natalDate,
			@ApiParam(value = "Natal location latitude", required = true) @PathVariable double natalLatitude,
			@ApiParam(value = "Natal location longitude", required = true) @PathVariable double natalLongitude,
			@ApiParam(value = "Date from which to calculate the revolution. ISO Date format, ex : 2018-01-22", required = true) @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String fromDate) {
		return getLunarDateTimeJson(natalDate, natalLatitude, natalLongitude, fromDate);
	}

	@ApiOperation(value = "Lunar revolution date", produces = "application/json")
	@GetMapping(value = "/{natalDate}/{address}/revolution/lunar/{fromDate}/date")
	public DateTimeJson getLunarRevolutionDateWithAddress(
			@ApiParam(value = "Natal date and time. ISO Datetime format, ex : 2018-01-22T22:04:19", required = true) @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) String natalDate,
			@ApiParam(value = "Natal location. Ex : '75015, FR', '1600 Amphitheatre Pkwy, Mountain View, CA 94043'", required = true) @PathVariable String address,
			@ApiParam(value = "Date from which to calculate the revolution. ISO Date format, ex : 2018-01-22", required = true) @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String fromDate)
			throws GeolocException {
		GeoResult geoResult = queryForGeoloc(address);
		double natalLatitude = geoResult.getGeometry().getLocation().getLat();
		double natalLongitude = geoResult.getGeometry().getLocation().getLng();

		return getLunarDateTimeJson(natalDate, natalLatitude, natalLongitude, fromDate);
	}
}
