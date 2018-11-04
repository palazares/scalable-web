package com.waes.palazares.scalableweb.service;

import com.waes.palazares.scalableweb.domain.DifferenceRecord;
import com.waes.palazares.scalableweb.exception.InavlidIdException;
import com.waes.palazares.scalableweb.exception.InvalidBase64Exception;
import com.waes.palazares.scalableweb.exception.InvalidRecordContentException;

public interface DifferenceService {
    DifferenceRecord putRight(String id, String doc) throws InavlidIdException, InvalidBase64Exception;
    DifferenceRecord putLeft(String id, String doc) throws InavlidIdException, InvalidBase64Exception;
    DifferenceRecord getDifference(String id) throws InavlidIdException, InvalidRecordContentException;
}
