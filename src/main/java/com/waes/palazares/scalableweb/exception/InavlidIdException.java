package com.waes.palazares.scalableweb.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Invalid entity Id")
public class InavlidIdException extends Exception {
}
