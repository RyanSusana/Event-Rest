package com.isa.arnhem.isarest.services;

import com.isa.arnhem.isarest.dto.*;
import com.isa.arnhem.isarest.models.NotificationType;
import com.isa.arnhem.isarest.models.event.*;
import com.isa.arnhem.isarest.models.user.Attendee;
import com.isa.arnhem.isarest.models.user.AttendeeSet;
import com.isa.arnhem.isarest.models.user.User;
import com.isa.arnhem.isarest.models.user.UserType;
import com.isa.arnhem.isarest.repository.DaoRepository;
import nl.stil4m.mollie.Client;
import nl.stil4m.mollie.ResponseOrError;
import nl.stil4m.mollie.domain.CreatePayment;
import nl.stil4m.mollie.domain.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
@Service
public class EventService extends BaseService {

    private final Client mollieClient;

    @Autowired
    public EventService(DaoRepository daoRepository, Client mollieClient) {
        super(daoRepository);
        this.mollieClient = mollieClient;
    }


    public ResponseMessage addUserToEvent(final String eventId, final String userId) {
        final User user = evaluateUserIsLoggedIn(userId);
        final Event event = evaluateEvent(eventId);

        return addUserToEvent(event, user);
    }

    public ResponseMessage removeUserFromEvent(final String eventId, final String userId) {
        final User user = evaluateUserIsLoggedIn(userId);
        final Event event = evaluateEvent(eventId);

        Attendee attendee = Attendee.of(userId, Calendar.getInstance().getTime());

        if (!event.getAttendees().contains(attendee)) {
            return ResponseMessage.builder().message("Event is already not being attended by this user.").type(ResponseType.CLIENT_ERROR).build();
        } else {
            if (event instanceof PayedEvent) {
                return ResponseMessage.builder().message("You have already payed for this event, refunds are not allowed.").type(ResponseType.CLIENT_ERROR).build();
            } else {
                event.getAttendees().remove(attendee);
                eventDao.update(event);
                return ResponseMessage.builder().message("Removed " + user.getUsername() + " from the event: " + event.getName()).type(ResponseType.SUCCESSFUL).build();
            }
        }
    }

    private ResponseMessage addUserToEvent(final Event event, final User user) {
        final Attendee attendee = Attendee.of(user.getId(), Calendar.getInstance().getTime());

        if (event.getAttendees().contains(attendee)) {
            return ResponseMessage.builder().message("Event is already being attended by this user.").type(ResponseType.CLIENT_ERROR).build();
        }
        if (event instanceof ControlledEvent) {
            final ControlledEvent controlledEvent = (ControlledEvent) (event);
            if (controlledEvent.getRequestedAttendees().contains(attendee)) {
                return ResponseMessage.builder().type(ResponseType.CLIENT_ERROR).message("You have already requested to join this event.").build();
            }
            controlledEvent.getRequestedAttendees().add(attendee);
            eventDao.update(controlledEvent);
            final List<User> admins = userDao.findByTypeAndAbove(controlledEvent.getControlledBy());
            notificationDao.notifyUsers(user.getUsername() + " has requested to join the event: " + controlledEvent.getName(), NotificationType.MESSAGE, admins);
            return ResponseMessage.builder().message(user.getUsername() + " has requested to join the event: " + controlledEvent.getName()).type(ResponseType.ACCEPTED).build();
        } else {
            event.getAttendees().add(attendee);
            eventDao.update(event);
            return ResponseMessage.builder().type(ResponseType.SUCCESSFUL).message("Added " + user.getUsername() + " to the event: " + event.getName()).build();

        }
    }

    public AttendeeSet getAttendees(String eventId, Optional<User> loggedInUser) {
        Event event = evaluateEvent(eventId);
        evaluateUserIsAuthorized(loggedInUser, UserType.ISA_MEMBER);
        return event.getAttendees();
    }


    public ResponseMessage addUserToControlledEvent(final String eventId, final String userId, final Optional<User> loggedInUser, final int plus) {

        final Event event = evaluateEvent(eventId);
        final Optional<User> userToAdd = userDao.findByString(userId);
        final User controllerUser = evaluateUserIsLoggedIn(loggedInUser);

        if (!userToAdd.isPresent()) {
            return ResponseMessage.builder().message("User doesn't exist!").type(ResponseType.NOT_FOUND).build();
        }

        if (event.getAttendees().contains(Attendee.of(userToAdd.get().getId(), null))) {
            return ResponseMessage.builder().message("Event is already being attended by this user.").type(ResponseType.CLIENT_ERROR).build();
        }
        if (event instanceof ControlledEvent) {
            ControlledEvent controlledEvent = (ControlledEvent) event;
            evaluateUserIsAuthorized(controllerUser, controlledEvent.getControlledBy());

            Optional<Attendee> attendee = controlledEvent.getRequestedAttendees().getAttendee(userToAdd.get());

            if (!attendee.isPresent()) {
                controlledEvent.getAttendees().add(Attendee.of(userToAdd.get().getId(), Calendar.getInstance().getTime(), plus));
            } else {
                attendee.get().setPlus(plus);
                controlledEvent.getAttendees().add(attendee.get());
                controlledEvent.getRequestedAttendees().remove(attendee.get());
            }
            eventDao.update(controlledEvent);
            return ResponseMessage.builder().type(ResponseType.ACCEPTED).message(userToAdd.get().getUsername() + " is now attending the event: " + event.getName()).build();
        } else {
            return addUserToEvent(event, userToAdd.get());
        }
    }

    public ResponseMessage updateEvent(final String id, final EventDTO updated, final Optional<User> loggedInUser) {
        Event original = evaluateEvent(id);
        evaluateUserIsAuthorized(loggedInUser, UserType.ISA_ADMIN);

        updateEvent(original, updated);
        return ResponseMessage.builder().type(ResponseType.SUCCESSFUL).message("Updated the event: " + original.getName()).property("new_event", original).build();
    }

    public ResponseMessage createEvent(final Event event, final Optional<User> userLoggedIn) {
        evaluateUserIsAuthorized(userLoggedIn, UserType.ISA_ADMIN);

        if (event.getSlug() == null) {
            event.setSlugToName();
        }
        eventDao.create(event);

        return ResponseMessage.builder().type(ResponseType.SUCCESSFUL).property("event_id", event.getId()).property("event_name", event.getName()).message("Successfully created an event").build();

    }

    public Optional<EventDTO> getEvent(final String id, final Optional<User> loggedInUser) {
        Event event = evaluateEvent(id);

        if (loggedInUser.isPresent() && loggedInUser.get().isAuthorative()) {
            return Optional.of(new CompleteEventDTO(event));
        } else {
            return Optional.of(new EventDTO(event));
        }
    }

    public ResponseMessage checkInEvent(final String userId, final String eventId, Optional<User> loggedInUser, boolean test) {
        Event event = evaluateEvent(eventId);
        if (!(event instanceof ControlledEvent)) {
            throw ResponseException.builder().message("This event is a 'pay at the door' or a free event").type(ResponseType.CLIENT_ERROR).build();
        }
        ControlledEvent controlledEvent = (ControlledEvent) event;
        User eventAdmin = evaluateUserIsAuthorized(loggedInUser, controlledEvent.getControlledBy());
        User user = evaluateUserIsLoggedIn(userId);

        if (!controlledEvent.getAttendees().containsUser(user)) {
            return ResponseMessage.builder().message("This user is not allowed inside of this event.").type(ResponseType.UNAUTHORIZED).build();
        } else if (controlledEvent.getCheckedInAttendees().containsUser(user)) {
            return ResponseMessage.builder().message("This user has already checked in. Double check him/her.").type(ResponseType.UNAUTHORIZED).build();
        } else {
            return checkInEvent(controlledEvent, user, test);
        }
    }

    private ResponseMessage checkInEvent(ControlledEvent controlledEvent, User user, boolean test) {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR, 1);
        Date anHourFromNow = cal.getTime();
        Attendee attendee = controlledEvent.getAttendees().getAttendee(user).get();
        String ticketString = "" + (attendee.getPlus() + 1) + " ticket" + (attendee.getPlus() > 0 ? "s" : "");
        if (controlledEvent.getDate().before(anHourFromNow) || test) {
            controlledEvent.getCheckedInAttendees().add(controlledEvent.getAttendees().getAttendee(user).get());
            eventDao.update(controlledEvent);
            return ResponseMessage.builder().message(String.format("The user has been checked into the event(%s)", ticketString)).type(ResponseType.ACCEPTED).build();
        } else {
            return ResponseMessage.builder().message(String.format("It is too early to be checked into the event, but the user may enter. (%s)", ticketString)).type(ResponseType.FOUND).build();
        }
    }

    public ResponseMessage processPayment(final String paymentId) throws IOException {
        final ResponseOrError<Payment> payment = mollieClient.payments().get(paymentId);

        if (payment.getData().getStatus().equals("paid")) {
            final Optional<Event> event = eventDao.findById((String) payment.getData().getMetadata().get("event_id"));
            final Optional<User> user = userDao.findById((String) payment.getData().getMetadata().get("user_id"));

            final Integer plus = Optional.of(Integer.valueOf((String) payment.getData().getMetadata().get("plus"))).orElse(0);
            if (event.isPresent() && user.isPresent()) {
                return updateEventWithProcessedPayment(event.get(), user.get(), paymentId, plus);
            } else {
                return ResponseMessage.builder().message("Event or user doesn't exist!").type(ResponseType.NOT_FOUND).build();
            }
        } else {
            return ResponseMessage.builder().message("Method not yet supported").type(ResponseType.CLIENT_ERROR).build();
        }
    }

    private ResponseMessage updateEventWithProcessedPayment(Event event, User user, String paymentId, int plus) {
        if (event instanceof PayedEvent) {
            PayedEvent payedEvent = (PayedEvent) event;
            Attendee attendee = Attendee.of(user.getId(), Calendar.getInstance().getTime(), plus);
            payedEvent.getAttendees().add(attendee);
            payedEvent.getRequestedAttendees().remove(attendee);
            payedEvent.getPayments().add(new EventPayment(user.getId(), paymentId));

            eventDao.update(payedEvent);
            return ResponseMessage.builder().message("Successfully added user to the event.").type(ResponseType.SUCCESSFUL).build();
        } else {
            return ResponseMessage.builder().message("Not a payed event!").type(ResponseType.NOT_FOUND).build();
        }
    }

    public ResponseMessage newPayment(final String eventId, final String redirectUrl, int plus, final String paymentWebhook, final Optional<User> userOptional) throws IOException {

        User user = evaluateUserIsLoggedIn(userOptional);
        Event event = evaluateEvent(eventId);
        if (plus < 0) {
            plus = 0;
        }
        if (event instanceof PayedEvent) {
            return createPaymentAndUpdateEvent((PayedEvent) event, user, plus, redirectUrl, paymentWebhook);
        } else {
            return ResponseMessage.builder().message("This event does not support online payments!").type(ResponseType.NOT_FOUND).build();
        }
    }

    private ResponseMessage createPaymentAndUpdateEvent(PayedEvent event, User user, int plus, String redirectUrl, String paymentWebhook) throws IOException {
        if (!event.getAttendees().containsUser(user)) {
            PayedEvent payedEvent = (PayedEvent) event;
            final Map<String, Object> metaData = new HashMap<>();
            metaData.put("user_id", user.getId());
            metaData.put("event_id", event.getId());
            metaData.put("plus", plus);
            @SuppressWarnings("ConstantConditions") CreatePayment createPayment =
                    new CreatePayment(null,
                            event.getPrice().doubleValue() * (plus + 1), String.format("ISA Event: %s", event.getName()),
                            redirectUrl,
                            Optional.of(paymentWebhook),
                            metaData);
            ResponseOrError<Payment> paymentResponse = mollieClient.payments().create(createPayment);
            if (paymentResponse.getError() == null) {
                payedEvent.getRequestedAttendees().add(Attendee.of(user.getId(), Calendar.getInstance().getTime()));
                eventDao.update(payedEvent);
                return ResponseMessage.builder().message("Successfully created payment.").property("paymentUrl", paymentResponse.getData().getLinks().getPaymentUrl()).type(ResponseType.REDIRECT).build();
            } else {
                return ResponseMessage.builder().message("Error creating payment!").properties(paymentResponse.getError()).type(ResponseType.SERVER_ERROR).build();
            }
        } else {
            return ResponseMessage.builder().message("Account is already attending this event!").type(ResponseType.CLIENT_ERROR).build();
        }
    }

    public ResponseMessage deleteEvent(final String id, final Optional<User> user) {
        Event event = evaluateEvent(id);
        evaluateUserIsAuthorized(user, UserType.ISA_ADMIN);
        eventDao.delete(event);
        return ResponseMessage.builder().message("Deleted event: " + event.getName()).type(ResponseType.SUCCESSFUL).build();
    }

    public List<EventDTO> getAll(final Boolean includePast, final Optional<User> loggedInUser) {
        List<EventDTO> eventList = new ArrayList<>();
        Optional<Boolean> includePastEvents = Optional.ofNullable(includePast);
        EventList eventSet = includePastEvents.isPresent() && includePastEvents.get() ? eventDao.getAll() : eventDao.getAll().filterOutPastEvents();
        eventSet.forEach((event -> {
            if (loggedInUser.isPresent() && loggedInUser.get().isAuthorative()) {
                eventList.add(new CalculatedEventDTO(event));
            } else {
                eventList.add(new EventDTO(event));
            }
        }));


        return eventList;
    }

    private void updateEvent(final Event old, final EventDTO updated) {
        if (updated.getName() != null)
            old.setName(updated.getName());
        if (updated.getDescription() != null)
            old.setDescription(updated.getDescription());
        if (updated.getMainImage() != null)
            old.setMainImage(updated.getMainImage());
        if (updated.getPrice() != null)
            old.setPrice(updated.getPrice());
        if (updated.getPriority() != null)
            old.setPriority(updated.getPriority());
        if (updated.getProperties() != null)
            old.getProperties().putAll(updated.getProperties());

        eventDao.update(old);
    }
}
