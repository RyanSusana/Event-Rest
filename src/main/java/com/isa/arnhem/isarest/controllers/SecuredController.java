package com.isa.arnhem.isarest.controllers;

import com.isa.arnhem.isarest.models.User;
import com.isa.arnhem.isarest.models.UserDetail;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecuredController {
    public User getLoggedInUser() {
        User user = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        return user;
    }
}
