package kz.iitu.itse1910.issenbayev.feature.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RecordAlreadyExistsException extends ApiException {
    public RecordAlreadyExistsException(String message) {
        super(message);
    }
}
