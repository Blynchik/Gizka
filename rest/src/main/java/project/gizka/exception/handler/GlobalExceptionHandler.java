package project.gizka.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import project.gizka.exception.notFound.AppUserNotFoundException;
import project.gizka.exception.response.ExceptionResponse;
import project.gizka.exception.validation.AppUserValidationException;

import java.time.LocalDateTime;
import java.util.Collections;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    private ResponseEntity<ExceptionResponse> handleException(AppUserNotFoundException e){
        ExceptionResponse response = new ExceptionResponse(Collections.singletonList(e.getMessage()), LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<?> handleException(AppUserValidationException e){
        ExceptionResponse response = new ExceptionResponse(e.getErrorMessages(), LocalDateTime.now());
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }
}
