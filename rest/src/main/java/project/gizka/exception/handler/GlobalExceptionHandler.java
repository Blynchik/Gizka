package project.gizka.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import project.gizka.exception.notFound.AppUserNotFoundException;
import project.gizka.exception.notFound.NotFoundException;
import project.gizka.exception.response.ExceptionResponse;
import project.gizka.exception.validation.AppUserValidationException;
import project.gizka.exception.validation.ValidationException;

import java.time.LocalDateTime;
import java.util.Collections;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    private ResponseEntity<ExceptionResponse> handleException(NotFoundException e){
        ExceptionResponse response = new ExceptionResponse(Collections.singletonList(e.getMessage()), LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<?> handleException(ValidationException e){
        ExceptionResponse response = new ExceptionResponse(Collections.singletonList(e.getMessage()), LocalDateTime.now());
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }
}
