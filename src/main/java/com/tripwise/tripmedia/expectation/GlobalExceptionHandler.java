package com.tripwise.tripmedia.expectation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.*;

/**
 * ================================================================
 * Package Name: com.tripwise.tripmedia.expectation
 * Author      : Ochwada-GMK
 * Project Name: tripmedia
 * Date        : Tuesday,  26.Aug.2025 | 10:28
 * Description :
 * ================================================================
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<?> nf(NoSuchElementException e){
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("error",e.getMessage())
                );
    }

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<?> fb(SecurityException e){
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(Map.of("error",e.getMessage())
                );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> gx(Exception e){
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error",e.getMessage())
                );
    }

}
