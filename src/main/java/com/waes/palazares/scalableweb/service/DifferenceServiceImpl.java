package com.waes.palazares.scalableweb.service;

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
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Base64;

@Service
@Slf4j
@RequiredArgsConstructor
public class DifferenceServiceImpl implements DifferenceService {
    private final DifferenceRepository repository;

    @Override
    public DifferenceRecord putRight(String id, String doc) throws InavlidIdException, InvalidBase64Exception {
        return putRecord(id, doc, false);
    }

    @Override
    public DifferenceRecord putLeft(String id, String doc) throws InavlidIdException, InvalidBase64Exception {
        return putRecord(id, doc, true);
    }

    @Override
    public DifferenceRecord getDifference(String id) throws InvalidRecordContentException, InavlidIdException {
        log.debug("Get difference request with id: {}", id);

        if (id == null || id.trim().isEmpty()) {
            log.debug("Get difference request has empty id");
            throw new InavlidIdException();
        }

        DifferenceRecord record = repository.findById(id).orElseThrow(InvalidRecordContentException::new);

        //No need to compare and save again if we already have a Result
        if(record.getResult() == null) {
            log.debug("Processing result for the record with id: {}", id);
            record.setResult(compare(record));
            return repository.save(record);
        }

        log.debug("Record with id: {} already has result: {}", id, record.getResult().getType());
        return record;
    }

    private DifferenceRecord putRecord(String id, String doc, boolean isLeft) throws InavlidIdException, InvalidBase64Exception {
        log.debug("Put record request with id: {}", id);

        if (id == null || id.trim().isEmpty()) {
            log.debug("Record request has empty id");
            throw new InavlidIdException();
        }

        if (doc == null || doc.trim().isEmpty()) {
            log.debug("Record request with id: {} has empty content", id);
            throw new InvalidBase64Exception();
        }

        byte[] decodedDoc = decode(doc);

        DifferenceRecord record = repository.findById(id).orElse(new DifferenceRecord(id));

        byte[] oldDoc;
        if(isLeft){
            oldDoc = record.getLeft();
            record.setLeft(decodedDoc);
        }
        else{
            oldDoc = record.getRight();
            record.setRight(decodedDoc);
        }

        //Nullify result and save new record if it differs from old one
        if(!Arrays.equals(oldDoc, decodedDoc)){
            log.debug("Nullifying result and saving record with id: {}", id);
            record.setResult(null);
            return repository.save(record);
        }

        log.debug("Record was unchanged after request with id: {}. Content is the same", id);
        return record;
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

        byte[] left = record.getLeft();
        byte[] right = record.getRight();

        if (Arrays.equals(left, right)) {
            log.debug("Record with id: {} has equal content", record.getId());
            return new DifferenceResult(DifferenceType.EQUALS, "Records are equal. Congratulations!");
        }

        if (left.length != right.length) {
            log.debug("Record with id: {} has different size content", record.getId());
            return new DifferenceResult(DifferenceType.DIFFERENT_SIZE, "Records have different size. What a pity!");
        }

        log.debug("Record with id: {} has different content", record.getId());
        return new DifferenceResult(DifferenceType.DIFFERENT_CONTENT,"Records have the same size, but content is different. Differences insight: " +
                Offsets.getOffsetsMessage(left, right));
    }
}
