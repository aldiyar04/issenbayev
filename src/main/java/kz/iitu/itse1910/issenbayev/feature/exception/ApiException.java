package kz.iitu.itse1910.issenbayev.feature.exception;

public class ApiException extends RuntimeException {
    public ApiException(String message) {
        super(message);
    }
}
