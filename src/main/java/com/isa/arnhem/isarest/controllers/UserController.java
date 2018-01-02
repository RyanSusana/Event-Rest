package com.isa.arnhem.isarest.controllers;


import com.google.common.collect.Lists;
import com.isa.arnhem.isarest.dao.UserDao;
import com.isa.arnhem.isarest.models.User;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.coyote.http11.Constants.a;

@RestController
@RequestMapping(path = "/api/users", consumes = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    private final UserDao userDao;

    @Autowired
    public UserController(UserDao UserDao) {
        this.userDao = UserDao;
    }

    @GetMapping
    public @ResponseBody
    List<User> getAll() {
        return Lists.newArrayList(userDao.find().as(User.class).iterator());
    }

    @PostMapping
    public ResponseEntity<String> addUser(@RequestBody User user) {

        HttpHeaders responseHeaders = new HttpHeaders();

        final String evaluatedUsername = evaluateUsername(user.getUsername());

        if (evaluatedUsername != null) {
            return new ResponseEntity<>(evaluatedUsername, responseHeaders, HttpStatus.FORBIDDEN);
        }
        if (userDao.findByUsername(user.getUsername()) != null) {
            return new ResponseEntity<>("Username already taken: " + user.getEmail(), responseHeaders, HttpStatus.FORBIDDEN);
        }
        if(!EmailValidator.getInstance().isValid(user.getEmail())){
            return new ResponseEntity<>("Email invalid: " + user.getEmail(), responseHeaders, HttpStatus.FORBIDDEN);
        }
        if (userDao.findByEmail(user.getEmail()) != null) {
            return new ResponseEntity<>("Email already taken: " + user.getEmail(), responseHeaders, HttpStatus.FORBIDDEN);
        }
        userDao.create(user.secure());
        return new ResponseEntity<>("Created: " + user.getUsername(), responseHeaders, HttpStatus.CREATED);
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public @ResponseBody
    User getUser(@PathVariable("id") String id) {
        return userDao.findByUserId(id);
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteUser(@PathVariable("id") String id) {

        userDao.deleteByUserId(id);
        HttpHeaders responseHeaders = new HttpHeaders();
        return new ResponseEntity<>("Deleted", responseHeaders, HttpStatus.OK);
    }

    public String evaluateUsername(String username) {

        if (username == null || username.isEmpty()) {
            return "Username can't be empty!";
        }
//        boolean hasUppercase = !username.equals(username.toLowerCase());

        Pattern p = Pattern.compile("^[a-zA-Z0-9._]+$/");
        Matcher m = p.matcher(username);


        if (!m.find()) {
            return "Username can't have special characters!";
        }

        if (username.length() > 10) {
            return "Username must be less than 11 characters long";
        }
        return null;
    }
}
