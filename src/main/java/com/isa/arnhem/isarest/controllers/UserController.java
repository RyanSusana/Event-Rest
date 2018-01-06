package com.isa.arnhem.isarest.controllers;


import com.google.common.collect.Lists;
import com.isa.arnhem.isarest.dao.EventDao;
import com.isa.arnhem.isarest.dao.UserDao;
import com.isa.arnhem.isarest.models.Event;
import com.isa.arnhem.isarest.models.User;
import com.isa.arnhem.isarest.models.UserType;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping(path = "/api/users")
public class UserController {

    private final UserDao userDao;
    private final EventDao eventDao;

    @Autowired
    public UserController(final UserDao UserDao, final EventDao eventDao) {
        this.userDao = UserDao;
        this.eventDao = eventDao;
    }

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public @ResponseBody
    List<User> getAll() {
        return Lists.newArrayList(userDao.find().as(User.class).iterator());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addUser(@RequestBody User user) {


        final String evaluatedUsername = evaluateUsername(user.getUsername());

        if (evaluatedUsername != null) {
            return new ResponseEntity<>(evaluatedUsername, HttpStatus.FORBIDDEN);
        }
        if (userDao.findByUsername(user.getUsername()) != null) {
            return new ResponseEntity<>("This username already taken!", HttpStatus.FORBIDDEN);
        }
        if (!EmailValidator.getInstance().isValid(user.getEmail())) {
            return new ResponseEntity<>("This is an invalid email!", HttpStatus.FORBIDDEN);
        }
        if (userDao.findByEmail(user.getEmail()) != null) {
            return new ResponseEntity<>("This e-mail address is already in use!", HttpStatus.FORBIDDEN);
        }

        int passwordLength = user.getPassword().length();

        if (passwordLength < 5 || passwordLength > 50) {
            return new ResponseEntity<>("Password must be between 4 and 50 characters long", HttpStatus.FORBIDDEN);
        }

        user.setType(UserType.MEMBER);
        user.setActivated(false);
        userDao.create(user.secure());
        return new ResponseEntity<>("Created: " + user.getUsername(), HttpStatus.CREATED);
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public @ResponseBody
    User getUser(@PathVariable("id") String id) {
        return userDao.findByUserId(id);
    }

    @RequestMapping(path = "/{id}/events", method = RequestMethod.GET)
    public @ResponseBody
    List<Event> getUserSignUps(@PathVariable("id") String id) {

        List<Event> events = new ArrayList<>();

        Lists.newArrayList(eventDao.find("{'attendees.user_id': #}", id)
                .projection("{event_id: 1, price: 1, name: 1, main_image: 1}")
                .as(Event.class).iterator())
                .forEach((event) -> {
                    events.add(event);
                });


        return events;
    }

    @RequestMapping(path = "/activate/{id}", method = RequestMethod.GET)
    public ResponseEntity<String> activateUser(@PathVariable("id") String id) {
        User user = userDao.findByUserId(id);
        if (user == null) {
            return new ResponseEntity<>("Cannot find user with id: " + id, HttpStatus.NOT_FOUND);
        }
        if (!user.isActivated()) {
            user.setActivated(true);
            userDao.update(user);
            return new ResponseEntity<>("Activated: " + user.getUsername(), HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(user.getUsername() + " is already activated!", HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public ResponseEntity<String> login(@RequestParam(value = "usernameOrEmail", required = true) String usernameOrEmail, @RequestParam(value = "password", required = false) String password) {
        User user;

        user = userDao.findByUsername(usernameOrEmail);
        if (user == null) {
            user = userDao.findByEmail(usernameOrEmail);
        }

        if (user != null) {
            if (user.samePassword(user.getId(), password)) {
                return new ResponseEntity<>(user.getId(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("Invalid Username/Password!", HttpStatus.UNAUTHORIZED);
    }

    public String evaluateUsername(String username) {

        if (username == null || username.isEmpty()) {
            return "Username can't be empty!";
        }
//        boolean hasUppercase = !username.equals(username.toLowerCase());

        Pattern p = Pattern.compile("^[a-zA-Z0-9._]+$/");
        Matcher m = p.matcher(username);


        if (m.find()) {
            return "Username can't have special characters!";
        }

        if (username.length() > 10) {
            return "Username must be less than 11 characters long";
        }
        return null;
    }
}
