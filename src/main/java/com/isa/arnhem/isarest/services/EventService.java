package com.isa.arnhem.isarest.services;

import com.isa.arnhem.isarest.models.*;
import com.isa.arnhem.isarest.repository.EventDao;
import com.isa.arnhem.isarest.repository.NotificationDao;
import com.isa.arnhem.isarest.repository.UserDao;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
@Getter
public class EventService {
    private final EventDao eventDao;
    private final UserDao userDao;


    private final NotificationDao notificationDao;

    public ResponseMessage addUserToEvent(final String eventId, final String userId) {
        final User user = userDao.findByString(userId).get();
        final Event event = eventDao.findById(eventId).get();

        if (event == null) {
            return ResponseMessage.builder().message("Event doesn't exist!").messageType(ResponseMessageType.CLIENT_ERROR).build();
        }
        if (user == null) {
            return ResponseMessage.builder().message("User doesn't exist!").messageType(ResponseMessageType.CLIENT_ERROR).build();
        }
        return addUserToEvent(event, user);
    }

    public ResponseMessage removeUserFromEvent(String eventId, String userId) {
        Optional<User> user = userDao.findByString(userId);
        Optional<Event> event = eventDao.findById(eventId);
        if (!event.isPresent()) {
            return ResponseMessage.builder().message("Event doesn't exist!").messageType(ResponseMessageType.NOT_FOUND).build();
        }
        if (event.get() instanceof PayedEvent) {
            return ResponseMessage.builder().message("You have already payed for this event, refunds are not allowed.").messageType(ResponseMessageType.CLIENT_ERROR).build();
        }
        if (!user.isPresent()) {
            return ResponseMessage.builder().message("User doesn't exist!").messageType(ResponseMessageType.NOT_FOUND).build();
        }
        Attendee attendee = Attendee.of(userId, Calendar.getInstance().getTime());

        if (!event.get().getAttendees().contains(attendee)) {
            return ResponseMessage.builder().message("Event is already not being attended by this user.").messageType(ResponseMessageType.CLIENT_ERROR).build();
        }

        event.get().getAttendees().remove(attendee);

        eventDao.update(event.get());
        return ResponseMessage.builder().message("Removed " + user.get().getUsername() + " from the event: " + event.get().getName()).messageType(ResponseMessageType.SUCCESSFUL).build();

    }

    private ResponseMessage addUserToEvent(Event event, User user) {
        final Attendee attendee = Attendee.of(user.getId(), Calendar.getInstance().getTime());

        if (event.getAttendees().contains(attendee)) {
            return ResponseMessage.builder().message("Event is already being attended by this user.").messageType(ResponseMessageType.CLIENT_ERROR).build();
        }
        if (event instanceof ControlledEvent) {
            final ControlledEvent controlledEvent = (ControlledEvent) (event);
            if (controlledEvent.getRequestedAttendees().contains(attendee)) {
                ResponseMessage.builder().messageType(ResponseMessageType.CLIENT_ERROR).message("You have already requested to join this event.").build();
            }
            controlledEvent.getRequestedAttendees().add(attendee);
            eventDao.update(controlledEvent);
            List<User> admins = userDao.findByTypeAndAbove(controlledEvent.getControlledBy());
            notificationDao.notify(user.getUsername() + " has requested to join the event: " + controlledEvent.getName(), NotificationType.MESSAGE, admins);
            return ResponseMessage.builder().message(user.getUsername() + " has requested to join the event: " + controlledEvent.getName()).messageType(ResponseMessageType.ACCEPTED).build();
        } else {
            event.getAttendees().add(attendee);
            eventDao.update(event);
            return ResponseMessage.builder().messageType(ResponseMessageType.SUCCESSFUL).message("Added " + user.getUsername() + " to the event: " + event.getName()).build();

        }
    }

    public ResponseMessage addUserToControlledEvent(String eventId, String userId, final Optional<User> controllerUser) {
        return addUserToControlledEvent(eventId, userId, controllerUser, 0);
    }

    public ResponseMessage addUserToControlledEvent(String eventId, String userId, final Optional<User> controllerUser, int plus) {

        final Optional<Event> event = eventDao.findById(eventId);
        final Optional<User> userToAdd = userDao.findByString(userId);
        if (!event.isPresent() || !userToAdd.isPresent()) {
            return ResponseMessage.builder().message("Event or User doesn't exist!").messageType(ResponseMessageType.CLIENT_ERROR).build();
        }
        if (!controllerUser.isPresent()) {
            return ResponseMessage.builder().message("You must be logged in to use this function!").messageType(ResponseMessageType.CLIENT_ERROR).build();

        }

        if (event.get().getAttendees().contains(Attendee.of(userToAdd.get().getId(), null))) {
            return ResponseMessage.builder().message("Event is already being attended by this user.").messageType(ResponseMessageType.CLIENT_ERROR).build();
        }
        if (event.get() instanceof ControlledEvent) {
            ControlledEvent controlledEvent = (ControlledEvent) event.get();
            if (controlledEvent.getControlledBy().equals(controllerUser.get().getType()) ||
                    controlledEvent.getControlledBy().getRanksAbove().contains(controllerUser.get().getType())) {
                Optional<Attendee> attendee = controlledEvent.getRequestedAttendees().getAttendee(userToAdd.get());

                if (!attendee.isPresent()) {
                    controlledEvent.getAttendees().add(Attendee.of(userToAdd.get().getId(), Calendar.getInstance().getTime(), plus));
                } else {
                    attendee.get().setPlus(plus);
                    controlledEvent.getAttendees().add(attendee.get());
                    controlledEvent.getRequestedAttendees().remove(attendee.get());
                }
                eventDao.update(controlledEvent);
                return ResponseMessage.builder().messageType(ResponseMessageType.ACCEPTED).message(userToAdd.get().getUsername() + " is now attending the event: " + event.get().getName()).build();

            }
            return ResponseMessage.builder().messageType(ResponseMessageType.UNAUTHORIZED).message("You are not allowed to permit users to this event!").build();
        } else {
            return addUserToEvent(event.get(), userToAdd.get());
        }

    }
}
