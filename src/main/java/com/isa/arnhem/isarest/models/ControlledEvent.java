package com.isa.arnhem.isarest.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ControlledEvent extends Event {

    @JsonProperty("controlled_by")
    private UserType controlledBy = UserType.ISA_MEMBER;

    @JsonProperty("requested_attendees")
    private AttendeeSet requestedAttendees = new AttendeeSet();


}
