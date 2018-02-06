package com.isa.arnhem.isarest.controllers;


import com.google.common.collect.Lists;
import com.isa.arnhem.isarest.models.*;
import com.isa.arnhem.isarest.services.UserService;
import lombok.AllArgsConstructor;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping(path = "/api/users")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserController extends SecuredController {

    private final UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @RequestMapping(path = "/secured/", method = RequestMethod.GET)
    public @ResponseBody
    List<User> getAll() {
        return Lists.newArrayList(userService.getUserDao().find().as(User.class).iterator());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseMessage> addUser(@RequestBody User user) {


        final String evaluatedUsername = evaluateUsername(user.getUsername());

        if (evaluatedUsername != null) {
            return ResponseMessage.builder().message(evaluatedUsername).messageType(ResponseMessageType.CLIENT_ERROR).build().toResponseEntity();
        }

        if (userService.getUserDao().findByUsername(user.getUsername()).isPresent()) {
            return ResponseMessage.builder().message("This username already taken!").messageType(ResponseMessageType.CLIENT_ERROR).build().toResponseEntity();
        }
        if (!EmailValidator.getInstance().isValid(user.getEmail())) {
            return ResponseMessage.builder().message("This is an invalid email!").messageType(ResponseMessageType.CLIENT_ERROR).build().toResponseEntity();
        }
        if (userService.getUserDao().findByEmail(user.getEmail()).isPresent()) {
            return ResponseMessage.builder().message("This e-mail address is already in use!").messageType(ResponseMessageType.CLIENT_ERROR).build().toResponseEntity();

        }

        int passwordLength = user.getPassword().length();

        if (passwordLength < 5 || passwordLength > 50) {
            return ResponseMessage.builder().message("Password must be between 4 and 50 characters long").messageType(ResponseMessageType.CLIENT_ERROR).build().toResponseEntity();

        }

        user.setType(UserType.STUDENT);
        user.setActivated(false);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.getUserDao().create(user);
        return ResponseMessage.builder().message("Created: " + user.getUsername()).messageType(ResponseMessageType.SUCCESSFUL).build().toResponseEntity();
    }

    @RequestMapping(path = "/secured/self", method = RequestMethod.POST)
    public @ResponseBody
    User getSelfUser() {


        User user = getLoggedInUser().get();
        user.setPassword(null);
        return user;
    }

    @RequestMapping(path = "/{id}/rank", method = RequestMethod.POST)
    public ResponseEntity<ResponseMessage> updateRank(@PathVariable("id") String userId, @RequestParam("rank") String rank) {
        final Optional<UserType> rankToUpdateTo = UserType.getRank(rank);
        final Optional<User> userToUpdate = userService.getUserDao().findByString(userId);
        final Optional<User> loggedInUser = getLoggedInUser();

        if (!userToUpdate.isPresent()) {
            return ResponseMessage.builder().messageType(ResponseMessageType.CLIENT_ERROR).message("User doesn't exist!").build().toResponseEntity();
        }
        if (loggedInUser.isPresent() && loggedInUser.get().getType().hasEqualRightsTo(UserType.ISA_MEMBER)) {
            if (userToUpdate.get().getType().hasMoreRightsThan(loggedInUser.get().getType())) {
                return ResponseMessage.builder().messageType(ResponseMessageType.UNAUTHORIZED).message("The user you are trying to update has a higher ranking than you!").build().toResponseEntity();
            }
            if (rankToUpdateTo.isPresent() && loggedInUser.get().getType().hasMoreRightsThan(rankToUpdateTo.get())) {

                userToUpdate.get().setType(rankToUpdateTo.get());
                userService.getUserDao().update(userToUpdate.get());
                return ResponseMessage.builder().messageType(ResponseMessageType.SUCCESSFUL).message("Succesfully updated users rank!").build().toResponseEntity();
            } else {
                List<UserType> ranksAllowed = loggedInUser.get().getType().getRanksBelow();

                StringBuilder stringBuilder = new StringBuilder();
                ranksAllowed.forEach((userType -> stringBuilder.append(userType.name() + ", ")));
                String ranksAllowedString = stringBuilder.toString().trim();

                ranksAllowedString = ranksAllowedString.substring(0, ranksAllowedString.length() - 1);
                return ResponseMessage.builder().messageType(ResponseMessageType.CLIENT_ERROR).message(String.format("Unable to update rank using using '%s' available ranks are[%s]", rank, ranksAllowedString)).build().toResponseEntity();
            }
        } else {
            return ResponseMessage.builder().messageType(ResponseMessageType.UNAUTHORIZED).message("You must be logged in as an ISA_MEMBER member(atleast) to promote/demote users.").build().toResponseEntity();

        }
    }


    @RequestMapping(path = "/find/{id}", method = RequestMethod.GET)
    public @ResponseBody
    User getUser(@PathVariable("id") String id) {
        User user = userService.getUserDao().findByString(id).get();

        user.setPassword(null);
        return user;
    }

    @RequestMapping(path = "/{id}/events", method = RequestMethod.GET)
    public @ResponseBody
    Map<String, Set<Event>> getUserSignUps(@PathVariable("id") String id) {

        Set<Event> events = new TreeSet<>();


        userService.getEventDao().find("{'attendees.user_id': #}", id)
                .projection("{_id: 1, price: 1, name: 1, main_image: 1, event_type: 1, date: 1}")
                .as(Event.class).iterator().forEachRemaining(events::add);

        Set<Event> requestedEvents = new TreeSet<>();

        userService.getEventDao().find("{'requested_attendees.user_id': #}", id)
                .projection("{_id: 1, price: 1, name: 1, main_image: 1, event_type: 1, date: 1}")
                .as(Event.class).iterator().forEachRemaining(requestedEvents::add);

        Map<String, Set<Event>> eventMap = new HashMap<>();
        eventMap.put("attending_events", events);
        eventMap.put("requested_events", requestedEvents);
        return eventMap;
    }

    @RequestMapping(path = "/activate/{id}", method = RequestMethod.GET)
    public ResponseEntity<String> activateUser(@PathVariable("id") String id) {
        User user = userService.getUserDao().findByUserId(id).get();
        if (user == null) {
            return new ResponseEntity<>("Cannot find user with id: " + id, HttpStatus.NOT_FOUND);
        }
        if (!user.isActivated()) {
            user.setActivated(true);
            userService.getUserDao().update(user);
            return new ResponseEntity<>("Activated: " + user.getUsername(), HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(user.getUsername() + " is already activated!", HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(path = "/logout", method = RequestMethod.GET)
    public void logOut() {
        logout();
    }

    public String evaluateUsername(String username) {

        if (username.startsWith(".") || username.endsWith(".")) {
            return "Username can't begin or end with a dot!";
        }
        if (username == null || username.isEmpty()) {
            return "Username can't be empty!";
        }

        Pattern p = Pattern.compile("[^a-z0-9._ ]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(username);

        if (m.find()) {
            return "Username can't have special characters!";
        }

        if (username.length() > 35) {
            return "Username must be less than 36 characters long";
        }
        return null;
    }
}
