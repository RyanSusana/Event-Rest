package com.isa.arnhem.isarest.controllers;


import com.google.common.collect.Lists;
import com.isa.arnhem.isarest.dao.EventDao;
import com.isa.arnhem.isarest.dao.UserDao;
import com.isa.arnhem.isarest.models.Attendee;
import com.isa.arnhem.isarest.models.Event;
import com.isa.arnhem.isarest.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.List;

@RestController
@RequestMapping(path = "/api/events")
public class EventController {

    private final EventDao eventDao;

    private final UserDao userDao;

    @Autowired
    public EventController(final EventDao eventDao, final UserDao userDao) {
        this.eventDao = eventDao;
        this.userDao = userDao;
    }

    @GetMapping
    public @ResponseBody
    List<Event> getAll() {
        List<Event> eventList = Lists.newArrayList(eventDao.find().as(Event.class).iterator());

        return eventList;
    }

    @PostMapping
    public ResponseEntity<String> addEvent(@RequestBody Event event) {
        //TODO proper event check
        eventDao.create(event);
        HttpHeaders responseHeaders = new HttpHeaders();
        return new ResponseEntity<String>("Created: " + event.getName(), responseHeaders, HttpStatus.CREATED);
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public @ResponseBody
    Event getEvent(@PathVariable("id") String id) {
        return eventDao.findByEventId(id);
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteEvent(@PathVariable("id") String id) {
        //TODO delete query
        eventDao.delete("");
        HttpHeaders responseHeaders = new HttpHeaders();
        return new ResponseEntity<String>("Deleted", responseHeaders, HttpStatus.OK);
    }

    @RequestMapping(path = "/{id}/attendees", method = RequestMethod.POST)
    public ResponseEntity<String> addAttendee(@PathVariable("id") String eventId, @RequestParam String userId) {
        User user = userDao.findByUserId(userId);
        Event event = eventDao.findByEventId(eventId);
        if (event == null) {
            return new ResponseEntity<>("Event doesn't exist!", HttpStatus.NOT_FOUND);

        }
        if (user == null) {
            return new ResponseEntity<>("User doesn't exist!", HttpStatus.NOT_FOUND);

        }
        Attendee attendee = new Attendee(userId, Calendar.getInstance().getTime());

        if (event.getAttendees().contains(attendee)) {

            return new ResponseEntity<>("Event is already being attended by this user.", HttpStatus.FORBIDDEN);
        }
        event.getAttendees().add(attendee);
        eventDao.update(event);
        return new ResponseEntity<>("Added " + user.getUsername() + " to the event: " + event.getName(), HttpStatus.OK);
    }

    @RequestMapping(path = "/{id}/attendees", method = RequestMethod.DELETE)
    public ResponseEntity<String> removeAttendee(@PathVariable("id") String eventId, @RequestParam String userId) {
        User user = userDao.findByUserId(userId);
        Event event = eventDao.findByEventId(eventId);
        if (event == null) {
            return new ResponseEntity<>("Event doesn't exist!", HttpStatus.NOT_FOUND);
        }
        if (user == null) {
            return new ResponseEntity<>("User doesn't exist!", HttpStatus.NOT_FOUND);
        }
        Attendee attendee = new Attendee(userId, Calendar.getInstance().getTime());

        if (!event.getAttendees().contains(attendee)) {
            return new ResponseEntity<>("Event is already not being attended by this user.", HttpStatus.FORBIDDEN);
        }
        event.getAttendees().remove(attendee);

        eventDao.update(event);
        return new ResponseEntity<>("Removed " + user.getUsername() + " from the event: " + event.getName(), HttpStatus.OK);
    }

    @RequestMapping(path = "/{id}/attendees", method = RequestMethod.GET)
    public @ResponseBody
    List<Attendee> getAttendees(@PathVariable("id") String eventId) {
        Event event = eventDao.findByEventId(eventId);
        return event.getAttendees();
    }


}
