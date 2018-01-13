package com.isa.arnhem.isarest.services;

import com.isa.arnhem.isarest.repository.EventDao;
import com.isa.arnhem.isarest.repository.NotificationDao;
import com.isa.arnhem.isarest.repository.UserDao;
import com.isa.arnhem.isarest.models.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
@Getter
public class EventService {
    private final EventDao eventDao;
    private final UserDao userDao;

    private final NotificationDao notificationDao;

    public ResponseMessage addUserToEvent(final String eventId, final String userId) {
        final User user = userDao.findByUserId(userId);
        final Event event = eventDao.findByEventId(eventId);

        if (event == null) {
            return ResponseMessage.builder().message("Event doesn't exist!").messageType(ResponseMessageType.CLIENT_ERROR).build();
        }
        if (user == null) {
            return ResponseMessage.builder().message("User doesn't exist!").messageType(ResponseMessageType.CLIENT_ERROR).build();
        }
        final Attendee attendee = new Attendee(user.getId(), Calendar.getInstance().getTime());

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
            List<Notification> notifications = new ArrayList<>();
            admins.forEach((admin) -> {
                Notification notification = new Notification();
                notification.setUserId(admin.getId());
                notification.setMessage(user.getUsername() + " has requested to join the event: " + controlledEvent.getName());
            });
            notificationDao.create(notifications);
            return ResponseMessage.builder().message(user.getUsername() + " has requested to join the event: " + controlledEvent.getName()).messageType(ResponseMessageType.SUCCESSFUL).build();
        } else {
            event.getAttendees().add(attendee);
            eventDao.update(event);
            return ResponseMessage.builder().messageType(ResponseMessageType.SUCCESSFUL).message("Added " + user.getUsername() + " to the event: " + event.getName()).build();

        }
    }

    public ResponseMessage removeUserFromEvent(String eventId, String userId){
        User user = userDao.findByUserId(userId);
        Event event = eventDao.findByEventId(eventId);
        if (event == null) {
            return ResponseMessage.builder().message("Event doesn't exist!").messageType(ResponseMessageType.NOT_FOUND).build();
        }
        if (user == null) {
            return ResponseMessage.builder().message("User doesn't exist!").messageType(ResponseMessageType.NOT_FOUND).build();
        }
        Attendee attendee = new Attendee(userId, Calendar.getInstance().getTime());

        if (!event.getAttendees().contains(attendee)) {
            return ResponseMessage.builder().message("Event is already not being attended by this user.").messageType(ResponseMessageType.CLIENT_ERROR).build();
        }
        event.getAttendees().remove(attendee);

        eventDao.update(event);
        return ResponseMessage.builder().message("Removed " + user.getUsername() + " from the event: " + event.getName()).messageType(ResponseMessageType.SUCCESSFUL).build();

    }
}
