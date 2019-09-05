package com.waes.palazares.scalableweb.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.waes.palazares.scalableweb.domain.DifferenceRecord;

/**
 * MongoDB repository  used to store differences
 */
public interface DifferenceRepository extends ReactiveCrudRepository<DifferenceRecord, String> {
}
