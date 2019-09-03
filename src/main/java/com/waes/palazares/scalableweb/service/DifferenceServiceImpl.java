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
    public Mono<DifferenceRecord> putRight(String id, String doc) throws InavlidIdException, InvalidBase64Exception {
        return putRecord(id, doc, false);
    }

    @Override
    public Mono<DifferenceRecord> putLeft(String id, String doc) throws InavlidIdException, InvalidBase64Exception {
        return putRecord(id, doc, true);
    }

    /**
     * Checks that record already has a result and returns it without further processing if it already exists
     *
     * @param id document id
     * @return difference result
     * @throws InavlidIdException when id is empty or null
     * @throws InvalidRecordContentException when can't find record with provided id, only one side has been stored so far or record content is empty
     */
    @Override
    public Mono<DifferenceRecord> getDifference(String id) throws InvalidRecordContentException, InavlidIdException {
        log.debug("Get difference request with id: {}", id);

        if (id == null || id.trim().isEmpty()) {
            log.debug("Get difference request has empty id");
            throw new InavlidIdException();
        }

        var record = repository.findById(id).orElseThrow(InvalidRecordContentException::new);

        //No need to compare and save again if we already have a Result
        if(record.getResult() == null) {
            log.debug("Processing result for the record with id: {}", id);
            return Mono.justOrEmpty(repository.save(record.toBuilder().result(compare(record)).build()));
        }

        log.debug("Record with id: {} already has result: {}", id, record.getResult().getType());
        return Mono.just(record);
    }

    /**
     * Nullify result only if new content is different from already existing. Otherwise old object is returned and no actual persistence performed
     *
     * @param id document id
     * @param doc base64 encoded document
     * @param isLeft document side
     * @return persisted difference record
     * @throws InavlidIdException when id is empty or null
     * @throws InvalidBase64Exception when content is not valid base64 string
     */
    private Mono<DifferenceRecord> putRecord(String id, String doc, boolean isLeft) throws InavlidIdException, InvalidBase64Exception {
        log.debug("Put record request with id: {}", id);

        if (id == null || id.trim().isEmpty()) {
            log.debug("Record request has empty id");
            throw new InavlidIdException();
        }

        if (doc == null || doc.trim().isEmpty()) {
            log.debug("Record request with id: {} has empty content", id);
            throw new InvalidBase64Exception();
        }

        var decodedDoc = decode(doc);

        DifferenceRecord record = repository.findById(id).orElse(DifferenceRecord.builder().id(id).build());

        byte[] oldDoc;
        if(isLeft){
            oldDoc = record.getLeft();
            record = record.toBuilder().left(decodedDoc).build();
        }
        else{
            oldDoc = record.getRight();
            record = record.toBuilder().right(decodedDoc).build();
        }

        //Nullify result and save new record if it differs from old one
        if(!Arrays.equals(oldDoc, decodedDoc)){
            log.debug("Nullifying result and saving record with id: {}", id);
            return Mono.justOrEmpty(repository.save(record.toBuilder().result(null).build()));
        }

        log.debug("Record was unchanged after request with id: {}. Content is the same", id);
        return Mono.just(record);
    }

    private byte[] decode(String doc) throws InvalidBase64Exception {
        try {
            return Base64.getDecoder().decode(doc);
        } catch (IllegalArgumentException e) {
            log.debug("Not valid base64 string: {}", doc, e);
            throw new InvalidBase64Exception();
        }
    }

    private DifferenceResult compare(DifferenceRecord record) throws InvalidRecordContentException {
        if (record.getLeft() == null || record.getRight() == null || record.getLeft().length < 1 || record.getRight().length < 1) {
            log.debug("Record with id: {} doesn't have full date for comparison", record.getId());
            throw new InvalidRecordContentException();
        }

        var left = record.getLeft();
        var right = record.getRight();

        if (Arrays.equals(left, right)) {
            log.debug("Record with id: {} has equal content", record.getId());
            return DifferenceResult.builder().type(DifferenceType.EQUALS).message("Records are equal. Congratulations!").build();
        }

        if (left.length != right.length) {
            log.debug("Record with id: {} has different size content", record.getId());
            return DifferenceResult.builder().type(DifferenceType.DIFFERENT_SIZE).message("Records have different size. What a pity!").build();
        }

        log.debug("Record with id: {} has different content", record.getId());
        return DifferenceResult.builder()
                .type(DifferenceType.DIFFERENT_CONTENT)
                .message("Records have the same size, but content is different. Differences insight: " + Offsets.getOffsetsMessage(left, right))
                .build();
    }
}
