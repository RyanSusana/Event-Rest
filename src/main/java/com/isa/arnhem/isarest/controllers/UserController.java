package com.isa.arnhem.isarest.controllers;


import com.google.common.collect.Lists;
import com.isa.arnhem.isarest.dao.UserDao;
import com.isa.arnhem.isarest.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        if (userDao.findByUsername(user.getUsername()) != null) {
            //TODO proper username validation
            return new ResponseEntity<>("Username already taken: " + user.getEmail(), responseHeaders, HttpStatus.FORBIDDEN);
        }
        if (userDao.findByEmail(user.getEmail()) != null) {
            //TODO proper email validation
            return new ResponseEntity<>("Email already taken: " + user.getEmail(), responseHeaders, HttpStatus.FORBIDDEN);
        }
        userDao.create(user);
        return new ResponseEntity<>("Created: " + user.getUsername(), responseHeaders, HttpStatus.CREATED);
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public @ResponseBody
    User getUser(@PathVariable("id") String id) {
        return userDao.findByUserId(id);
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteUser(@PathVariable("id") String id) {
        //TODO delete query
        userDao.delete("");
        HttpHeaders responseHeaders = new HttpHeaders();
        return new ResponseEntity<>("Deleted", responseHeaders, HttpStatus.OK);
    }
}
