package com.waes.palazares.scalableweb.repository;

import com.waes.palazares.scalableweb.domain.DifferenceRecord;
import org.springframework.data.repository.CrudRepository;

/**
 * Simple CRUD repository interface used to store differences
 */
public interface DifferenceRepository extends CrudRepository<DifferenceRecord, String> {
}
