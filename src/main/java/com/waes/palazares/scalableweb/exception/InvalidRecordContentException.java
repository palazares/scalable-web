package com.waes.palazares.scalableweb.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when difference record was not found or it's in partial state having only one valid side
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Record is not found or exists only in partial state")
public class InvalidRecordContentException extends Exception {
}
