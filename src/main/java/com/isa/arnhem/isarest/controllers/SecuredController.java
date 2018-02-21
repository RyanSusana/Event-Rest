package com.isa.arnhem.isarest.controllers;

import com.isa.arnhem.isarest.models.ResponseMessage;
import com.isa.arnhem.isarest.models.ResponseMessageType;
import com.isa.arnhem.isarest.models.User;
import com.isa.arnhem.isarest.models.UserDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Optional;

public class SecuredController {
    public Optional<User> getLoggedInUser() {
        try {
            User user = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
            return Optional.ofNullable(user);
        } catch (ClassCastException e) {
            return Optional.empty();
        }
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseMessage> handleException(Exception e) {
        e.printStackTrace();
        return ResponseMessage.builder().message("Internal server error!").messageType(ResponseMessageType.SERVER_ERROR).property("exceptionName", e.getClass().getSimpleName()).build().toResponseEntity();
    }
}
