package com.isa.arnhem.isarest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.isa.arnhem.isarest.models.AttendeeSet;
import com.isa.arnhem.isarest.models.ControlledEvent;
import com.isa.arnhem.isarest.models.Event;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class CompleteEventDTO extends CalculatedEventDTO {
    @JsonProperty("attendees")
    private final AttendeeSet attendees;
    @JsonProperty("requested_attendees")
    private final AttendeeSet requestedAttendees;

    public CompleteEventDTO(Event event) {
        super(event);

        this.attendees = event.getAttendees();
        if (event instanceof ControlledEvent) {
            this.requestedAttendees = ((ControlledEvent) event).getRequestedAttendees();
        } else {
            requestedAttendees = null;
        }
    }
}
