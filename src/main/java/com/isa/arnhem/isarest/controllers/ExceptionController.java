package com.isa.arnhem.isarest.controllers;

import com.isa.arnhem.isarest.dto.ResponseException;
import com.isa.arnhem.isarest.dto.ResponseMessage;
import com.isa.arnhem.isarest.dto.ResponseType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler(ResponseException.class)
    public ResponseEntity<ResponseMessage> handleResponseException(ResponseException e) {
        return ResponseMessage.builder().message(e.getMessage()).type(e.getType()).properties(e.getProperties()).build().toResponseEntity();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseMessage> handleUnknownException(Exception e) {
        e.printStackTrace();
        return ResponseMessage.builder().message("Internal server error!").type(ResponseType.SERVER_ERROR).property("exceptionName", e.getClass().getSimpleName()).property("exceptionMessage", e.getMessage()).build().toResponseEntity();
    }
}
