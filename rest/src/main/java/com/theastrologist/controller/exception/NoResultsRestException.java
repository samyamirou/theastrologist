package com.theastrologist.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Samy on 21/05/2017.
 */
@ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="No result returned by Google API")
public class NoResultsRestException extends RuntimeException {
}
