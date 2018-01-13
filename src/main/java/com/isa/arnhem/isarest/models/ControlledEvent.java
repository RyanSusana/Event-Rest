package com.isa.arnhem.isarest.models;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
public class ControlledEvent extends Event {

    private UserType controlledBy = UserType.MEMBER;

    private AttendeeSet requestedAttendees = new AttendeeSet();


}
