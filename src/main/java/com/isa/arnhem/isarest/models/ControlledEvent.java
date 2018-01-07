package com.isa.arnhem.isarest.models;

import java.util.List;


public class ControlledEvent extends Event {


    private List<Attendee> requestedAttendees;

    public List<Attendee> getRequestedAttendees() {
        return requestedAttendees;
    }

    public void setRequestedAttendees(List<Attendee> requestedAttendees) {
        this.requestedAttendees = requestedAttendees;
    }

}
