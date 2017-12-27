package com.isa.arnhem.isarest.controllers;


import com.google.common.collect.Lists;
import com.isa.arnhem.isarest.dao.EventDao;
import com.isa.arnhem.isarest.models.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/events")
public class EventController {

    private final EventDao eventDao;

    @Autowired
    public EventController(EventDao eventDao) {
        this.eventDao = eventDao;
    }

    @GetMapping
    public @ResponseBody
    List<Event> getAll() {
        return Lists.newArrayList(eventDao.find().as(Event.class).iterator());
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
        //TODO find query
        return eventDao.findOne("").as(Event.class);
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteEvent(@PathVariable("id") String id) {
        //TODO delete query
        eventDao.delete("");
        HttpHeaders responseHeaders = new HttpHeaders();
        return new ResponseEntity<String>("Deleted", responseHeaders, HttpStatus.OK);
    }
}