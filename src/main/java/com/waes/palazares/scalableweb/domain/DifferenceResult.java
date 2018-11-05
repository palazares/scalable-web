package com.waes.palazares.scalableweb.domain;
import lombok.*;

import javax.persistence.*;

@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
@Embeddable
public class DifferenceResult {
    @Enumerated(EnumType.STRING)
    @Column(length = 17)
    private DifferenceType type;

    private String message;
}
