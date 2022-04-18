package kz.iitu.itse1910.issenbayev.kafka.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

// Used in ProjectPerformanceService and ProjectPerformanceController
@Builder
@Getter
@ToString
public class ProjectPerformanceResponse {
    private final String name;
    private final int performance;
}
