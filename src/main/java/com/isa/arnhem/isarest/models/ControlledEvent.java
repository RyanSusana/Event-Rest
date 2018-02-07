package com.isa.arnhem.isarest.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ControlledEvent extends Event {

    @JsonProperty("controlled_by")
    @JsonDeserialize(using = UserTypeDeserializer.class)
    private UserType controlledBy = UserType.ISA_MEMBER;

    @JsonProperty("requested_attendees")
    private AttendeeSet requestedAttendees = new AttendeeSet();


}
