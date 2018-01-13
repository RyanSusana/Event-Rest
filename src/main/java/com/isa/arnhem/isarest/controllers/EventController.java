package com.isa.arnhem.isarest.controllers;


import com.google.common.collect.Lists;
import com.isa.arnhem.isarest.models.*;
import com.isa.arnhem.isarest.services.EventService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/events")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class EventController {

    private final EventService eventService;

    @GetMapping
    public @ResponseBody
    List<Event> getAll() {
        List<Event> eventList = Lists.newArrayList(eventService.getEventDao().find().as(Event.class).iterator());

        return eventList;
    }

    @PostMapping
    public ResponseEntity<ResponseMessage> addEvent(@RequestBody Event event) {

        eventService.getEventDao().create(event);
        return ResponseMessage.builder().messageType(ResponseMessageType.SUCCESSFUL).message("Created: " + event.getName()).build().toResponseEntity();
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public @ResponseBody
    Event getEvent(@PathVariable("id") String id) {
        return eventService.getEventDao().findByEventId(id);
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteEvent(@PathVariable("id") String id) {
        //TODO delete query
        //eventService.getEventDao().delete("");
        HttpHeaders responseHeaders = new HttpHeaders();
        return new ResponseEntity<>("Deleted", responseHeaders, HttpStatus.OK);
    }

    @RequestMapping(path = "/{id}/attendees", method = RequestMethod.POST)
    public ResponseEntity<ResponseMessage> addAttendee(@PathVariable("id") String eventId, @RequestParam String userId) {
        return eventService.addUserToEvent(eventId, userId).toResponseEntity();
    }

    @RequestMapping(path = "/{id}/attendees", method = RequestMethod.DELETE)
    public ResponseEntity<ResponseMessage> removeAttendee(@PathVariable("id") String eventId, @RequestParam String userId) {
        return eventService.removeUserFromEvent(eventId, userId).toResponseEntity();
    }

    @RequestMapping(path = "/{id}/attendees", method = RequestMethod.GET)
    public @ResponseBody
    List<Attendee> getAttendees(@PathVariable("id") String eventId) {
        Event event = eventService.getEventDao().findByEventId(eventId);
        return event.getAttendees();
    }


}
