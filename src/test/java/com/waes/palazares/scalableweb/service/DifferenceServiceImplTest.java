package com.waes.palazares.scalableweb.service;

import com.waes.palazares.scalableweb.domain.DifferenceRecord;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.repository.CrudRepository;

@RunWith(MockitoJUnitRunner.class)
public class DifferenceServiceImplTest {
    @InjectMocks
    private DifferenceService diffService;

    @Mock
    private CrudRepository<DifferenceRecord, String> repository;
}