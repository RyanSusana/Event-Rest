package com.isa.arnhem.isarest.controllers;


import com.isa.arnhem.isarest.dto.CalculatedEventDTO;
import com.isa.arnhem.isarest.dto.CompleteEventDTO;
import com.isa.arnhem.isarest.dto.EventDTO;
import com.isa.arnhem.isarest.models.*;
import com.isa.arnhem.isarest.services.EventService;
import lombok.AllArgsConstructor;
import nl.stil4m.mollie.Client;
import nl.stil4m.mollie.ResponseOrError;
import nl.stil4m.mollie.domain.CreatePayment;
import nl.stil4m.mollie.domain.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping(path = "/api/events")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class EventController extends SecuredController {

    private final EventService eventService;

    private final Client mollieClient;

    @GetMapping
    public @ResponseBody
    List<EventDTO> getAll(@RequestParam(value = "includePast", required = false) Boolean includePast) {
        List<EventDTO> eventList = new ArrayList<>();
        Optional<Boolean> includePastEvents = Optional.ofNullable(includePast);
        EventList eventSet = includePastEvents.isPresent() && includePastEvents.get() == true ? eventService.getEventDao().getAll() : eventService.getEventDao().getAll().filterOutPastEvents();
        eventService.getEventDao().getAll().forEach(event -> {
            System.out.println(event.getName());
        });
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
                if (event.getSlug() == null) {
                    event.setSlugToName();
                }
                eventService.getEventDao().create(event);
            } else {
                return ResponseMessage.builder().messageType(ResponseMessageType.UNAUTHORIZED).message("You do not have permissions to create events.").build().toResponseEntity();
            }
        } else {
            return ResponseMessage.builder().messageType(ResponseMessageType.UNAUTHORIZED).message("You must be logged in to create events.").build().toResponseEntity();
        }
        return ResponseMessage.builder().messageType(ResponseMessageType.SUCCESSFUL).property("event_id", event.getId()).property("event_name", event.getName()).message("Successfully created an event").build().toResponseEntity();
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<ResponseMessage> updateEvent(@PathVariable("id") String id, @RequestBody EventDTO updated) {
        Optional<Event> original = eventService.getEventDao().findByString(id);
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
                    return ResponseMessage.builder().messageType(ResponseMessageType.SUCCESSFUL).message("Updated the event: " + original.get().getName()).property("new_event", original.get()).build().toResponseEntity();
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
        Optional<Event> event = eventService.getEventDao().findByString(id);
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
        Optional<Event> event = eventService.getEventDao().findById(id);

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

    @RequestMapping(path = "/payment-webhook", method = RequestMethod.POST)
    public ResponseEntity<ResponseMessage> paymentWebhook(@RequestParam("id") String paymentId) throws IOException {
        System.out.println("Webhook called: ");
        final ResponseOrError<Payment> payment = mollieClient.payments().get(paymentId);

        if (payment.getData().getStatus().equals("paid")) {
            final Optional<Event> event = eventService.getEventDao().findById((String) payment.getData().getMetadata().get("event_id"));
            final Optional<User> user = eventService.getUserDao().findById((String) payment.getData().getMetadata().get("user_id"));
            if (event.isPresent() && user.isPresent()) {
                if (event.get() instanceof PayedEvent) {
                    PayedEvent payedEvent = (PayedEvent) event.get();
                    Attendee attendee = Attendee.of(user.get().getId(), Calendar.getInstance().getTime());
                    payedEvent.getAttendees().add(attendee);
                    payedEvent.getRequestedAttendees().remove(attendee);
                    payedEvent.getPayments().add(new EventPayment(user.get().getId(), paymentId));

                    eventService.getEventDao().update(payedEvent);
                    return ResponseMessage.builder().message("Successfully added user to the event.").messageType(ResponseMessageType.SUCCESSFUL).build().toResponseEntity();
                } else {
                    return ResponseMessage.builder().message("Not a payed event!").messageType(ResponseMessageType.NOT_FOUND).build().toResponseEntity();
                }
            } else {
                return ResponseMessage.builder().message("Event or user doesn't exist!").messageType(ResponseMessageType.NOT_FOUND).build().toResponseEntity();
            }
        } else {
            return ResponseMessage.builder().message("Method not yet supported").messageType(ResponseMessageType.CLIENT_ERROR).build().toResponseEntity();
        }
    }

    @RequestMapping(path = "/{id}/pay", method = RequestMethod.POST)
    public ResponseEntity<ResponseMessage> createPayment(@PathVariable("id") String eventId, @RequestParam String redirectUrl, HttpServletRequest request) throws IOException {
        Optional<Event> event = eventService.getEventDao().findById(eventId);
        Optional<User> user = getLoggedInUser();
        String paymentWebhook = request.getScheme() + "://" + request.getHeader("host") + "/api/events/payment-webhook";

        if (paymentWebhook.contains("localhost")) {
            paymentWebhook = "https://google.com/";
        }
        if (user.isPresent()) {
            if (event.isPresent()) {
                if (event.get() instanceof PayedEvent) {
                    if (!event.get().getAttendees().containsUser(user.get())) {
                        PayedEvent payedEvent = (PayedEvent) event.get();
                        final Map<String, Object> metaData = new HashMap<>();
                        metaData.put("user_id", user.get().getId());
                        metaData.put("event_id", event.get().getId());

                        CreatePayment createPayment = new CreatePayment(null, event.get().getPrice().doubleValue(), String.format("ISA Event: %s", event.get().getName()), redirectUrl, Optional.of(paymentWebhook), metaData);
                        ResponseOrError<Payment> paymentResponse = mollieClient.payments().create(createPayment);
                        if (paymentResponse.getError() == null) {
                            payedEvent.getRequestedAttendees().add(Attendee.of(user.get().getId(), Calendar.getInstance().getTime()));
                            eventService.getEventDao().update(payedEvent);
                            return ResponseMessage.builder().message("Successfully created payment.").property("paymentUrl", paymentResponse.getData().getLinks().getPaymentUrl()).messageType(ResponseMessageType.REDIRECT).build().toResponseEntity();
                        } else {
                            return ResponseMessage.builder().message("Error processing payment!").properties(paymentResponse.getError()).messageType(ResponseMessageType.SERVER_ERROR).build().toResponseEntity();
                        }
                    } else {
                        return ResponseMessage.builder().message("Account is already attending this event!").messageType(ResponseMessageType.CLIENT_ERROR).build().toResponseEntity();
                    }
                } else {
                    return ResponseMessage.builder().message("This event does not support online payments!").messageType(ResponseMessageType.NOT_FOUND).build().toResponseEntity();
                }
            } else {
                return ResponseMessage.builder().message("Event doesn't exist!").messageType(ResponseMessageType.NOT_FOUND).build().toResponseEntity();
            }
        } else {
            return ResponseMessage.builder().message("You must be logged in to pay for an event!").messageType(ResponseMessageType.NOT_FOUND).build().toResponseEntity();
        }

    }

    @RequestMapping(path = "/{id}/attendees", method = RequestMethod.DELETE)
    public ResponseEntity<ResponseMessage> removeAttendee(@PathVariable("id") String eventId, @RequestParam String userId) {
        return eventService.removeUserFromEvent(eventId, userId).toResponseEntity();
    }

    @RequestMapping(path = "/{id}/attendees", method = RequestMethod.GET)
    public @ResponseBody
    AttendeeSet getAttendees(@PathVariable("id") String eventId) {
        Event event = eventService.getEventDao().findById(eventId).get();
        return event.getAttendees();
    }

    public ResponseEntity<ResponseMessage> addUserToControlledEvent(String eventId, String userId, Optional<User> controllerUser, int plus) {
        return eventService.addUserToControlledEvent(eventId, userId, controllerUser, plus).toResponseEntity();
    }


}
