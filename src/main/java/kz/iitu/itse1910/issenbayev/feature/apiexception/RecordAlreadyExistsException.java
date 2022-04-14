package kz.iitu.itse1910.issenbayev.feature.apiexception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;


@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RecordAlreadyExistsException extends ApiException {
    public RecordAlreadyExistsException(List<ApiExceptionDetailHolder> detailHolderList) {
        super(detailHolderList);
    }

    public RecordAlreadyExistsException(ApiExceptionDetailHolder detailHolder) {
        super(detailHolder);
    }
}
