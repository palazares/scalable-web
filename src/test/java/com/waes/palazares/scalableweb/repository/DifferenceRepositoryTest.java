package com.waes.palazares.scalableweb.repository;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.time.Duration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import com.waes.palazares.scalableweb.domain.DifferenceRecord;
import com.waes.palazares.scalableweb.domain.DifferenceResult;
import com.waes.palazares.scalableweb.domain.DifferenceType;
import com.waes.palazares.scalableweb.service.DifferenceService;

@RunWith(SpringRunner.class)
@DataMongoTest
public class DifferenceRepositoryTest {
    @Autowired
    private ReactiveMongoTemplate mongoTemplate;

    @Autowired
    private DifferenceRepository repository;

    @MockBean
    private DifferenceService differenceService;

    @Test
    public void shouldReturnSaved() {
        // arrange
        DifferenceRecord sample = DifferenceRecord.builder()
                .id("testId")
                .left("leftContent".getBytes())
                .right("rightContent".getBytes())
                .result(DifferenceResult.builder().type(DifferenceType.DIFFERENT_SIZE).message("testMessage").build())
                .build();
        // act
        repository.save(sample).block(Duration.ofSeconds(30));
        // assert
        DifferenceRecord result = mongoTemplate.findById("testId", DifferenceRecord.class).block();
        assertNotNull(result);
        assertEquals("testId", result.getId());
        assertArrayEquals("leftContent".getBytes(), result.getLeft());
        assertArrayEquals("rightContent".getBytes(), result.getRight());
        assertEquals(DifferenceType.DIFFERENT_SIZE, result.getResult().getType());
        assertEquals("testMessage", result.getResult().getMessage());
    }
}