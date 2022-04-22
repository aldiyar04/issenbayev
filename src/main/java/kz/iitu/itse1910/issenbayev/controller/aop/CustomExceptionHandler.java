package kz.iitu.itse1910.issenbayev.controller.aop;

import kz.iitu.itse1910.issenbayev.dto.ErrorResponse;
import kz.iitu.itse1910.issenbayev.feature.apiexception.ApiException;
import kz.iitu.itse1910.issenbayev.feature.apiexception.RecordAlreadyExistsException;
import kz.iitu.itse1910.issenbayev.feature.apiexception.RecordNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
@SuppressWarnings({"unchecked","rawtypes"})
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
        ex.printStackTrace();
        ErrorResponse error = new ErrorResponse("Server Error", ex.getLocalizedMessage());
        return new ResponseEntity(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({ApiException.class})
    public final ResponseEntity<Object> handleApiExceptions(ApiException ex, WebRequest request) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        String message;
        if (ex instanceof RecordNotFoundException) {
            httpStatus = HttpStatus.NOT_FOUND;
            message = "Record Not Found";
        } else if (ex instanceof RecordAlreadyExistsException) {
            message = "Record Already Exists";
        }
//        else if (ex instanceof IncorrectPasswordException) {
//            message = "Provided password is incorrect";
//        }
        else {
            message = "Invalid use of API";
        }
        ErrorResponse error = new ErrorResponse(message, ex.getDetailHolders());
        return new ResponseEntity(error, httpStatus);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
        List<String> details = new ArrayList<>();
        for (ObjectError error : ex.getBindingResult().getAllErrors()) {
            details.add(error.getDefaultMessage());
        }
        ErrorResponse error = new ErrorResponse(details, "Validation Failed");
        return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
    }
}
