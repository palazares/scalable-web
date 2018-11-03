package com.waes.palazares.scalableweb.service;

import com.waes.palazares.scalableweb.domain.DifferenceRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DifferenceServiceImpl implements DifferenceService {

    private final CrudRepository<String, DifferenceRecord> repository;

    public DifferenceServiceImpl(CrudRepository<String, DifferenceRecord> repository) {
        this.repository = repository;
    }

    @Override
    public void putRight(String id, String doc) {

    }

    @Override
    public void putLeft(String id, String doc) {

    }

    @Override
    public DifferenceRecord getDifference(String id) {
        return null;
    }
}
