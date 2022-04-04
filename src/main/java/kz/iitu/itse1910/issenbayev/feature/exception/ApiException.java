package kz.iitu.itse1910.issenbayev.feature.exception;

import lombok.Getter;

import java.util.Collections;
import java.util.List;

@Getter
public class ApiException extends RuntimeException {
    private final List<ApiExceptionDetailHolder> detailHolders;

    public ApiException(List<ApiExceptionDetailHolder> detailHolders) {
        this.detailHolders = detailHolders;
    }

    public ApiException(ApiExceptionDetailHolder detailHolder) {
        this.detailHolders = Collections.singletonList(detailHolder);
    }
}
