package com.waes.palazares.scalableweb.service;

import com.waes.palazares.scalableweb.domain.DifferenceRecord;

public interface DifferenceService {
    void putRight(String id, String doc);
    void putLeft(String id, String doc);
    DifferenceRecord getDifference(String id);
}
