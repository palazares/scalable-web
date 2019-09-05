package com.waes.palazares.scalableweb.service;

import com.waes.palazares.scalableweb.domain.DifferenceRecord;

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
     */
    Mono<DifferenceRecord> putRight(String id, String doc);
    /**
     * Puts document as a left side of the difference into the repository. Document contents are decoded
     *
     * @param id document id
     * @param doc base64 encoded document
     * @return persisted difference record
     */
    Mono<DifferenceRecord> putLeft(String id, String doc);
    /**
     * Gets the difference between left and right documents
     *
     * @param id document id
     * @return difference record with a result
     */
    Mono<DifferenceRecord> getDifference(String id);
}
