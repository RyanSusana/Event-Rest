package com.isa.arnhem.isarest.controllers;


import com.isa.arnhem.isarest.dto.EventDTO;
import com.isa.arnhem.isarest.dto.ResponseMessage;
import com.isa.arnhem.isarest.dto.ReturnedObject;
import com.isa.arnhem.isarest.models.event.Event;
import com.isa.arnhem.isarest.models.user.AttendeeSet;
import com.isa.arnhem.isarest.models.user.User;
import com.isa.arnhem.isarest.services.EventService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
@RestController
@RequestMapping(path = "/api/events")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class EventController extends SecuredController {

    private final EventService eventService;


    @GetMapping
    public @ResponseBody
    List<EventDTO> getAll(@RequestParam(value = "includePast", defaultValue = "false", required = false) Boolean includePast) {
        return eventService.getAll(includePast, getLoggedInUser());
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseMessage> addEvent(@RequestBody Event event) {
        return eventService.createEvent(event, getLoggedInUser()).toResponseEntity();
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseMessage> updateEvent(@PathVariable("id") String id, @RequestBody EventDTO updated) {
        return eventService.updateEvent(id, updated, getLoggedInUser()).toResponseEntity();
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public @ResponseBody
    Object getEvent(@PathVariable("id") String id) {
        return ReturnedObject.of(eventService.getEvent(id, getLoggedInUser()));
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseMessage> deleteEvent(@PathVariable("id") String id) {
        return eventService.deleteEvent(id, getLoggedInUser()).toResponseEntity();
    }


    @RequestMapping(path = "/{id}/attendees", method = RequestMethod.POST)
    @PreAuthorize("isAuthenticated()")
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

    @RequestMapping(path = "/payment-webhook", method = RequestMethod.POST)
    public ResponseEntity<ResponseMessage> paymentWebhook(@RequestParam("id") String paymentId) throws IOException {
        return eventService.processPayment(paymentId).toResponseEntity();
    }

    @RequestMapping(path = "/{id}/pay", method = RequestMethod.POST)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseMessage> createPayment(@PathVariable("id") String eventId, @RequestParam String redirectUrl, @RequestParam(required = false, defaultValue = "0") int plus, HttpServletRequest request) throws IOException {
        String paymentWebhook = request.getScheme() + "://" + request.getHeader("host") + "/api/events/payment-webhook";
        System.out.println(paymentWebhook);
        if (paymentWebhook.contains("localhost")) {
            paymentWebhook = "https://google.com/";
        }
        return eventService.newPayment(eventId, redirectUrl, plus, paymentWebhook, getLoggedInUser()).toResponseEntity();
    }

    @RequestMapping(path = "/{id}/attendees", method = RequestMethod.DELETE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseMessage> removeAttendee(@PathVariable("id") String eventId, @RequestParam String userId) {
        return eventService.removeUserFromEvent(eventId, userId).toResponseEntity();
    }

    @RequestMapping(path = "/{id}/attendees", method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody
    AttendeeSet getAttendees(@PathVariable("id") String eventId) {
        return eventService.getAttendees(eventId, getLoggedInUser());
    }

    private ResponseEntity<ResponseMessage> addUserToControlledEvent(String eventId, String userId, Optional<User> controllerUser, int plus) {
        return eventService.addUserToControlledEvent(eventId, userId, controllerUser, plus).toResponseEntity();
    }


}
