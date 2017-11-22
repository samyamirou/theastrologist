package com.theastrologist.controller;

import com.theastrologist.controller.exception.IndividualAlreadyExistsRestException;
import com.theastrologist.controller.exception.NoResultsFoundException;
import com.theastrologist.controller.exception.TooManyResultsRestException;
import com.theastrologist.controller.exception.UserAlreadyExistsRestException;
import com.theastrologist.domain.SkyPosition;
import com.theastrologist.domain.individual.Individual;
import com.theastrologist.domain.user.User;
import com.theastrologist.exception.IndividualAlreadyExistsException;
import com.theastrologist.exception.TooManyResultsException;
import com.theastrologist.exception.UserAlreadyExistsException;
import com.theastrologist.service.IndividualService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@Api(value = "/user/{userName}/individual", tags = "Individuals", description = "Individuals")
public class IndividualController extends AbstractController {
    @Autowired
    private IndividualService individualService;

    @ApiOperation(value = "Find individual by name", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Individual not found"),
            @ApiResponse(code = 404, message = "User not found"),
            @ApiResponse(code = 400, message = "Too many individuals found")
    })
    @GetMapping(value = "/user/{userName}/individual/{individualName}")
    public Individual getIndividual(
            @ApiParam(value = "User Name", required = true) @PathVariable String userName,
            @ApiParam(value = "Individual Name", required = true) @PathVariable String individualName) throws NoResultsFoundException {
        User user = getUser(userName);

        Individual individual = null;
        try {
            individual = individualService.findIndividualByName(user, individualName);
        } catch (TooManyResultsException e) {
            throw new TooManyResultsRestException();
        }

        if (individual == null) {
            throw new NoResultsFoundException();
        }
        return individual;
    }

    @ApiOperation(value = "Create individual", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Individual not found"),
            @ApiResponse(code = 404, message = "User not found"),
            @ApiResponse(code = 400, message = "Too many individuals found")
    })
    @PostMapping(value = "/user/{userName}/individual/{individualName}/{datetime}/{latitude:.+}/{longitude:.+}")
    public ResponseEntity<Void> createIndividual(
            @ApiParam(value = "User Name", required = true) @PathVariable String userName,
            @ApiParam(value = "Individual Name", required = true) @PathVariable String individualName,
            @ApiParam(value = "Theme date and time. ISO Datetime format, ex : 2018-01-22T22:04:19", required = true) @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) String datetime,
            @ApiParam(value = "Theme location latitude", required = true) @PathVariable double latitude,
            @ApiParam(value = "Theme location longitude", required = true) @PathVariable double longitude) throws NoResultsFoundException, IndividualAlreadyExistsRestException {
        User user = getUser(userName);

        Individual individual;

		SkyPosition skyPosition = getSkyPosition(datetime, latitude, longitude, null);
		try {
            individual = individualService.createIndividual(user, individualName, skyPosition);
        } catch (IndividualAlreadyExistsException e) {
            throw new IndividualAlreadyExistsRestException();
        }

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand(user.getUserName()).toUri();

        return ResponseEntity.created(location).build();
    }
}
