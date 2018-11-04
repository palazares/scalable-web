package com.waes.palazares.scalableweb.domain;

import lombok.Data;
import lombok.NonNull;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class DifferenceRecord {
    @NonNull
    @Id
    private final String id;

    private byte[] left;

    private byte[] right;

    private DifferenceResult result;
}
