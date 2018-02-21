package com.isa.arnhem.isarest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.isa.arnhem.isarest.models.Attendee;
import com.isa.arnhem.isarest.models.Event;
import lombok.Getter;

@Getter
public class CalculatedEventDTO extends EventDTO {


    @JsonProperty("total_attendees")
    private final int totalParticipants;


    public CalculatedEventDTO(Event event) {
        super(event);
        this.totalParticipants = getTotalParticipants(event.getAttendees());

    }


    public int getTotalParticipants(Iterable<Attendee> attendees) {
        int result = 0;
        for (Attendee attendee : attendees) {
            result += 1;
            result += attendee.getPlus();
        }

        return result;
    }
}
