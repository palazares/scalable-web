package com.waes.palazares.scalableweb.service;

import com.waes.palazares.scalableweb.domain.DifferenceRecord;
import com.waes.palazares.scalableweb.domain.DifferenceResult;
import com.waes.palazares.scalableweb.domain.DifferenceType;
import com.waes.palazares.scalableweb.exception.InavlidIdException;
import com.waes.palazares.scalableweb.exception.InvalidBase64Exception;
import com.waes.palazares.scalableweb.exception.InvalidRecordContentException;
import com.waes.palazares.scalableweb.utils.Offsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Base64;

@Service
@Slf4j
@RequiredArgsConstructor
public class DifferenceServiceImpl implements DifferenceService {
    private final CrudRepository<DifferenceRecord, String> repository;

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
            throw new InavlidIdException();
        }

        DifferenceRecord record = repository.findById(id).orElseThrow(InvalidRecordContentException::new);
        record.setResult(compare(record));
        return repository.save(record);
    }

    private DifferenceRecord putRecord(String id, String doc, boolean isLeft) throws InavlidIdException, InvalidBase64Exception {
        log.debug("Put record request with id: {}", id);

        if (id == null || id.trim().isEmpty()) {
            throw new InavlidIdException();
        }

        if (doc == null || doc.trim().isEmpty()) {
            throw new InvalidBase64Exception();
        }

        byte[] decodedDoc = decode(doc);

        DifferenceRecord record = repository.findById(id).orElse(new DifferenceRecord(id));

        if(isLeft){
            record.setLeft(decodedDoc);
        }
        else{
            record.setRight(decodedDoc);
        }

        return repository.save(record);
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
            throw new InvalidRecordContentException();
        }

        byte[] left = record.getLeft();
        byte[] right = record.getRight();

        if (Arrays.equals(left, right)) {
            return new DifferenceResult(DifferenceType.EQUALS, "Records are equal. Congratulations!");
        }

        if (left.length != right.length) {
            return new DifferenceResult(DifferenceType.DIFFERENT_SIZE, "Records have different size. What a pity!");
        }

        return new DifferenceResult(DifferenceType.DIFFERENT_CONTENT,"Records have the same size, but there are differences. Here they are: " +
                Offsets.getOffsetsMessage(left, right));
    }
}
