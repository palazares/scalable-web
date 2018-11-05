package com.waes.palazares.scalableweb.domain;

import lombok.*;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Entity object used to store document id, left and right side and last result if both sides were unchanged
 */
@Entity
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
@EqualsAndHashCode
public class DifferenceRecord {
    @Id
    private String id;

    private byte[] left;

    private byte[] right;

    @Embedded
    private DifferenceResult result;
}
