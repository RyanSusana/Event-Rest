package com.isa.arnhem.isarest.models.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.isa.arnhem.isarest.models.user.AttendeeSet;
import com.isa.arnhem.isarest.models.user.UserType;
import com.isa.arnhem.isarest.models.user.UserTypeDeserializer;
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
