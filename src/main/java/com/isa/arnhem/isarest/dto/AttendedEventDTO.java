package com.isa.arnhem.isarest.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.isa.arnhem.isarest.models.event.ControlledEvent;
import com.isa.arnhem.isarest.models.event.Event;
import com.isa.arnhem.isarest.models.user.Attendee;
import com.isa.arnhem.isarest.models.user.AttendeeSet;
import lombok.Getter;

import java.util.Optional;

@Getter
public class AttendedEventDTO extends EventDTO {

    @JsonIgnore
    private final AttendeeSet attendees;


    public AttendedEventDTO(Event event, String userId, boolean requested) {
        super(event);
        if (requested) {
            this.attendees = ((ControlledEvent) event).getRequestedAttendees();
        } else {
            this.attendees = event.getAttendees();
        }
        Optional<Attendee> attendee = attendees.getAttendee(userId);
        if (!attendee.isPresent()) {
            throw new IllegalStateException("User is not attending this event!");
        }
        if (attendee.get().getPlus() > 0) {
            setName(event.getName() + String.format(" (%d tickets)", (attendee.get().getPlus() + 1)));
        } else {
            setName(event.getName());
        }


    }


}
