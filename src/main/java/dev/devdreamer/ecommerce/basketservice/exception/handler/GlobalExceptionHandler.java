package dev.devdreamer.ecommerce.basketservice.exception.handler;

import dev.devdreamer.ecommerce.basketservice.exception.custom.BusinessException;
import dev.devdreamer.ecommerce.basketservice.exception.custom.EmailAlreadyExistsException;
import dev.devdreamer.ecommerce.basketservice.exception.custom.ResourceNotFoundException;
import dev.devdreamer.ecommerce.basketservice.exception.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler  {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourseNotFoundException(
            ResourceNotFoundException ex,
            HttpServletRequest request
    ){
     ErrorResponse error = ErrorResponse.of(
             HttpStatus.NOT_IMPLEMENTED.value(),
             HttpStatus.NOT_FOUND.name(),
             ex.getMessage(),
             request.getRequestURI()
     );
     return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }


    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyExistsException(
            EmailAlreadyExistsException ex,
            HttpServletRequest request
    ){
        ErrorResponse errorResponse = ErrorResponse.of(
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.name(),
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse>handleBusinessException(
            BusinessException ex,
            HttpServletRequest request
    ){
        ErrorResponse errorResponse = ErrorResponse.of(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.name(),
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }






}
