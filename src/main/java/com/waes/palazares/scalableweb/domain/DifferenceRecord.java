package com.waes.palazares.scalableweb.domain;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Entity object used to store document id, left and right side and last result if both sides were unchanged
 */
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
@EqualsAndHashCode
@Document(collection = "difference")
public class DifferenceRecord {
    private String id;

    private byte[] left;

    private byte[] right;

    private DifferenceResult result;
}
