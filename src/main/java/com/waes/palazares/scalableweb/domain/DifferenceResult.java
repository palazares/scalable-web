package com.waes.palazares.scalableweb.domain;
import lombok.*;

/**
 * Embeddable object used to store comparison result
 */
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
@EqualsAndHashCode
public class DifferenceResult {
    private DifferenceType type;

    private String message;
}
