package com.waes.palazares.scalableweb.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when provided content is not a valid base64 string
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Invalid base64 string")
public class InvalidBase64Exception extends Exception {
}
