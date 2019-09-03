package com.waes.palazares.scalableweb.service;

import com.waes.palazares.scalableweb.domain.DifferenceRecord;
import com.waes.palazares.scalableweb.exception.InavlidIdException;
import com.waes.palazares.scalableweb.exception.InvalidBase64Exception;
import com.waes.palazares.scalableweb.exception.InvalidRecordContentException;

import reactor.core.publisher.Mono;

/**
 * {@code DifferenceService} interface defines methods to store left and right documents and for getting difference between them
 */
public interface DifferenceService {
    /**
     * Puts document as a right side of the difference into the repository. Document contents are decoded
     *
     * @param id document id
     * @param doc base64 encoded document
     * @return persisted difference record
     * @throws InavlidIdException when id is empty or null
     * @throws InvalidBase64Exception when content is not valid base64 string
     */
    Mono<DifferenceRecord> putRight(String id, String doc) throws InavlidIdException, InvalidBase64Exception;
    /**
     * Puts document as a left side of the difference into the repository. Document contents are decoded
     *
     * @param id document id
     * @param doc base64 encoded document
     * @return persisted difference record
     * @throws InavlidIdException when id is empty or null
     * @throws InvalidBase64Exception when content is not valid base64 string
     */
    Mono<DifferenceRecord> putLeft(String id, String doc) throws InavlidIdException, InvalidBase64Exception;
    /**
     * Gets the difference between left and right documents
     *
     * @param id document id
     * @return difference record with a result
     * @throws InavlidIdException when id is empty or null
     * @throws InvalidRecordContentException when can't find record with provided id, only one side has been stored so far or record content is empty
     */
    Mono<DifferenceRecord> getDifference(String id) throws InavlidIdException, InvalidRecordContentException;
}
