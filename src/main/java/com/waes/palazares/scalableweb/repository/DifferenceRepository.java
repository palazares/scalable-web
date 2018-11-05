package com.waes.palazares.scalableweb.repository;

import com.waes.palazares.scalableweb.domain.DifferenceRecord;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * MongoDB repository  used to store differences
 */
public interface DifferenceRepository extends MongoRepository<DifferenceRecord, String> {
}
