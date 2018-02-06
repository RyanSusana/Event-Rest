package com.isa.arnhem.isarest.controllers;

import com.isa.arnhem.isarest.models.User;
import com.isa.arnhem.isarest.models.UserDetail;
import org.springframework.security.core.context.SecurityContextHolder;

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
    public void logout(){
        SecurityContextHolder.clearContext();
    }
}
