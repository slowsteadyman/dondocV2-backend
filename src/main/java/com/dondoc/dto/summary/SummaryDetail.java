package com.dondoc.dto.summary;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SummaryDetail {
    private final SummaryCategory category;
    private final Long amount;
    private final Integer ratio;
}
