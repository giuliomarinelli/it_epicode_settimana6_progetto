package it.epicode.w6d5.devices_management.exceptions;

import it.epicode.w6d5.devices_management.Models.resDTO.HttpErrorRes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;

@ControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<HttpErrorRes> badRequestHandler(BadRequestException e) {
        return new ResponseEntity<>(new HttpErrorRes(HttpStatus.BAD_REQUEST,
                "Bad request", e.getMessage()), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<HttpErrorRes> notFoundHandler(NotFoundException e) {
        return new ResponseEntity<>(new HttpErrorRes(HttpStatus.NOT_FOUND,
                "Not found", e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<HttpErrorRes> ioExceptionHandler(IOException e) {
        return new ResponseEntity<>(new HttpErrorRes(HttpStatus.SERVICE_UNAVAILABLE,
                "Service unavailable", "An input/output error with Cloudinary provider occurred" +
                " during file upload. " + e.getMessage()), HttpStatus.SERVICE_UNAVAILABLE);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<HttpErrorRes> genericExceptionHandler(Exception e) {
        return new ResponseEntity<>(new HttpErrorRes(HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal server error", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
