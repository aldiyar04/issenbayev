package kz.iitu.itse1910.issenbayev.feature.exception.apiexception;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ApiExceptionDetailHolder {
    private final String field;
    private final String message;
}
