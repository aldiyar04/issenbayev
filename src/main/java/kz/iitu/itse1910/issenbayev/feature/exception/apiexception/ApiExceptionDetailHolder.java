package kz.iitu.itse1910.issenbayev.feature.exception.apiexception;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class ApiExceptionDetailHolder {
    private final String field;
    private final String message;
}
