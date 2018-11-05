package com.waes.palazares.scalableweb.repository;

import com.waes.palazares.scalableweb.domain.DifferenceRecord;
import org.springframework.data.repository.CrudRepository;

public interface DifferenceRepository extends CrudRepository<DifferenceRecord, String> {
}
