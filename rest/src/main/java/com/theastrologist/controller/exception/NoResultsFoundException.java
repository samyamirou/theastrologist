package com.theastrologist.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by SAM on 04/08/2015.
 */
@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="No result found")
public class NoResultsFoundException extends RuntimeException {
}
