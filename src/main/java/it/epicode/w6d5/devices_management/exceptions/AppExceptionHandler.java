package it.epicode.w6d5.devices_management.exceptions;

import it.epicode.w6d5.devices_management.Models.resDTO.HttpErrorRes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
@ControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<HttpErrorRes> badRequestHandler(BadRequestException e) {
        return new ResponseEntity<HttpErrorRes>(new HttpErrorRes(HttpStatus.BAD_REQUEST,
                "Bad request", e.getMessage()), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<HttpErrorRes> notFoundHandler(NotFoundException e) {
        return new ResponseEntity<HttpErrorRes>(new HttpErrorRes(HttpStatus.NOT_FOUND,
                "Not found", e.getMessage()), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<HttpErrorRes> internalServerErrorHandler(InternalServerErrorException e) {
        return new ResponseEntity<HttpErrorRes>(new HttpErrorRes(HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal server error", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<HttpErrorRes> genericExceptionHandler(Exception e) {
        return new ResponseEntity<HttpErrorRes>(new HttpErrorRes(HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal server error", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
