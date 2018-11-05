package com.waes.palazares.scalableweb.domain;
import lombok.Data;
import lombok.NonNull;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@Embeddable
public class DifferenceResult {
    @NonNull
    @Enumerated(EnumType.STRING)
    @Column(length = 17)
    private final DifferenceType type;

    @NonNull
    private final String message;
}
