package com.isa.arnhem.isarest.controllers;

import com.isa.arnhem.isarest.dto.Response;
import com.isa.arnhem.isarest.dto.ResponseException;
import com.isa.arnhem.isarest.dto.ResponseMessage;
import com.isa.arnhem.isarest.dto.ResponseType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler(ResponseException.class)
    public ResponseEntity<Response> handleResponseException(ResponseException e) {
        return ResponseMessage.builder().properties(e.getProperties()).type(e.getType()).message(e.getMessage()).build().toResponseEntity();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response> handleUnknownException(Exception e) {
        e.printStackTrace();
        return ResponseMessage.builder().message("Internal server error!").type(ResponseType.SERVER_ERROR).property("exceptionName", e.getClass().getSimpleName()).property("exceptionMessage", e.getMessage()).build().toResponseEntity();
    }
}
