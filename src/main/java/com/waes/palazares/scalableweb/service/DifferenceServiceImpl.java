package com.waes.palazares.scalableweb.service;

import java.util.Arrays;
import java.util.Base64;

import org.springframework.stereotype.Service;

import com.waes.palazares.scalableweb.domain.DifferenceRecord;
import com.waes.palazares.scalableweb.domain.DifferenceResult;
import com.waes.palazares.scalableweb.domain.DifferenceType;
import com.waes.palazares.scalableweb.exception.InavlidIdException;
import com.waes.palazares.scalableweb.exception.InvalidBase64Exception;
import com.waes.palazares.scalableweb.exception.InvalidRecordContentException;
import com.waes.palazares.scalableweb.repository.DifferenceRepository;
import com.waes.palazares.scalableweb.utils.Offsets;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * Implementation of {@code DifferenceService} interface.
 * Integrates with CRUD storage, makes validation checks, builds difference results
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class DifferenceServiceImpl implements DifferenceService {
    private final DifferenceRepository repository;

    @Override
    public Mono<DifferenceRecord> putRight(String id, String doc) {
        return putRecord(id, doc, false);
    }

    @Override
    public Mono<DifferenceRecord> putLeft(String id, String doc) {
        return putRecord(id, doc, true);
    }

    /**
     * Checks that record already has a result and returns it without further processing if it already exists
     *
     * @param id document id
     * @return difference result
     */
    @Override
    public Mono<DifferenceRecord> getDifference(String id) {
        log.debug("Get difference request with id: {}", id);

        if (id == null || id.trim().isEmpty()) {
            log.debug("Get difference request has empty id");
            return Mono.error(new InavlidIdException());
        }

        var record = repository.findById(id).switchIfEmpty(Mono.error(new InvalidRecordContentException()));
        var yesResultRecord = record.filter(r -> r.getResult() != null);

        return yesResultRecord
                .switchIfEmpty(record
                        .flatMap(rec -> compare(rec).map(x -> rec.toBuilder().result(x).build()))
                        .flatMap(repository::save));
    }

    /**
     * Nullify result only if new content is different from already existing. Otherwise old object is returned and no actual persistence performed
     *
     * @param id document id
     * @param doc base64 encoded document
     * @param isLeft document side
     * @return persisted difference record
     */
    private Mono<DifferenceRecord> putRecord(String id, String doc, boolean isLeft) {
        log.debug("Put record request with id: {}", id);

        if (id == null || id.trim().isEmpty()) {
            log.debug("Record request has empty id");
            return Mono.error(new InavlidIdException());
        }

        if (doc == null || doc.trim().isEmpty()) {
            log.debug("Record request with id: {} has empty content", id);
            return Mono.error(new InvalidBase64Exception());
        }

        var decodedDoc = decode(doc);
        var record = repository.findById(id).defaultIfEmpty(DifferenceRecord.builder().id(id).build());

        var sameDocRecord = decodedDoc.flatMap(d ->
                record.filter(rec ->  Arrays.equals((isLeft ? rec.getLeft() : rec.getRight()), d)));

        return sameDocRecord.switchIfEmpty(
                decodedDoc.flatMap(d -> record
                        .map(rec -> isLeft ? rec.toBuilder().left(d).build() : rec.toBuilder().right(d).build()))
                        .map(rec -> rec.toBuilder().result(null).build())
                        .flatMap(repository::save));
    }

    private Mono<byte[]> decode(String doc) {
        return Mono.just(doc)
                .map(d -> Base64.getDecoder().decode(d))
                .doOnError(ex -> log.debug("Not valid base64 string: $doc", ex))
                .onErrorMap(ex -> new InvalidBase64Exception());
    }

    private Mono<DifferenceResult> compare(DifferenceRecord record) {
        if (record.getLeft() == null || record.getRight() == null || record.getLeft().length < 1 || record.getRight().length < 1) {
            log.debug("Record with id: {} doesn't have full date for comparison", record.getId());
            return Mono.error(new InvalidRecordContentException());
        }

        var left = record.getLeft();
        var right = record.getRight();

        if (Arrays.equals(left, right)) {
            log.debug("Record with id: {} has equal content", record.getId());
            return Mono.just(DifferenceResult.builder().type(DifferenceType.EQUALS).message("Records are equal. Congratulations!").build());
        }

        if (left.length != right.length) {
            log.debug("Record with id: {} has different size content", record.getId());
            return Mono.just(DifferenceResult.builder().type(DifferenceType.DIFFERENT_SIZE).message("Records have different size. What a pity!").build());
        }

        log.debug("Record with id: {} has different content", record.getId());
        return Mono.just(DifferenceResult.builder()
                .type(DifferenceType.DIFFERENT_CONTENT)
                .message("Records have the same size, but content is different. Differences insight: " + Offsets.getOffsetsMessage(left, right))
                .build());
    }
}
