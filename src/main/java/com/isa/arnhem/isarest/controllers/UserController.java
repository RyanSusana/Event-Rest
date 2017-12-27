package com.isa.arnhem.isarest.controllers;


import com.google.common.collect.Lists;
import com.isa.arnhem.isarest.dao.UserDao;
import com.isa.arnhem.isarest.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/users")
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
        //TODO proper User check
        if (userDao.findByUsername(user.getUsername()) != null) {
            return new ResponseEntity<String>("Username already taken: " + user.getEmail(), responseHeaders, HttpStatus.FORBIDDEN);
        }
        if (userDao.findByEmail(user.getEmail()) != null) {

            return new ResponseEntity<String>("Email already taken: " + user.getEmail(), responseHeaders, HttpStatus.FORBIDDEN);
        }
        userDao.create(user);
        return new ResponseEntity<String>("Created: " + user.getUsername(), responseHeaders, HttpStatus.CREATED);
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public @ResponseBody
    User getUser(@PathVariable("id") String id) {
        //TODO find query
        return userDao.findOne("").as(User.class);
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteUser(@PathVariable("id") String id) {
        //TODO delete query
        userDao.delete("");
        HttpHeaders responseHeaders = new HttpHeaders();
        return new ResponseEntity<String>("Deleted", responseHeaders, HttpStatus.OK);
    }
}
