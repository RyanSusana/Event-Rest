package com.isa.arnhem.isarest.controllers;


import com.isa.arnhem.isarest.dto.CalculatedEventDTO;
import com.isa.arnhem.isarest.dto.CompleteEventDTO;
import com.isa.arnhem.isarest.dto.EventDTO;
import com.isa.arnhem.isarest.models.*;
import com.isa.arnhem.isarest.services.EventService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/events")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class EventController extends SecuredController {

    private final EventService eventService;

    @GetMapping
    public @ResponseBody
    List<EventDTO> getAll(@RequestParam(value = "includePast", required = false) Boolean includePast) {
        List<EventDTO> eventList = new ArrayList<>();
        Optional<Boolean> includePastEvents = Optional.ofNullable(includePast);
        EventSet eventSet = includePastEvents.isPresent() && includePastEvents.get() == true ? eventService.getEventDao().getAll() : eventService.getEventDao().getAll().filterOutPastEvents();

        eventSet.forEach((event -> {
            final Optional<User> loggedInUser = getLoggedInUser();
            if (loggedInUser.isPresent() && loggedInUser.get().isAuthorative()) {
                eventList.add(new CalculatedEventDTO(event));
            } else {
                eventList.add(new EventDTO(event));
            }
        }));


        return eventList;
    }

    @PostMapping
    public ResponseEntity<ResponseMessage> addEvent(@RequestBody Event event) {
        Optional<User> user = getLoggedInUser();

        if (user.isPresent()) {
            if (user.get().getType().hasEqualRightsTo(UserType.ISA_ADMIN)) {
                eventService.getEventDao().create(event);
            } else {
                return ResponseMessage.builder().messageType(ResponseMessageType.UNAUTHORIZED).message("You do not have permissions to create events.").build().toResponseEntity();
            }
        } else {
            return ResponseMessage.builder().messageType(ResponseMessageType.UNAUTHORIZED).message("You must be logged in to create events.").build().toResponseEntity();
        }
        return ResponseMessage.builder().messageType(ResponseMessageType.SUCCESSFUL).message("Created: " + event.getName()).build().toResponseEntity();
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<ResponseMessage> updateEvent(@PathVariable("id") String id, @RequestBody EventDTO updated) {
        Optional<Event> original = eventService.getEventDao().findByEventId(id);
        Optional<User> user = getLoggedInUser();
        if (user.isPresent()) {
            if (user.get().getType().hasEqualRightsTo(UserType.ISA_ADMIN)) {
                if (original.isPresent()) {
                    original.get().setName(updated.getName());
                    original.get().setDescription(updated.getDescription());
                    original.get().setMainImage(updated.getMainImage());
                    original.get().setPrice(updated.getPrice());
                    original.get().setProperties(updated.getProperties());
                    original.get().setPriority(updated.getPriority());
                    eventService.getEventDao().update(original.get());
                    return ResponseMessage.builder().messageType(ResponseMessageType.SUCCESSFUL).message("Updated the event: " + original.get().getName()).build().toResponseEntity();
                } else {
                    return ResponseMessage.builder().messageType(ResponseMessageType.NOT_FOUND).message("Event does not exist!").build().toResponseEntity();
                }
            } else {
                return ResponseMessage.builder().messageType(ResponseMessageType.UNAUTHORIZED).message("You do not have permissions to update events.").build().toResponseEntity();
            }
        } else {
            return ResponseMessage.builder().messageType(ResponseMessageType.UNAUTHORIZED).message("You must be logged in to update events.").build().toResponseEntity();

        }

    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public @ResponseBody
    Object getEvent(@PathVariable("id") String id) {
        Optional<Event> event = eventService.getEventDao().findByEventId(id);
        if (event.isPresent()) {
            final Optional<User> loggedInUser = getLoggedInUser();

            if (loggedInUser.isPresent() && loggedInUser.get().isAuthorative()) {
                return new CompleteEventDTO(event.get());
            } else {
                return new EventDTO(event.get());
            }
        }
        return ResponseMessage.builder().messageType(ResponseMessageType.NOT_FOUND).message("Event does not exist!").build().toResponseEntity();

    }

    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<ResponseMessage> deleteEvent(@PathVariable("id") String id) {
        Optional<Event> event = eventService.getEventDao().findByEventId(id);

        if (event.isPresent()) {
            eventService.getEventDao().delete(event.get());
            return ResponseMessage.builder().message("Deleted event: " + event.get().getName()).messageType(ResponseMessageType.NOT_FOUND).build().toResponseEntity();
        } else {
            return ResponseMessage.builder().message("Can't delete, event not found.").messageType(ResponseMessageType.NOT_FOUND).build().toResponseEntity();
        }

    }


    @RequestMapping(path = "/{id}/attendees", method = RequestMethod.POST)
    public ResponseEntity<ResponseMessage> addAttendee(@PathVariable("id") String eventId, @RequestParam("userId") String userId, @RequestParam(value = "controlled", required = false) Boolean controlled, @RequestParam(value = "plus", required = false) Integer _plus) {
        if (controlled != null && controlled) {
            return eventService.addUserToEvent(eventId, userId).toResponseEntity();
        }
        final int plus;
        if (_plus == null || _plus < 0) {
            plus = 0;
        } else {
            plus = _plus;
        }
        return addUserToControlledEvent(eventId, userId, getLoggedInUser(), plus);
    }

    @RequestMapping(path = "/{id}/attendees", method = RequestMethod.DELETE)
    public ResponseEntity<ResponseMessage> removeAttendee(@PathVariable("id") String eventId, @RequestParam String userId) {
        return eventService.removeUserFromEvent(eventId, userId).toResponseEntity();
    }

    @RequestMapping(path = "/{id}/attendees", method = RequestMethod.GET)
    public @ResponseBody
    AttendeeSet getAttendees(@PathVariable("id") String eventId) {
        Event event = eventService.getEventDao().findByEventId(eventId).get();
        return event.getAttendees();
    }

    public ResponseEntity<ResponseMessage> addUserToControlledEvent(String eventId, String userId, Optional<User> controllerUser, int plus) {
        return eventService.addUserToControlledEvent(eventId, userId, controllerUser, plus).toResponseEntity();
    }


}
