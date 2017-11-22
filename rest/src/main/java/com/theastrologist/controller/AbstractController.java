package com.theastrologist.controller;

import com.theastrologist.controller.exception.ErrorResponse;
import com.theastrologist.controller.exception.NoResultsFoundException;
import com.theastrologist.controller.exception.NoResultsRestException;
import com.theastrologist.controller.exception.TooManyResultsRestException;
import com.theastrologist.domain.Degree;
import com.theastrologist.domain.SkyPosition;
import com.theastrologist.domain.user.User;
import com.theastrologist.external.GoogleRestException;
import com.theastrologist.external.geoloc.GeoResponse;
import com.theastrologist.external.geoloc.GeoResult;
import com.theastrologist.external.geoloc.GeolocException;
import com.theastrologist.external.geoloc.GeolocRestClient;
import com.theastrologist.service.ThemeService;
import com.theastrologist.service.UserService;
import com.theastrologist.util.TimeService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

/**
 * Created by Samy on 31/01/2016.
 */
public class AbstractController {
	@Autowired
	protected TimeService timeService;

	@Autowired
	protected ThemeService themeService;

	protected SkyPosition getSkyPosition(String datetime, double latitude, double longitude, String address) {
		DateTime parse = timeService.parseDateTime(datetime, latitude, longitude);
		Degree latitudeDegree = new Degree(latitude);
		Degree longitudeDegree = new Degree(longitude);
		SkyPosition skyPosition = themeService.getSkyPosition(parse, latitudeDegree, longitudeDegree);
		if (address != null) {
			skyPosition.setAddress(address);
		}
		return skyPosition;
	}

	@Autowired
	private UserService userService;

	public GeoResult queryForGeoloc(String address) throws GeolocException {
		GeolocRestClient geolocRestClient = new GeolocRestClient(address);
		GeoResponse response = geolocRestClient.getGeocoding();

		List<GeoResult> results = response.getResults();
		if(results.isEmpty()) {
			throw new NoResultsRestException();
		} else if(results.size() > 1) {
			throw new TooManyResultsRestException();
		}

		return results.get(0);
	}

	@ExceptionHandler(GeolocException.class)
	public ResponseEntity<ErrorResponse> exceptionHandler(Exception ex) {
		ErrorResponse error = new ErrorResponse();
		error.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		error.setMessage(ex.getMessage());
		return new ResponseEntity<ErrorResponse>(error, HttpStatus.OK);
	}

	public User getUser(String userName) throws NoResultsFoundException {
		User user = userService.getUser(userName);
		if(user == null) {
			throw new NoResultsFoundException();
		}
		return user;
	}
}
