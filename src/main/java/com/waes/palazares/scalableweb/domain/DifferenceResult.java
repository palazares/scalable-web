package com.waes.palazares.scalableweb.domain;
import lombok.Data;
import lombok.NonNull;

@Data
public class DifferenceResult {
    @NonNull
    private final DifferenceType type;

    @NonNull
    private final String message;
}
