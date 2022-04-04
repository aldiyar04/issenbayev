package kz.iitu.itse1910.issenbayev.feature.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RecordNotFoundException extends ApiException {
    public RecordNotFoundException(List<ApiExceptionDetailHolder> detailHolderList) {
        super(detailHolderList);
    }

    public RecordNotFoundException(ApiExceptionDetailHolder detailHolder) {
        super(detailHolder);
    }
}
