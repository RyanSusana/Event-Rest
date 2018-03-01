package com.isa.arnhem.isarest.controllers;

import com.isa.arnhem.isarest.models.user.User;
import com.isa.arnhem.isarest.models.user.UserDetail;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class SecuredController {
    Optional<User> getLoggedInUser() {
        try {
            final User user = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
            return Optional.ofNullable(user);
        } catch (ClassCastException e) {
            return Optional.empty();
        }
    }
}
