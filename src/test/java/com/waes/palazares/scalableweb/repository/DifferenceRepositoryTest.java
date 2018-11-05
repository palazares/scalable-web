package com.waes.palazares.scalableweb.repository;

import com.waes.palazares.scalableweb.domain.DifferenceRecord;
import com.waes.palazares.scalableweb.domain.DifferenceResult;
import com.waes.palazares.scalableweb.domain.DifferenceType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@DataMongoTest
public class DifferenceRepositoryTest {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private DifferenceRepository repository;

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
        repository.save(sample);
        // assert
        DifferenceRecord result = mongoTemplate.findById("testId", DifferenceRecord.class);
        assertNotNull(result);
        assertEquals("testId", result.getId());
        assertArrayEquals("leftContent".getBytes(), result.getLeft());
        assertArrayEquals("rightContent".getBytes(), result.getRight());
        assertEquals(DifferenceType.DIFFERENT_SIZE, result.getResult().getType());
        assertEquals("testMessage", result.getResult().getMessage());
    }
}