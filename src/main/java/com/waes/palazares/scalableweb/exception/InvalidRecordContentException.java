package com.waes.palazares.scalableweb.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Record is not found or exists only in partial state")
public class InvalidRecordContentException extends Exception {
}
