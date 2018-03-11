package com.isa.arnhem.isarest.controllers;


import com.isa.arnhem.isarest.dto.AttendedEventDTO;
import com.isa.arnhem.isarest.dto.Response;
import com.isa.arnhem.isarest.dto.ResponseMessage;
import com.isa.arnhem.isarest.models.user.User;
import com.isa.arnhem.isarest.models.user.UserType;
import com.isa.arnhem.isarest.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping(path = "/api/users")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserController extends SecuredController {

    private final UserService userService;


    @RequestMapping(path = "/secured/", method = RequestMethod.GET)
    public @ResponseBody
    Collection<User> getAll() {
        return userService.getAll();
    }

    @GetMapping()
    public Collection<User> getAllUsers(@RequestParam(value = "q", required = false) String q, @RequestParam(value = "limit", required = false) Integer l, @RequestParam(value = "event", required = false) String eventId) {
        return userService.getAllUsers(q, l, eventId, getLoggedInUser());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> addUser(@RequestBody User user) {
        return userService.addUser(user).toResponseEntity();
    }

    @RequestMapping(path = "/secured/self", method = RequestMethod.POST)
    public @ResponseBody
    User getSelfUser(HttpServletRequest request) {
        return userService.getSelfUser(getLoggedInUser());
    }

    public @ResponseBody
    @RequestMapping(path = "/allowed-ranks", method = RequestMethod.GET)
    Iterable<UserType> getRanks() {
        return userService.getRanks(getLoggedInUser());
    }

    @RequestMapping(path = "/{id}/rank", method = RequestMethod.POST)
    public ResponseEntity<Response> updateRank(@PathVariable("id") String userId, @RequestParam("rank") String rank) {
        return userService.updateRank(userId, rank, getLoggedInUser()).toResponseEntity();
    }


    @RequestMapping(path = "/find/{id}", method = RequestMethod.GET)
    public @ResponseBody
    Object getUser(@PathVariable("id") String id) {
        return userService.getUser(id);
    }

    @RequestMapping(path = "/{id}/events", method = RequestMethod.GET)
    public @ResponseBody
    Map<String, Set<AttendedEventDTO>> getUserSignUps(@PathVariable("id") String id) {
        return userService.getUserSignUps(id);
    }

    @RequestMapping(path = "/activate/{id}", method = RequestMethod.GET)
    public ResponseEntity<Response> activateUser(@PathVariable("id") String id) {
        return userService.activateUser(id).toResponseEntity();
    }

    @RequestMapping(path = "/logout", method = RequestMethod.GET)
    public void logOut(HttpServletRequest request) {
        request.getSession().invalidate();
    }

}
